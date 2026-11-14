# UC-005 — Opravit a schválit návrh dokumentu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-005
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.

## Aktér

Uživatel.

## Předpoklady

Existuje strukturovaný návrh dokumentu.

## Hlavní tok

1. Zobrazí se dokument.
2. Uživatel jej porovná s přepisem (případně audiem).
3. Opraví pole.
4. Uloží změny.
5. Označí dokument za připravený k exportu.

## Alternativní toky

- **A1:** Návrat k přepisu a jeho úprava.
- **A2:** Opětovné spuštění strukturování.
- **A3:** Přerušení editace a pokračování později.

## Výsledek

Existuje uložený, uživatelsky zkontrolovaný dokument.

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): Kontrola — přímo editovatelný výsledný formátovaný dokument s viditelně označenými chybějícími poli, sbalitelný doslovný přepis jako zdroj k porovnání a akce „Uložit změny“ / „Schválit a pokračovat“ / „Zrušit převod“.

## Související

- Požadavek: [FR-REV-001](../requirements/FR-REV-001-kontrola-a-oprava-vysledneho-dokumentu.md), [FR-REV-002](../requirements/FR-REV-002-odliseni-prepisu-od-strukturovaneho-vysledku-v-ui.md), [FR-REV-003](../requirements/FR-REV-003-zachovani-editaci-pri-obnove-ui-rotaci-ukonceni.md), [NFR-QUAL-002](../requirements/NFR-QUAL-002-dokument-oznacen-jako-navrh-k-lidske-kontrole.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
