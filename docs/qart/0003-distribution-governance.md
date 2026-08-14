# QART 0003: Distribution and upgrade governance

## Status

Accepted for pre-v0.1 governance.

## Question

How should Morifolium be distributed so teams can bootstrap from the golden path without turning the repository into an authenticated package dependency or losing all upgrade lineage after copying it?

## Alternatives

### A. GitHub template repository only

Consumers create a new repository from Morifolium's default branch.

**Advantages**

- minimal adoption friction;
- copies the complete repository shape, including CI and project-level configuration;
- no package registry credentials.

**Costs / risks**

- generated repositories have unrelated Git history;
- template creation does not create an ongoing upgrade channel;
- consumers can unknowingly bootstrap from an arbitrary `main` commit rather than a reviewed release.

### B. Consumable Gradle/package artifact

Move reusable platform behavior into a versioned package and make consumer applications depend on it.

**Advantages**

- conventional dependency versioning and upgrades;
- strong machine-readable dependency relationship.

**Costs / risks**

- a package cannot represent repository-level CI, workflow permissions, task conventions, documentation, or project bootstrap completely;
- introduces package publication and potentially registry credentials before the platform contract requires them;
- risks turning Morifolium into another application framework rather than a project/SDLC golden path.

### C. Versioned source distribution with optional future GitHub template bootstrap

Treat immutable Git tags/releases as the authoritative Morifolium distribution. Preserve the option to enable GitHub template mode later for low-friction bootstrap, while recording Morifolium lineage in a repository marker and documenting explicit upgrades between releases.

**Advantages**

- preserves the whole repository-level golden path;
- requires no package registry or consumer credentials;
- gives production consumers an immutable version authority;
- keeps future template bootstrap available without exposing an unstable pre-v0.1 default branch today;
- leaves room to publish narrower libraries later if repeated implementation reuse justifies them.

**Costs / risks**

- upgrades are explicit migrations rather than dependency-manager updates;
- if template mode is enabled later, generated repositories must preserve their lineage marker deliberately;
- template mode alone is never a reproducible release selector because `main` can be ahead of the latest release.

## Recommendation

Adopt **Alternative C**, but **defer enabling GitHub template mode until Morifolium has a v0.1 release or a concrete consumer proves that default-branch template bootstrap is useful**.

Morifolium releases and their immutable tags define reproducible source versions. Consumers retain `.morifolium-version` and apply documented migrations when adopting a newer release.

Template mode, if enabled later, is a bootstrap UI rather than the version authority. A package should be introduced only for a narrower reusable runtime/build component whose independent versioned API has been demonstrated by multiple consumers.

## Tradeoffs

This deliberately favors inspectable source, explicit migrations, and release-backed adoption over premature convenience. It accepts more deliberate upgrade work in exchange for keeping CI, security defaults, repository policy, build conventions, and application fixtures reviewable as one versioned reference distribution.
