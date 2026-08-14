# Privacy-aware observability contract

## Purpose

Morifolium needs operational evidence without making a crash-reporting, analytics, or logging vendor part of the golden-path authority. The reference contract therefore starts with **local, deterministic, low-cardinality operational events** and an adapter boundary that can be implemented by a consumer when remote export is justified.

The reference implementation lives under `app/src/main/kotlin/com/micrantha/morifolium/observability/` and is covered by JVM tests in the canonical `mise run ci` gate.

## Operational telemetry is not product analytics

The reference contract is for platform/operational signals such as:

- a bounded operation succeeded, failed, or was cancelled;
- a component or operation category was involved;
- a low-cardinality error category occurred;
- a duration was observed.

It is **not** a generic channel for user behavior, business analytics, user content, arbitrary exception messages, request/response bodies, or identifiers.

## Data minimization

`OperationalEvent` deliberately has a small shape:

- low-cardinality snake_case event name;
- outcome enum;
- optional non-negative duration;
- metadata that passes `TelemetryPolicy`.

The default metadata policy allows only these keys:

- `component`
- `operation`
- `result`
- `error_category`
- `retryable`

Values must be short, lowercase, enum-like identifiers. The reference policy also drops representative credential-bearing patterns such as bearer/token/secret/password/cookie/private-key values.

This filter is **defense in depth, not semantic data classification**. A product must not derive otherwise-valid labels from user names, account identifiers, health/financial data, message content, or other sensitive values merely because they match the syntax.

## Prohibited generic telemetry

The following data classes must not enter the generic Morifolium operational event contract:

- passwords, API keys, signing material, private keys, tokens, cookies, authorization headers;
- email addresses, phone numbers, account/user/device identifiers;
- message bodies, documents, prompts, attachments, request/response payloads;
- health, financial, authentication-recovery, or other regulated/sensitive product data;
- raw exception messages or stack-associated data when they may include user/product content;
- production endpoint credentials or DSNs.

A consumer with a legitimate need for a sensitive or identifying telemetry field must define that decision in its own privacy/threat model rather than widening the generic golden path silently.

## Local reference sink

`InMemoryTelemetrySink` stores already-filtered `TelemetryRecord` values only in process memory. It performs no network I/O, file persistence, Android logging, account lookup, device identification, or third-party SDK call.

This makes the reference contract deterministic and usable in tests without:

- a vendor account;
- a DSN/API token;
- an endpoint;
- consent/retention configuration that belongs to a real product.

## Vendor adapter boundary

`TelemetrySink` is the narrow adapter boundary:

```text
OperationalEvent
    -> PrivacyAwareTelemetry
        -> TelemetryPolicy
            -> TelemetryRecord
                -> TelemetrySink
```

A future Crashlytics/Sentry/OpenTelemetry/custom adapter belongs **after** the policy boundary and must receive the already-filtered `TelemetryRecord`, not the original arbitrary metadata map.

A product adding remote export owns:

- provider selection;
- privacy/consent basis;
- endpoint/account credentials;
- retention and residency policy;
- sampling and cost controls;
- user/device identifiers, if any;
- incident/debug access;
- provider-specific redaction and transport configuration.

## Network export

Morifolium provides no remote telemetry sink in the reference profile. Network export is opt-in and consumer-owned. The existing mobile security baseline continues to deny cleartext traffic if a consumer later adds an exporter.

## Failure behavior

Observability must not become application authority. A telemetry sink failure must not grant permissions, bypass product policy, or become a prerequisite for core application correctness.

The current in-memory sink is intentionally simple; asynchronous buffering, persistence, retries, and delivery guarantees are deferred until a concrete consumer requires them.
