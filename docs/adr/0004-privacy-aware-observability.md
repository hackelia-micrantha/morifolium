# ADR 0004: Privacy-aware operational telemetry boundary

## Status

Accepted.

## Context

Morifolium promises observability as part of its golden path but does not yet have a concrete product, provider account, consent model, retention policy, or remote telemetry requirement. QART 0004 evaluated vendor-first integration, free-form telemetry, and a minimal local operational contract.

## Decision

Morifolium will own a **minimal, privacy-aware operational telemetry contract** whose policy executes before the sink boundary.

The initial reference implementation provides:

- `OperationalEvent` with a low-cardinality name, outcome, optional duration, and metadata;
- `TelemetryPolicy` with a strict default metadata allowlist and enum-like value constraints;
- `PrivacyAwareTelemetry` which filters before emission;
- `TelemetrySink` as the provider/export seam;
- `InMemoryTelemetrySink` as the only built-in reference sink.

No remote exporter or vendor SDK is part of the initial contract.

## Data boundary

Generic operational telemetry must not contain credentials, tokens, authorization headers, cookies, signing material, user identifiers, user content, request/response payloads, or regulated/sensitive product data.

The reference filter is defense in depth. It cannot determine whether every syntactically valid label is semantically sensitive, so consumers remain responsible for ensuring allowlisted values are derived from low-cardinality program state rather than user/product data.

## Adapter boundary

A future provider integration implements `TelemetrySink` and receives only `TelemetryRecord` values after Morifolium policy has executed.

Provider selection, network export, consent, retention, identifiers, credentials, sampling, cost, and residency remain consumer-owned.

## Consequences

### Positive

- observability works in tests without a service account or network endpoint;
- generic telemetry is data-minimized before provider code;
- vendor lock-in is avoided at the golden-path layer;
- the contract is independently testable and reusable.

### Negative

- the initial sink is not a production telemetry backend;
- consumers must deliberately design richer telemetry when needed;
- policy cannot replace semantic data classification or a product privacy model.

## Follow-up

- validate the contract in a second mobile consumer through issue #10;
- add a remote/vendor adapter only when a concrete consumer supplies the missing privacy, credential, and operational requirements;
- keep the security threat model synchronized if remote export introduces new trust boundaries.
