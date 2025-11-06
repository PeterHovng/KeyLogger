# Security Policy

**Repository:** PeterHovng/KeyLogger  
**Purpose:** Research / educational code. This repository contains dual-use functionality (keylogging, screenshots, exfiltration). It is intended for research and defensive/security education only.

---

## Reporting a Vulnerability

If you discover a security vulnerability in this repository, please follow the responsible disclosure steps below:

1. **Do not** post exploit details in public issues, pull requests, or social media. Public disclosure may enable misuse.
2. Prefer secure, private communication: send an email to **leehoanggiadai@gmail.com** with:
   - Subject: `[SECURITY] <short summary>`
   - A short description of the issue and steps to reproduce.
   - Environment details (OS, language/runtime version, configuration).
   - Severity estimate (High / Medium / Low) if possible.
   - Proof-of-concept (PoC) or sample code that is safe and non-destructive, or instructions for reproducing.
3. If email is not feasible, open a GitHub issue and clearly mark it as a private/security report and request a private channel; however, email is strongly preferred to avoid accidental leaks.

---

## Response Commitments

- **Acknowledgement:** within **72 hours** of receiving a valid report.
- **Preliminary assessment:** within **7 days** of acknowledgement.
- **Fix / mitigation:** aim to provide a patch, workaround, or mitigation guidance within **30 days** where feasible. If more time is required, maintainers will communicate a timeline to the reporter.

> Note: This is a research repository, not production software. Response times may vary depending on maintainer availability and the severity of the issue.

---

## Scope

**In scope**
- Code and configuration in this repository (source files, scripts, documentation, and sample configuration files).
- Demonstrations, PoCs, or examples included in the repository that illustrate keylogging, screenshot capture, or data exfiltration techniques.

**Out of scope**
- Binaries or builds provided by third parties that were not produced by the project maintainers.
- External systems (email providers, remote hosts) that are not controlled by the project.
- Misconfigurations or issues in user environments that are unrelated to the repository code.

---

## Disclosure Policy

- The maintainer will coordinate with the reporter on the disclosure timeline.
- After a fix has been released, the maintainer will work with the reporter to agree on a public disclosure schedule.
- If the reporter does not respond within **90 days** after a patch is made available, the maintainer may publish a security advisory summarizing the issue and remediation steps without releasing exploit details.

---

## Mitigations and Best Practices

- **Do not run** this code on systems you do not own or do not have explicit written permission to test.
- Remove all real credentials (email addresses, passwords, API keys, app passwords) from code before publishing.
- Use sandboxed or isolated test environments and disposable accounts for testing.
- Avoid shipping compiled binaries or automated payloads in a public repository. Prefer source code and safe examples only.
- When sharing sensitive information with maintainers, consider encrypting with PGP. Request the maintainer's public key via the security email prior to sending.

---

## Legal & Ethical Notice

This repository contains dual-use research code. It is provided **"AS IS"** for research and education. The maintainer is not responsible for misuse by third parties. Using this software in a way that violates laws or regulations may expose the user to civil or criminal liability—use responsibly and in accordance with local law.

---

## Contact

- Security contact email: **leehoanggiadai@gmail.com**  
- Maintainer (GitHub): https://github.com/PeterHovng

If you would like to send encrypted reports, email first to request the maintainer's PGP key.
