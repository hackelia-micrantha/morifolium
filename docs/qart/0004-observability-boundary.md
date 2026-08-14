# QART 0004: Privacy-aware observability boundary

## Status

Accepted for the initial reference profile.

## Question

What observability abstraction should Morifolium own before a real consumer has selected a crash/performance/telemetry provider?

## Alternatives

### A. Vendor-first SDK integration

Select a hosted crash/performance platform and wire its SDK into the reference application.

**Advantages**

- realistic dashboards and network delivery immediately;
- vendor-specific production features are available quickly.

**Costs / risks**

- requires an account, endpoint/DSN, credentials, retention decisions, and product privacy assumptions;
- makes one provider appear to be part of the golden-path contract;
- can encourage collection before data classification exists.

### B. Free-form logging/event maps

Expose arbitrary strings/maps and leave filtering to each sink.

**Advantages**

- maximally flexible;
- trivial to instrument.

**Costs / risks**

- sensitive values can cross the boundary before a provider adapter has a chance to reason about them;
- high-cardinality/user-derived event names and metadata create privacy, cost, and operability problems;
- every adapter must repeat policy.

### C. Minimal operational event contract with local sink

Define low-cardinality events, apply a privacy policy before the sink boundary, and provide an in-memory sink for deterministic tests. Leave remote export to consumer-owned adapters.

**Advantages**

- works without accounts or secrets;
- keeps data minimization ahead of vendor code;
- provides a stable seam for future providers;
- deterministic and testable in canonical CI;
- distinguishes platform operations from product analytics.

**Costs / risks**

- does not provide production dashboards by itself;
- strict generic labels may be insufficient for a specific product;
- filtering cannot infer semantic sensitivity from syntax alone.

## Recommendation

Adopt **Alternative C**.

The generic contract remains intentionally narrow. Consumers widen it only through an explicit privacy/threat-model decision, and remote adapters operate on already-filtered records.

## Tradeoffs

Morifolium accepts less immediate telemetry richness in exchange for deterministic local operation, lower credential/privacy coupling, and a clearer product-vs-platform ownership boundary.
