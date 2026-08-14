# Repository governance

## Ownership

Morifolium owns the mobile project/SDLC golden-path contract. Product code and specialized Micrantha capabilities retain their own authorities as documented in ADR 0001.

Repository ownership is recorded in `.github/CODEOWNERS`. Changes to ownership boundaries, distribution semantics, or security authority require design evidence appropriate to their impact (QART/ADR when a durable decision is involved).

## Change policy

Changes should be small enough to review and independently verify. The canonical local and CI entry point is `mise run ci`.

A change that alters a consumer-facing golden-path contract should include documentation and, once releases exist, migration/release-note impact.

## CI permissions

Validation workflows are read-only by default. The repository-level CI workflow grants only `contents: read`. A future job that needs a write permission must declare it narrowly at the smallest useful scope and document why it is required.

## Supply-chain rules

- pin third-party GitHub Actions/reusable workflows to immutable commit SHAs;
- pin repository-managed build tools and dependencies to explicit versions;
- avoid secrets in validation jobs;
- introduce publishing credentials only with a concrete release boundary;
- review dependency/toolchain updates through the same canonical CI gate as application changes.

## Template and release distinction

Template mode answers **how to start quickly**. Releases answer **which Morifolium version is authoritative**.

Consumers should not infer compatibility from the date a repository was generated. Preserve `.morifolium-version` and use release/migration documentation to reason about lineage.
