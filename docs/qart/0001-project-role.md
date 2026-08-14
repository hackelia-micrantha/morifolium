# QART 0001: Morifolium project role

## Status

Accepted for initial implementation.

## Question

What should Morifolium own within the Micrantha mobile ecosystem?

The repository was originally described as a production-grade mobile platform engineering starter kit, but it had no implementation and its proposed scope overlapped several newer Micrantha projects with clearer ownership boundaries.

## Alternatives

### A. General mobile application framework

Morifolium would define application architecture, shared libraries, mobile UI structure, and platform abstractions.

**Advantages**

- superficially simple project story;
- one repository could appear to provide a complete application foundation.

**Costs / risks**

- duplicates Bluebell's KMP architecture and SDK role;
- risks collapsing React Native, KMP, and native streams into one framework;
- creates a large, slow-moving abstraction surface;
- makes project adoption dependent on Morifolium-specific application architecture.

### B. Loose collection of examples

Morifolium would host independent examples for CI, testing, security, observability, and mobile tooling.

**Advantages**

- low coupling;
- easy to add isolated experiments.

**Costs / risks**

- no coherent adoption path;
- examples can drift independently;
- weak verification that the combined platform actually works;
- duplicates documentation already better owned by specialized projects.

### C. Mobile platform engineering golden path

Morifolium owns a versioned reference distribution for the project and SDLC layer. It composes established capabilities into a bounded, executable path from project setup through build, analysis, tests, artifact production, release, and operational evidence.

**Advantages**

- fills a distinct ecosystem gap;
- allows specialized projects to retain authority;
- produces a concrete integration surface for platform engineering practices;
- can be validated as an end-to-end vertical slice;
- supports multiple future implementation profiles without requiring one universal framework.

**Costs / risks**

- requires disciplined dependency and ownership boundaries;
- template evolution and downstream upgrades become product concerns;
- must avoid becoming a dumping ground for every mobile convention.

## Recommendation

Adopt **Alternative C: mobile platform engineering golden path**.

Morifolium should own:

- project bootstrap and reference distribution composition;
- canonical developer/build/test/release workflows;
- integration-level validation across selected platform capabilities;
- platform defaults and adoption guidance;
- evidence that the composed mobile delivery path works end to end.

Morifolium should not own:

- a new general mobile application architecture;
- specialized attestation, hardening, local-AI, or device-capability implementations already owned elsewhere;
- product-specific application behavior;
- autonomous operational authority without a governed execution boundary.

## Tradeoffs

The golden-path model deliberately prefers a smaller opinionated integration surface over maximum framework flexibility. It requires picking one concrete reference profile for v0.1, but that restriction is useful: the first milestone can prove the SDLC contract instead of claiming unsupported multi-stack compatibility.

Additional profiles should be added only when they prove a materially different integration need and can share the same project-level contract without weakening ownership boundaries.

## Decision trigger

This QART is sufficiently resolved to support ADR 0001 and the first implementation slice.
