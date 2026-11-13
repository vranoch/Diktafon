# ADR-002 — Bez vlastní serverové části v MVP

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-002

## Kontext

Pro jednoho uživatele (ASM-001) a rychlý vývoj je otázkou, zda stavět vlastní backendovou vrstvu, nebo volat externí STT/LLM API přímo z klienta (ASM-004, ASM-005).

## Rozhodnutí

V MVP se volá externí STT/LLM API přímo z Android APK, bez vlastní serverové infrastruktury.

## Důsledky

Dlouhodobý API klíč uložený v klientovi lze na kompromitovaném zařízení potenciálně získat; Android Keystore riziko snižuje, ale neodstraňuje (riziko R-001, viz [NFR-SEC-002](../requirements/NFR-SEC-002-ochrana-api-klicu.md)). Revisit trigger: více uživatelů, produkční citlivá data, potřeba centrálních nákladových limitů nebo bezpečnostní požadavek (viz [ADR-007](ADR-007-serverless-proxy-az-pri-zmene-okolnosti.md)).
