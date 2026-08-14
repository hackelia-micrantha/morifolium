# Release and upgrade policy

## Distribution authority

Morifolium is distributed as versioned source. Immutable Git tags and GitHub releases are the authoritative snapshots of the golden path.

GitHub template mode is a convenience for creating a repository from the current default branch. Because template-generated repositories have independent history and `main` can be ahead of the latest release, the template UI is **not** a reproducible version selector.

Consumers that require a reproducible baseline should start from or reconcile against a tagged release and keep `.morifolium-version` intact.

## Versioning

Morifolium uses Semantic Versioning:

- `v0.x.y`: incubating contracts; breaking changes are permitted with explicit migration notes;
- `v1.0.0` and later: public golden-path contracts follow normal SemVer compatibility expectations.

The repository marker omits the Git tag prefix, for example:

```text
0.1.0
```

Development between releases uses a prerelease marker such as:

```text
0.1.0-dev
```

## Release gate

A release candidate must:

1. pass `mise run ci` from the exact candidate commit;
2. have no unresolved P0/P1 defect that invalidates the advertised release contract;
3. document user-visible build, toolchain, security-default, or migration changes;
4. update `.morifolium-version` to the release version;
5. use an immutable annotated/release tag `vMAJOR.MINOR.PATCH`.

Release signing, attestations, or artifact provenance should be added when Morifolium publishes an artifact whose consumer threat model benefits from them. They are not simulated before that boundary exists.

## Toolchain support

The supported toolchain is the one declared by the repository and verified by canonical CI.

For the initial Android profile:

- `mise.toml` owns Java and Gradle versions;
- the Gradle build owns Android Gradle Plugin and Android SDK levels;
- `scripts/setup-android.sh` owns required SDK packages;
- the CI workflow owns the pinned shared-workflow revision.

A toolchain upgrade is a platform change. It must be reviewable, pass `mise run ci`, and be called out in release notes when it can affect consumers.

## Dependency and Action updates

- do not use floating `latest` versions for build/runtime dependencies;
- pin external GitHub Actions and reusable workflows to immutable commit SHAs;
- prefer a single authoritative version declaration over duplicated version strings;
- dependency updates happen through reviewed changes with canonical CI evidence;
- do not add package-registry credentials merely to distribute repository scaffolding.

## Downstream migration

Morifolium upgrades are **explicit repository migrations**.

For each target release:

1. compare the consumer's `.morifolium-version` with the target release;
2. review release notes for every intervening version;
3. separate platform-owned changes from product-owned code;
4. apply the smallest coherent migration slices;
5. preserve local product decisions unless the release documents an incompatible platform requirement;
6. run the consumer's full validation gate;
7. update `.morifolium-version` after successful validation.

A future migration tool may automate discovery or patch generation, but application remains reviewable and consumer-controlled.
