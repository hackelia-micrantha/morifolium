# ADR 0003: Versioned source distribution and template bootstrap

## Status

Accepted.

## Context

Morifolium's primary artifact is a repository-level golden path. It contains build tooling, CI composition, static-analysis policy, security defaults, reference application code, documentation, and future operational integrations. A conventional runtime package cannot carry that full contract.

QART 0003 evaluated template-only distribution, package distribution, and versioned source distribution with template bootstrap.

## Decision

Morifolium will use **immutable Git tags/releases as its version authority** and may expose the repository as a **GitHub template for bootstrap convenience**.

Template generation is not an upgrade mechanism and does not replace release versioning.

### Version lineage

The root `.morifolium-version` file records the source lineage of the golden path. During pre-release development it uses a SemVer-compatible prerelease value such as `0.1.0-dev`. Released snapshots use their release version.

Consumers created from or migrated to Morifolium should retain this marker even if they substantially customize the application.

### Releases

- releases use SemVer tags in the form `vMAJOR.MINOR.PATCH`;
- tags are immutable once published;
- `main` is the active development line and may be ahead of the latest release;
- every release must pass the canonical `mise run ci` gate from the tagged commit;
- pre-1.0 releases may make breaking changes, but breaking migration steps must be documented;
- release notes identify toolchain, security-default, and migration changes relevant to consumers.

### Supply-chain policy

- GitHub Actions and reusable workflows are pinned to immutable commit SHAs;
- repository-managed tool versions are pinned in `mise.toml` or the owning build configuration;
- application/build dependencies use explicit versions rather than floating `latest` selectors;
- dependency/toolchain upgrades land through reviewable pull requests and must pass canonical CI;
- CI receives only the permissions required by its jobs; the baseline validation workflow is read-only.

### Downstream upgrades

A consumer upgrades deliberately:

1. identify its current `.morifolium-version`;
2. select a target Morifolium release;
3. review intervening release notes and migration instructions;
4. apply repository-level changes by concern rather than overwriting product-owned code;
5. update `.morifolium-version` only after the migration is complete;
6. run the consumer's full canonical validation gate.

Automated upgrade tooling may be added later, but it must preserve this explicit review boundary.

## Consequences

### Positive

- the complete project/SDLC contract remains versioned together;
- consumers do not require package-registry credentials;
- source provenance is inspectable through immutable tags and the lineage marker;
- GitHub template mode remains available for low-friction experimentation;
- future libraries can still be extracted independently when reuse is proven.

### Negative

- upgrades require migration work rather than a dependency-version bump;
- template-created repositories do not share history with Morifolium;
- maintainers must keep release notes and migrations useful enough for downstream consumers.

## Follow-up

- enable GitHub template-repository mode after this policy lands;
- publish the first release only when the v0.1 acceptance contract is satisfied;
- add release automation/provenance only when a concrete release artifact requires it.
