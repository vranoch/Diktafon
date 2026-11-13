# UC-007 — Nastavit externí API

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-13
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-007
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — výslovně označeno jako administrační použití (viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).

## Aktér

Uživatel / správce jediného zařízení (administrační použití — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).

## Předpoklady

Žádné — jde o počáteční konfiguraci.

## Hlavní tok

1. Uživatel vybere providera.
2. Bezpečně vloží vlastní API klíč.
3. Aplikace ověří spojení bez úniku klíče do logů.
4. Uživatel nastaví model a případný rozpočtový limit.
5. Konfigurace se uloží.

## Alternativní toky

- **A1:** Neplatný klíč.
- **A2:** Nedostatečné kredity u providera.
- **A3:** Nepodporovaný model.
- **A4:** Offline režim.

## Výsledek

Existuje použitelná konfigurace, nebo srozumitelná diagnostika chyby.

## Související

- Požadavek: [FR-CFG-001](../requirements/FR-CFG-001-sprava-api-klice.md), [FR-CFG-002](../requirements/FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md), [FR-CFG-003](../requirements/FR-CFG-003-informovani-o-chybach-limitech-a-offline-stavu.md), [NFR-SEC-001](../requirements/NFR-SEC-001-https-a-validace-certifikatu.md), [NFR-SEC-002](../requirements/NFR-SEC-002-ochrana-api-klicu.md), [NFR-SEC-005](../requirements/NFR-SEC-005-rozpoctove-limity-api.md)
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
