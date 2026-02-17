# Branch: feature/oauth2-login-flow 🔐

This branch focuses on implementing the **OAuth2.1 login flow** with support for username/password, OTP verification, and token management.  
It is isolated from other features to ensure clean development and testing before merging into the main branch.

---

## 🎯 Purpose
- Introduce secure **OAuth2.1 Authorization Code Flow** with PKCE.
- Provide APIs for **login**, **OTP verification**, and **token exchange**.
- Issue **JWT access tokens** and **refresh tokens** for session management.
- Enable backend‑only testing via Postman before frontend integration.

---

## 🚀 Features in this Branch
- `/authorize` → Entry point for OAuth2.1 flow.
- `/login` → Username + password authentication.
- `/otp/verify` → Multi‑factor OTP validation.
- `/token` → Exchange authorization code for access/refresh tokens.
- **JWT validation** → Access tokens are signed and validated.
- **Refresh token support** → Silent session renewal without forcing login.

---

## 🛠️ Dependencies Added
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-client`
- `spring-boot-starter-oauth2-resource-server`
- `spring-security-oauth2-jose` (for JWT support)

---

## ✅ Workflow
1. User hits `/authorize` → gets redirected to `/login`.
2. User submits credentials → OTP is triggered.
3. User verifies OTP → receives authorization code.
4. Client exchanges code at `/token` → gets JWT access + refresh tokens.
5. Access tokens secure APIs, refresh tokens renew sessions.

---

## 📌 Status
- [x] Dependencies added
- [x] Basic endpoints defined
- [ ] JWT signing/validation implementation
- [ ] Refresh token handling
- [ ] Integration testing with Postman

---

This README is **branch‑specific** and documents only the scope of `feature/oauth2-login-flow`.  
Once complete and tested, this branch will be merged into `v0.0.1` and later into `main`.