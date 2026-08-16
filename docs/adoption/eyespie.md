# Eyespie second-consumer adoption

## Status

Validated on 2026-08-15 through `ryjen/eyespie#186`.

This adoption is the first external proof that a Morifolium project/SDLC convention can be reconciled into an existing mobile repository without importing Morifolium application code or replacing the consumer's architecture.

## Consumer baseline

Eyespie is a backendless-first Kotlin Multiplatform game with its own product, domain, persistence, camera/MediaPipe, sharing, and platform boundaries.

Before this adoption, the primary Android CI contract was already small and healthy:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug --no-daemon --stacktrace
```

The workflow already provided:

- read-only repository permissions;
- cancellation of superseded CI runs;
- pinned GitHub Actions;
- Temurin Java 21;
- Gradle setup and caching;
- an independent Apple/MediaPipe validation workflow.

Those consumer-owned choices were treated as evidence to preserve, not configuration to replace merely for consistency with Morifolium.

## Adopted convention

Eyespie adopted Morifolium's **canonical repository task boundary**:

```bash
mise install
mise run ci
```

The consumer added a committed `mise.toml` that:

- declares Temurin Java 21;
- exposes `mise run test` for the existing Android unit-test target;
- exposes `mise run build` for the existing debug-assembly target;
- exposes `mise run ci` for the unchanged combined validation contract.

The existing Android GitHub Actions job now installs `mise` and invokes `mise run ci`. Gradle remains the build authority and `gradle/actions/setup-gradle` remains in place for consumer-specific setup and caching.

## Security and observability reconciliation

The second-consumer review also compared Morifolium's security and observability boundaries against Eyespie rather than assuming the reference implementation should be copied.

### Security

Eyespie already has an explicit backendless trust model. Its current Android profile:

- does not request the `INTERNET` permission;
- sets `android:allowBackup="false"`;
- intentionally exports only the launcher activity;
- documents platform-backed storage for future local identity key material;
- treats locally usable image embeddings as accessible to a sufficiently motivated device owner rather than claiming local secrecy as an authority boundary.

These choices are compatible with Morifolium's security principles but are consumer-owned. Morifolium additionally carries explicit cleartext-network denial and Network Security Configuration as future-proof defense in depth, while its current reference manifest also omits `INTERNET`. Copying that extra configuration into Eyespie would therefore add consistency rather than protect a network capability Eyespie currently exposes. If Eyespie later adds optional remote transport, the network boundary should be reviewed then against the concrete capability.

This is useful evidence for the golden path: **secure defaults should preserve an existing consumer boundary instead of mechanically copying every reference-fixture control.**

### Observability

No generic telemetry, analytics, crash-export, DSN, or remote logging implementation was identified in the current Eyespie core. That is consistent with the backendless/no-network default and there is no evidence that copying Morifolium's telemetry classes would improve the current consumer.

Morifolium's privacy-aware observability contract remains relevant as a future design constraint if Eyespie introduces operational telemetry: minimize data before a sink/export boundary, keep generic operational signals distinct from product analytics, and make remote export an explicit consumer decision. The second-consumer proof does **not** justify a shared telemetry library or mandatory exporter.

The reconciliation therefore validates the ownership rule rather than implementation reuse: Morifolium defines a reference boundary; the consumer adopts it only when the capability exists and retains authority over its product-specific privacy model.

## Validation evidence

The adoption PR passed both repository gates before merge:

- workflow-security: success;
- `mise` tool installation: success;
- Gradle setup: success;
- existing Android unit tests and debug APK assembly through `mise run ci`: success.

The PR was then squash-merged as `ryjen/eyespie#186` (`27cc291cdad259bf7a8aa78d1047eb35dbcfc0c3`).

No gameplay, persistence, identity, camera, MediaPipe, DI, KMP domain, backendless-authority, or Apple workflow behavior was changed by the adoption.

## Findings

### 1. The task boundary is portable

The useful reusable contract is the **repository-level canonical task interface**, not Morifolium's Android fixture implementation.

Morifolium's own reference profile and Eyespie have materially different application architectures, yet both can expose a small `mise` surface that maps local developer validation to CI.

### 2. Composition is preferable to replacement

Eyespie already had useful Gradle setup/caching and workflow security controls. The adoption retained them and inserted `mise` only at the tool/task boundary.

This is consistent with Morifolium's golden-path role: preserve healthy consumer-owned implementation details while standardizing the small platform contract that provides leverage.

### 3. The shared reusable CI workflow is not universally required

Morifolium uses Micrantha's reusable `mise` CI workflow, but Eyespie did not need to adopt that entire workflow. Doing so would have discarded consumer-specific Gradle setup/caching without providing additional correctness.

The more durable contract is therefore:

> CI invokes the same canonical repository task that developers can invoke locally.

A shared workflow is one implementation of that contract, not the contract itself.

### 4. Security and observability are boundary contracts, not copy targets

Eyespie already satisfies the currently relevant no-network, backup, and exported-component boundaries without copying Morifolium's complete Android security fixture, and its current core has no generic telemetry/export requirement.

The adoption supports reconciling invariants and ownership first. Reference implementations should be copied only when the consumer actually has the corresponding capability and threat/privacy requirement.

### 5. No shared runtime/package abstraction is justified

The adoption required no Morifolium library dependency and revealed no repeated runtime implementation that should be extracted into a package.

Morifolium should continue to own project/SDLC composition rather than turn the golden path into an application framework.

### 6. Template mode remains deferred

Eyespie was best served by a selective reconciliation of one convention into an established repository, not by recreating the project from Morifolium's default branch.

This evidence does not justify enabling GitHub template mode during pre-v0.1. Immutable releases remain the appropriate version authority; template bootstrap can be reconsidered after v0.1 or when a greenfield consumer demonstrates a concrete need.

### 7. Migration tooling is premature

The adoption was a small, reviewable three-file change. There is no demonstrated repeated migration burden that warrants a generator, updater, or automated migration mechanism yet.

### 8. This does not create a KMP Morifolium profile

Eyespie proves that the project-level task convention works in a KMP consumer. It does not prove that Morifolium itself supports or owns a KMP reference profile, nor does it transfer Eyespie's application architecture into Morifolium.

## Resulting decision

Second-consumer evidence supports keeping Morifolium's current architecture unchanged:

- retain the versioned source-distribution model;
- retain `mise` as the canonical repository task boundary;
- allow consumers to preserve stack-specific build tooling and CI setup behind that boundary;
- reconcile security and observability at the ownership/invariant level before copying implementation;
- do not add a shared runtime package;
- do not enable GitHub template mode yet;
- do not add migration tooling yet;
- do not claim an additional KMP reference profile from this adoption alone.

The next bounded milestone is therefore a **v0.1 release candidate** of the contracts already proven, rather than another abstraction-expansion slice.
