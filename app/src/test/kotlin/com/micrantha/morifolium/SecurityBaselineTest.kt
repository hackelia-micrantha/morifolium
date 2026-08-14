package com.micrantha.morifolium

import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.w3c.dom.Document
import org.w3c.dom.Element

class SecurityBaselineTest {
    private val androidNamespace = "http://schemas.android.com/apk/res/android"
    private val repoRoot = Path.of(
        requireNotNull(System.getProperty("morifolium.repoRoot")) {
            "morifolium.repoRoot must be provided by the Gradle test task"
        },
    )

    @Test
    fun manifestEnforcesRepositoryOwnedSecurityDefaults() {
        val document = parse("app/src/main/AndroidManifest.xml")
        val application = document.getElementsByTagName("application").item(0) as? Element
        assertNotNull("manifest must contain an application element", application)
        application!!

        assertEquals("false", application.getAttributeNS(androidNamespace, "allowBackup"))
        assertEquals(
            "@xml/data_extraction_rules",
            application.getAttributeNS(androidNamespace, "dataExtractionRules"),
        )
        assertEquals(
            "@xml/backup_rules",
            application.getAttributeNS(androidNamespace, "fullBackupContent"),
        )
        assertEquals("false", application.getAttributeNS(androidNamespace, "usesCleartextTraffic"))
        assertEquals(
            "@xml/network_security_config",
            application.getAttributeNS(androidNamespace, "networkSecurityConfig"),
        )

        val exportedComponents = buildList {
            listOf("activity", "activity-alias", "service", "receiver", "provider").forEach { tag ->
                val nodes = document.getElementsByTagName(tag)
                for (index in 0 until nodes.length) {
                    val element = nodes.item(index) as Element
                    if (element.getAttributeNS(androidNamespace, "exported") == "true") {
                        add(element.getAttributeNS(androidNamespace, "name"))
                    }
                }
            }
        }

        assertEquals(listOf(".MainActivity"), exportedComponents)
    }

    @Test
    fun networkPolicyDeniesCleartextWithoutDomainExceptions() {
        val document = parse("app/src/main/res/xml/network_security_config.xml")
        val baseConfig = document.getElementsByTagName("base-config").item(0) as? Element
        assertNotNull("network security config must contain a base-config", baseConfig)
        assertEquals("false", baseConfig!!.getAttribute("cleartextTrafficPermitted"))

        val domainConfigs = document.getElementsByTagName("domain-config")
        for (index in 0 until domainConfigs.length) {
            val element = domainConfigs.item(index) as Element
            assertFalse(
                "domain exceptions must not re-enable cleartext traffic",
                element.getAttribute("cleartextTrafficPermitted") == "true",
            )
        }
    }

    @Test
    fun backupPoliciesDenyAllSupportedStorageDomains() {
        val expectedDomains = setOf(
            "root",
            "file",
            "database",
            "sharedpref",
            "external",
            "device_root",
            "device_file",
            "device_database",
            "device_sharedpref",
        )

        assertEquals(
            expectedDomains,
            excludedDomains(parse("app/src/main/res/xml/backup_rules.xml")),
        )
        assertEquals(
            expectedDomains,
            excludedDomains(parse("app/src/main/res/xml/data_extraction_rules.xml")),
        )
    }

    private fun excludedDomains(document: Document): Set<String> {
        val excludes = document.getElementsByTagName("exclude")
        return buildSet {
            for (index in 0 until excludes.length) {
                add((excludes.item(index) as Element).getAttribute("domain"))
            }
        }
    }

    private fun parse(relativePath: String): Document {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
            setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            setFeature("http://xml.org/sax/features/external-general-entities", false)
            setFeature("http://xml.org/sax/features/external-parameter-entities", false)
            setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "")
            setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "")
        }

        return Files.newInputStream(repoRoot.resolve(relativePath)).use { input ->
            factory.newDocumentBuilder().parse(input)
        }
    }
}
