# Mobile security baseline

## Purpose

This baseline defines security controls that Morifolium itself can own and verify in the reference distribution. It is deliberately smaller than a product threat model: a real application still owns its users, backend, authorization model, production data, privacy obligations, signing credentials, and incident response.

The baseline is enforced by source control, Android lint, `SecurityBaselineTest`, and the canonical `mise run ci` gate.

## Baseline requirements

| ID | Requirement | Reference enforcement |
| --- | --- | --- |
| MSEC-01 | Cleartext application traffic is denied by default. | Manifest + `network_security_config.xml` + conformance test |
| MSEC-02 | Application data is excluded from cloud backup and device transfer by default. | Manifest + backup/data-extraction rules + conformance test |
| MSEC-03 | Cross-application component exposure is explicit and minimal. | Only launcher activity is exported + conformance test |
| MSEC-04 | Validation builds require no production secrets, service credentials, signing keys, or endpoints. | Debug-only build contract + read-only CI |
| MSEC-05 | Security-sensitive configuration remains inspectable and fails closed when a safe generic default exists. | Repository-owned XML/configuration + lint/tests |
| MSEC-06 | Specialized trust/hardening implementations remain owned by their dedicated projects. | Documented integration boundary |

## Network transport

The Android reference profile sets both an application intent (`usesCleartextTraffic=false`) and a Network Security Configuration whose base policy denies cleartext traffic. No domain exception is present.

This is a platform guardrail, not proof that every possible networking library or raw socket implementation is incapable of sending cleartext. Product applications must review the behavior of networking stacks they add.

Certificate pinning is intentionally **not** a generic Morifolium default. Pinning creates certificate lifecycle and outage tradeoffs that require a concrete backend/operational contract.

## Local data, backup, and transfer

The reference fixture stores no product data. Its generic default is nevertheless deny-by-default for Android cloud backup and device-to-device transfer. A consumer that needs backup must define its data classification and explicitly narrow what is allowed rather than inheriting an implicit platform default.

## Component exposure

The reference application exports only `MainActivity`, because Android launcher discovery requires an externally visible launcher entry point. Services, receivers, providers, and future activities remain non-exported unless a concrete integration contract requires otherwise.

A new exported component is therefore a security-relevant platform change and must update the conformance expectation deliberately.

## Identity, authentication, and biometrics

Morifolium does not define user identity or backend authorization.

Biometric or device-credential prompts can be useful for **local user-presence gating**, such as controlling access to locally protected key material or a sensitive device action. They are not, by themselves:

- proof of application-user identity to a backend;
- server-side authorization;
- a session-lifecycle policy;
- an MFA architecture;
- permission to perform a remote operation.

Products must design those controls around their backend identity, session, recovery, and risk model.

## Secrets and signing material

The canonical validation build requires no production secrets. CI has read-only repository permission and the reference build does not configure release signing credentials.

Future release signing must treat keystores/private keys and store credentials as external secret material. They must not be committed into the golden-path repository or embedded into generated source.

## Logging and observability

Credentials, tokens, authorization headers, signing material, private keys, user content, and regulated/sensitive product data must not be treated as generic telemetry.

Issue #9 owns the first executable privacy-aware observability contract. Until then, Morifolium does not add a remote logging or crash-reporting endpoint.

## Specialized Micrantha integrations

- **Digitalis** may provide runtime trust-bootstrap / attestation integration for a product that needs it.
- **Envuscator** may provide build-time configuration hardening for selected mobile configuration.

Morifolium may demonstrate how those capabilities are composed into a project, but it does not duplicate their implementation or imply that either replaces product identity, authorization, backend security, or local consent/policy.
