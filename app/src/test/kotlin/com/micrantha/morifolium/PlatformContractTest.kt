package com.micrantha.morifolium

import org.junit.Assert.assertEquals
import org.junit.Test

class PlatformContractTest {
    @Test
    fun identifierNamesTheMilestoneAndProfile() {
        assertEquals("v0.1:android-kotlin", PlatformContract.identifier())
    }
}
