# ADR 0001: Mobile platform golden-path boundary

## Status

Accepted.

## Context

Morifolium was initially described as a production-grade mobile platform engineering starter kit, but the repository did not contain an implementation. Since that description was written, the Micrantha mobile ecosystem has developed clearer specialized ownership boundaries for Kotlin Multiplatform architecture, React Native local AI, governed device capabilities, runtime trust bootstrap, and build-time configuration hardening.

A new Morifolium implementation therefore needs a role that integrates these capabilities without duplicating them.

QART 0001 evaluated a general application framework, a loose examples repository, and a mobile platform engineering golden path.

## Decision

Morifolium will be a **mobile platform engineering golden path and reference distribution**.

Its primary contract is the project/SDLC path from bootstrap through build, static analysis, automated tests, artifact production, release, and operational evidence.

Morifolium may integrate specialized Micrantha projects, but those projects remain authoritative for their own implementation contracts.

### Owned by Morifolium

- reference project composition;
- canonical local task entry points;
- CI/CD composition and repository-level policy;
- cross-cutting test and conformance expectations;
- integration guidance for security and observability;
- release/reference-template lifecycle;
- validation that a supported profile works end to end.

### Not owned by Morifolium

- a replacement mobile application framework;
- Bluebell's KMP architecture contract;
- Amaryllis local-inference implementation;
- Myosotis governed device-capability protocol/runtime;
- Digitalis runtime attestation/trust-bootstrap implementation;
- Envuscator build-hardening implementation;
- product-specific domain behavior.

## Initial reference profile

The first implementation milestone will select **one concrete mobile implementation stream** and use it to prove the project-level golden-path contract.

The initial backlog should evaluate Bluebell/KMP as the default reference because it already provides a cross-platform Micrantha application foundation, but the implementation choice remains a bounded executable decision of the bootstrap issue rather than an architectural requirement that every Morifolium consumer use KMP.

## Validation principle

A supported Morifolium profile is not considered implemented merely because configuration files or example directories exist.

The profile must demonstrate an executable path equivalent to:

```text
setup -> build -> static analysis -> tests -> artifact -> CI evidence
```

Local and CI entry points should invoke the same canonical checks wherever practical.

## Consequences

### Positive

- Morifolium gains an independent ecosystem role;
- specialized projects keep clear authority;
- work can proceed in independently verifiable vertical slices;
- multi-stack support can be added later without forcing a single application framework;
- platform engineering concerns become testable integration contracts rather than README claims.

### Negative

- the project must manage compatibility across selected integrations;
- template/reference-distribution upgrades require an explicit downstream migration strategy;
- maintainers must resist adding unrelated mobile abstractions merely because they are reusable.

## Follow-up

1. establish repository/tooling baseline and canonical validation commands;
2. implement one golden-path reference profile;
3. establish licensing and template/release governance;
4. synchronize Micrantha meta-registry classification once this contract lands;
5. add additional profiles or reusable abstractions only after demonstrated demand.
