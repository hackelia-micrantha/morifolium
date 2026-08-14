package com.micrantha.morifolium.observability

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class TelemetryTest {
    @Test
    fun sensitiveMetadataIsOmittedBeforeItReachesTheSink() {
        val sink = InMemoryTelemetrySink()
        val telemetry = PrivacyAwareTelemetry(sink)

        telemetry.record(
            OperationalEvent(
                name = "app_start",
                outcome = TelemetryOutcome.SUCCESS,
                durationMs = 42,
                metadata = mapOf(
                    "component" to "main_activity",
                    "operation" to "startup",
                    "result" to "ok",
                    "authorization" to "Bearer super-secret-token",
                    "user_email" to "person@example.com",
                    "payload" to "private medical note",
                ),
            ),
        )

        val record = sink.snapshot().single()
        assertEquals(
            mapOf(
                "component" to "main_activity",
                "operation" to "startup",
                "result" to "ok",
            ),
            record.metadata,
        )

        val emitted = record.toString()
        assertFalse(emitted.contains("super-secret-token"))
        assertFalse(emitted.contains("person@example.com"))
        assertFalse(emitted.contains("private medical note"))
    }

    @Test
    fun suspiciousValuesAreDroppedEvenWhenTheMetadataKeyIsAllowed() {
        val sink = InMemoryTelemetrySink()
        val telemetry = PrivacyAwareTelemetry(sink)

        telemetry.record(
            OperationalEvent(
                name = "request_finished",
                outcome = TelemetryOutcome.FAILURE,
                metadata = mapOf(
                    "component" to "person@example.com",
                    "operation" to "bearer_token_refresh",
                    "error_category" to "network_timeout",
                    "retryable" to "true",
                ),
            ),
        )

        assertEquals(
            mapOf(
                "error_category" to "network_timeout",
                "retryable" to "true",
            ),
            sink.snapshot().single().metadata,
        )
    }

    @Test
    fun sinkSnapshotsAreLocalAndDefensive() {
        val sink = InMemoryTelemetrySink()
        val telemetry = PrivacyAwareTelemetry(sink)

        telemetry.record(
            OperationalEvent(
                name = "build_ready",
                outcome = TelemetryOutcome.SUCCESS,
            ),
        )

        val firstSnapshot = sink.snapshot()
        telemetry.record(
            OperationalEvent(
                name = "build_verified",
                outcome = TelemetryOutcome.SUCCESS,
            ),
        )

        assertEquals(1, firstSnapshot.size)
        assertEquals(2, sink.snapshot().size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun arbitraryFreeFormEventNamesAreRejected() {
        OperationalEvent(
            name = "User ryan@example.com opened medical record 42",
            outcome = TelemetryOutcome.SUCCESS,
        )
    }
}
