# Security policy

## Supported versions

Morifolium is pre-v0.1. Security fixes are applied to the current `main` branch and, once releases exist, to the latest supported release when practical. Older source snapshots and downstream consumer copies are not automatically updated.

## Reporting a vulnerability

Do not disclose suspected vulnerabilities, credentials, tokens, signing material, or exploitable configuration details in a public issue.

Prefer GitHub's private vulnerability-reporting / security-advisory flow for this repository when it is available. If no private reporting control is available, contact a repository maintainer through an existing private channel. A public issue may be used only to request a private contact path and must not contain vulnerability details.

Include, when possible:

- affected Morifolium version or commit;
- affected reference profile;
- impact and realistic attack preconditions;
- minimal reproduction steps;
- proposed mitigation if known.

## Security boundary

Morifolium is a reference distribution, not a security boundary by itself. Consumers remain responsible for product-specific threat models, identities, authorization, backend controls, signing keys, store credentials, privacy obligations, and production incident response.

Security-sensitive defaults in Morifolium should fail closed where practical and remain inspectable in source control.

Reference-profile design and enforced defaults are documented in:

- [Mobile security baseline](docs/security/baseline.md)
- [Reference-profile threat model](docs/security/threat-model.md)
