# ADR 0002: Native Android v0.1 reference profile

## Status

Accepted.

## Context

ADR 0001 requires one concrete implementation stream to prove Morifolium's golden-path contract. QART 0002 evaluated Bluebell/KMP, a minimal native Android fixture, and React Native.

The Bluebell/KMP path remains desirable but is not currently a dependable build dependency from its canonical repository state. React Native would add unrelated toolchain breadth before Morifolium has proven its own SDLC boundary.

## Decision

Morifolium v0.1 will use a **minimal native Android reference application with Android Gradle Plugin built-in Kotlin support**.

The profile is a conformance fixture for the project/SDLC layer. It does not establish an application architecture that downstream products must adopt.

### Canonical tool boundary

- `mise` owns repository task entry points and the Java/Gradle tool versions;
- the Android SDK is an explicit workstation/runner prerequisite for v0.1;
- `mise run setup` validates the SDK and provisions the pinned platform/build-tools packages;
- `mise run lint`, `mise run test`, and `mise run build` are the supported local gates;
- `mise run ci` composes the same gates used by CI.

### Validation boundary

The initial pyramid is deliberately small:

1. Android lint for platform-aware static analysis;
2. JVM unit tests for repository-owned logic;
3. debug APK assembly as the end-to-end integration/artifact proof.

Device/emulator UI tests are deferred until Morifolium introduces behavior that requires device-level verification.

## Consequences

### Positive

- issue #2 can prove a real mobile artifact without waiting on another project;
- local and CI command ownership is explicit;
- no new general-purpose mobile framework is introduced;
- the test pyramid grows only when behavior justifies it.

### Negative

- the first profile is Android-only;
- Android command-line tools are not yet themselves provisioned by `mise`;
- the first fixture does not validate KMP or iOS delivery.

## Follow-up

- make the Android gate green in CI;
- record any toolchain or runner assumptions exposed by CI;
- revisit a Bluebell/KMP profile once its canonical SDK is consumable;
- address distribution, licensing, versioning, and downstream upgrade policy in issue #3.
