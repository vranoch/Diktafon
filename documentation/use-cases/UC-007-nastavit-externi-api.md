# UC-007 — Nastavit externí API

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-007
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — výslovně označeno jako administrační použití (viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu; jazyky přepisu se spravují samostatně mimo toto API nastavení.

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

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): Administrace → Připojení (API klíče, provider pro STT a LLM). Správa jazyků nabízených při přepisu je samostatná položka Administrace (viz [UC-003](UC-003-prevest-nahravku-na-cesky-text.md#obrazovky-a-tok)).

## Související

- Požadavek: [FR-CFG-001](../requirements/FR-CFG-001-sprava-api-klice.md), [FR-CFG-002](../requirements/FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md), [FR-CFG-003](../requirements/FR-CFG-003-informovani-o-chybach-limitech-a-offline-stavu.md), [NFR-SEC-001](../requirements/NFR-SEC-001-https-a-validace-certifikatu.md), [NFR-SEC-002](../requirements/NFR-SEC-002-ochrana-api-klicu.md), [NFR-SEC-005](../requirements/NFR-SEC-005-rozpoctove-limity-api.md)
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
