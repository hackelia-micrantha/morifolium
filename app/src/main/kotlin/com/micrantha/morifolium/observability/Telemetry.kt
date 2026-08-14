package com.micrantha.morifolium.observability

enum class TelemetryOutcome {
    SUCCESS,
    FAILURE,
    CANCELLED,
}

data class OperationalEvent(
    val name: String,
    val outcome: TelemetryOutcome,
    val durationMs: Long? = null,
    val metadata: Map<String, String> = emptyMap(),
) {
    init {
        require(EVENT_NAME.matches(name)) {
            "event name must be a low-cardinality snake_case identifier"
        }
        require(durationMs == null || durationMs >= 0) {
            "durationMs must be non-negative when present"
        }
    }

    private companion object {
        val EVENT_NAME = Regex("^[a-z][a-z0-9_]{0,63}$")
    }
}

data class TelemetryRecord(
    val name: String,
    val outcome: TelemetryOutcome,
    val durationMs: Long?,
    val metadata: Map<String, String>,
)

fun interface TelemetrySink {
    fun emit(record: TelemetryRecord)
}

class InMemoryTelemetrySink : TelemetrySink {
    private val records = mutableListOf<TelemetryRecord>()

    @Synchronized
    override fun emit(record: TelemetryRecord) {
        records += record
    }

    @Synchronized
    fun snapshot(): List<TelemetryRecord> = records.toList()
}

class PrivacyAwareTelemetry(
    private val sink: TelemetrySink,
    private val policy: TelemetryPolicy = TelemetryPolicy(),
) {
    fun record(event: OperationalEvent) {
        sink.emit(
            TelemetryRecord(
                name = event.name,
                outcome = event.outcome,
                durationMs = event.durationMs,
                metadata = policy.filter(event.metadata),
            ),
        )
    }
}

class TelemetryPolicy(
    private val allowedMetadataKeys: Set<String> = DEFAULT_ALLOWED_METADATA_KEYS,
) {
    fun filter(metadata: Map<String, String>): Map<String, String> =
        metadata.entries
            .asSequence()
            .filter { it.key in allowedMetadataKeys }
            .filter { SAFE_VALUE.matches(it.value) }
            .filterNot { entry ->
                FORBIDDEN_VALUE_FRAGMENTS.any { fragment ->
                    entry.value.contains(fragment, ignoreCase = true)
                }
            }
            .sortedBy { it.key }
            .associate { it.key to it.value }

    private companion object {
        val DEFAULT_ALLOWED_METADATA_KEYS = setOf(
            "component",
            "operation",
            "result",
            "error_category",
            "retryable",
        )

        val SAFE_VALUE = Regex("^[a-z][a-z0-9_.:-]{0,63}$")

        val FORBIDDEN_VALUE_FRAGMENTS = listOf(
            "authorization",
            "bearer",
            "cookie",
            "password",
            "private_key",
            "secret",
            "token",
        )
    }
}
