# Morifolium

> *Chrysanthemum morifolium* — layered structure and seasonal cycles, reflecting repeatable platform rollouts and continuous refinement.

Morifolium is an **experimental mobile platform engineering golden path** for teams that need a repeatable way to turn mobile architecture, delivery, security, testing, observability, and release practices into an operable project.

It is not a new mobile application framework. Morifolium composes established platform capabilities and conventions into a reference distribution that can be cloned, evaluated, and adapted by mobile teams.

## Status

**Lifecycle: incubating / pre-v0.1**

Morifolium now has an initial executable **native Android/Kotlin reference profile** for proving the project-level delivery contract. Its canonical gate uses `mise` to validate the Android SDK, run Android lint and unit tests, and build a debug APK through the same task interface locally and in CI.

```bash
mise install
mise run ci
```

See [Getting started](docs/getting-started.md) for prerequisites and artifact details.

The reference profile is still pre-v0.1: security, observability, release, distribution, and downstream adoption contracts remain intentionally incomplete. Morifolium is **not yet production-grade** and should not be treated as a finished starter kit.

## Purpose

Large mobile teams often accumulate repeated delivery problems across application squads: inconsistent project setup, divergent CI pipelines, duplicated security work, fragile release processes, uneven test coverage, and recurring operational failures.

Morifolium exists to provide an opinionated reference for the **project and SDLC layer**:

- repeatable project bootstrap;
- canonical build and task entry points;
- layered automated testing;
- static analysis and policy checks;
- secure delivery defaults;
- privacy-aware observability;
- reproducible release workflows;
- integration guidance for specialized Micrantha mobile capabilities.

The goal is a **golden path, not a golden cage**: projects should be able to adopt the platform defaults while keeping product-specific architecture and features local.

## Ownership boundary

Morifolium owns the composition and operational contract of a reference mobile project. It does not replace the specialized projects it may integrate with.

| Concern | Primary authority |
| --- | --- |
| Mobile project golden path and SDLC composition | **Morifolium** |
| Kotlin Multiplatform application architecture | Bluebell |
| React Native / on-device inference | Amaryllis |
| Governed device capabilities | Myosotis |
| Runtime trust bootstrap / attestation | Digitalis |
| Build-time configuration hardening | Envuscator |
| Product-specific application behavior | Consuming application |

Integrations are optional unless a specific Morifolium reference profile documents otherwise.

## v0.1 milestone

The first milestone proves one bounded vertical slice:

```text
clone / generate
  -> configure
  -> build
  -> static analysis
  -> automated tests
  -> application artifact
  -> inspectable CI evidence
```

The initial Android profile proves the delivery mechanics while broader platform contracts are added through subsequent slices.

### v0.1 success criteria

A developer can:

1. understand the project boundary and supported reference profile;
2. run canonical setup, build, analysis, and test commands locally;
3. execute the same checks in CI;
4. produce a mobile application artifact;
5. inspect representative security and observability boundaries;
6. distinguish platform-owned configuration from product-owned code;
7. reproduce the workflow from documented tooling.

## Non-goals for v0.1

- supporting every Android/iOS/cross-platform stack at once;
- creating another general-purpose mobile architecture framework;
- autonomous or "self-healing" production changes;
- a broad AI feature surface;
- vendor matrices for every observability or CI provider;
- replacing product-specific application architecture.

AI-assisted diagnostics and governed remediation may be explored later only with explicit authority, approval, evidence, rollback, and blast-radius controls.

## Repository shape

Current and planned boundaries are introduced through executable slices rather than empty scaffolding:

```text
app/                 # current Android reference profile
scripts/             # canonical environment/setup helpers
docs/                # architecture, QARTs, ADRs, adoption guidance
.github/workflows/   # CI composition using canonical mise tasks
platform/            # future reusable integration glue when justified
tests/               # future cross-cutting conformance tests when justified
```

Specialized implementation concerns should remain in the projects that own them instead of being copied into Morifolium.

## Design decisions

- [QART 0001 — Morifolium project role](docs/qart/0001-project-role.md)
- [ADR 0001 — Mobile platform golden-path boundary](docs/adr/0001-mobile-platform-golden-path.md)
- [QART 0002 — Initial reference profile](docs/qart/0002-initial-reference-profile.md)
- [ADR 0002 — Native Android v0.1 reference profile](docs/adr/0002-native-android-v0.1-profile.md)

## Current priorities

1. keep the Android golden-path validation gate reproducible and green;
2. establish repository governance, licensing, and release/template policy;
3. add representative security and privacy-aware observability boundaries;
4. validate adoption through a second consumer before expanding abstractions.

## Project philosophy

Morifolium follows the Micrantha engineering model: design, implement, observe, refine. Platform defaults should remain understandable, testable, replaceable, and bounded by explicit ownership rather than growing into an opaque framework.
