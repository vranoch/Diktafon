# NFR-SEC-002 — Ochrana API klíčů

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-SEC-002
**Priorita:** MUST

## Popis

[NAVRŽENO] API klíče nejsou v APK, Git repozitáři, zálohách ani diagnostických exportech; jsou zadávány uživatelem a chráněny Android Keystore nebo jiným vhodným šifrováním.

Riziko R-001 — API klíč unikne z Android klienta; mitigace: vlastní klíč, Keystore, limity, revokace, při rozšíření proxy (viz [ADR-002](../decisions/ADR-002-bez-vlastni-serverove-casti-v-mvp.md), [ADR-007](../decisions/ADR-007-serverless-proxy-az-pri-zmene-okolnosti.md)).

## Akceptační kritéria

- [ ] Kontrola APK, repozitáře, záloh a diagnostických exportů neobsahuje hodnotu API klíče (AT-012).

## Související

- Use-case: [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- ADR: [ADR-002](../decisions/ADR-002-bez-vlastni-serverove-casti-v-mvp.md), [ADR-007](../decisions/ADR-007-serverless-proxy-az-pri-zmene-okolnosti.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
