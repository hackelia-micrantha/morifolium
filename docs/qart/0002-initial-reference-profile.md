# QART 0002: Initial reference profile

## Status

Accepted for v0.1 implementation.

## Question

Which mobile implementation profile should Morifolium use first to prove the golden-path contract?

The first profile must be small enough to validate the project/SDLC layer without accidentally turning Morifolium into another application framework.

## Alternatives

### A. Bluebell / Kotlin Multiplatform

This is the strongest long-term ecosystem fit because Bluebell owns Micrantha's KMP application architecture boundary.

**Current blocker:** on 2026-08-14, Bluebell's canonical `main` tree does not expose the SDK/workspace structure described by its README, so Morifolium cannot presently depend on that implementation as a verifiable build input.

### B. Minimal native Android with built-in Kotlin

Use a deliberately small Android application with no product architecture and no third-party runtime dependencies. The profile exists only to prove setup, static analysis, tests, artifact production, and CI composition.

**Advantages**

- Android artifacts can be built on Linux CI;
- Kotlin remains the implementation language without adding a second Kotlin Gradle plugin under modern AGP;
- Android lint supplies a useful static-analysis gate immediately;
- the application can remain architecture-neutral;
- KMP/Bluebell can be added later as a separate integration profile.

**Tradeoff:** this first profile proves Android delivery, not cross-platform architecture.

### C. React Native

A React Native profile would prove cross-platform application delivery but would introduce Node/package-manager/Metro concerns and overlap the Amaryllis stream before the lower-level Morifolium SDLC contract is established.

## Recommendation

Adopt **Alternative B** for v0.1.

The first profile is intentionally a delivery/conformance fixture, not the architectural center of Morifolium. Its value is demonstrating that a fresh project can use canonical commands to configure, analyze, test, and build a real mobile artifact.

Revisit Bluebell/KMP after its canonical implementation surface is directly consumable and can be integrated without copying its architecture into Morifolium.

## Tradeoffs

This chooses fast operational proof over immediate cross-platform breadth. That is acceptable because ADR 0001 makes profile expansion subordinate to the stable project-level golden-path contract.
