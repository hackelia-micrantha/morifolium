# Morifolium reference-profile threat model

## Status and scope

This threat model covers the **Morifolium repository, CI/build path, and native Android reference fixture**. It does not claim to model a future consumer's product backend, user population, regulated data, authentication system, production signing infrastructure, or third-party services.

The objective is to identify threats introduced or amplified by a reusable golden path and to enforce the controls that Morifolium can own without inventing a fictional product.

## Assets

1. **Golden-path source integrity** — build files, CI workflows, security defaults, tests, and migration guidance copied or adopted by consumers.
2. **Build provenance and dependency intent** — pinned tool/action/dependency choices and the evidence produced by canonical CI.
3. **Consumer secret material** — future API credentials, signing keys, store credentials, and service tokens that must remain outside repository source.
4. **Application-local data** — data a future consumer may store on the device even though the reference fixture currently stores none.
5. **Network confidentiality/integrity** — data exchanged by future application integrations.
6. **Security boundary clarity** — the distinction between platform defaults and controls that remain product/backend responsibilities.

## Actors

- repository maintainers and contributors;
- trusted CI runners;
- a legitimate device/application user;
- a malicious local application attempting cross-app interaction;
- an attacker with access to device backups or transferred application data;
- a hostile or compromised network path;
- a compromised dependency, build plugin, Action, or upstream distribution source;
- an attacker who obtains a leaked credential/signing secret from source, logs, or CI.

## Trust boundaries

```text
source/review
    |
    v
CI + dependency/tool bootstrap
    |
    v
built APK ---- future release-signing boundary
    |
    v
device / Android application sandbox
    |             \
    |              -> other local applications
    v
network boundary -> future product backend/services
```

Optional Digitalis or Envuscator integrations add their own separately owned trust boundaries; Morifolium composes them but does not redefine them.

## Threats and controls

| Threat | Example | Morifolium control | Residual/product responsibility |
| --- | --- | --- | --- |
| Build/supply-chain tampering | Floating Action/tool silently changes behavior | Immutable workflow SHA, explicit tool/dependency versions, reviewed CI | Upstream compromise, artifact provenance, release signing |
| Credential disclosure | Secret committed or required by generic CI | Validation requires no production credentials; CI is read-only | Product secret store, credential rotation, store/signing access |
| Cleartext network exposure | Endpoint regresses from HTTPS to HTTP | Network Security Configuration denies cleartext by default | Review added networking libraries/protocols and backend TLS posture |
| Cross-app component abuse | New exported receiver/service accepts attacker input | Only launcher activity exported; conformance test enumerates exports | Validate any future intentional IPC contract and permissions |
| Backup/transfer data disclosure | Sensitive local state copied off device | Backup/device transfer deny-by-default rules | Product-specific data classification and explicit exceptions |
| Security control confusion | Biometrics treated as backend authentication | Baseline states biometrics are local user-presence gates only | Product identity, MFA, sessions, authorization, recovery |
| Sensitive telemetry | Token/user content enters logs | No remote telemetry in baseline; privacy contract deferred to #9 | Product data taxonomy, consent, retention, provider configuration |
| Signing-key compromise | Release keystore stored with template | No release key in repository or validation build | External signing service/secret storage, access control, rotation |

## Abuse cases

### A malicious application invokes an exposed component

The Android package surface must not grow implicitly. `SecurityBaselineTest` fails if another activity, service, receiver, provider, or alias is marked exported without updating the expected contract.

### A consumer introduces an HTTP endpoint

The base network configuration denies cleartext traffic. A product exception therefore requires an explicit configuration change that is visible in review. The generic profile contains no cleartext exception.

### A consumer stores sensitive local state

Backup and transfer are denied by default. Enabling either requires the product to identify which data is safe to migrate and to change the repository-owned policy deliberately.

### A reusable CI change requests more authority

The baseline workflow has only `contents: read`. A job requiring write permissions or secrets must justify that capability at the narrowest job/workflow boundary instead of broadening generic validation.

## Assumptions

- Android platform sandbox and standard package-signature boundaries are functioning as designed on supported devices.
- Source review and GitHub repository authorization remain outside the application runtime boundary.
- The reference app does not contain production endpoints, accounts, regulated data, or release signing credentials.
- Consumers will add product-specific threat models when they introduce real identities, data, backends, or privileged device capabilities.

## Residual risks

- Platform cleartext policy is a guardrail and cannot prove arbitrary third-party/native networking code never emits unencrypted traffic.
- Dependency pinning does not eliminate compromise of a pinned upstream artifact or build environment.
- A debug APK is not evidence of a production signing/release chain.
- Device compromise/root-level attackers are not generically solved by the reference application.
- Future product requirements may legitimately relax a baseline default; such changes must be explicit, reviewed, and covered by the consumer threat model.

## Review triggers

Revisit this threat model when Morifolium adds:

- a second mobile profile or consumer;
- user authentication/session examples;
- persistent sensitive data;
- remote observability;
- release signing or store publishing;
- Digitalis/Envuscator integration;
- agentic/AI-driven operational effects.
