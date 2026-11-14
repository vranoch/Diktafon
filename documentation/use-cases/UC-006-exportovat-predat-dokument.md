# UC-006 — Exportovat/předat dokument

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-006
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — výběr příjemce ze seznamu e-mailových adres, viz [FR-CFG-004](../requirements/FR-CFG-004-sprava-seznamu-emailovych-adres.md)/[FR-OUT-004](../requirements/FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md).
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.

## Aktér

Uživatel.

## Předpoklady

Existuje schválený strukturovaný dokument.

## Hlavní tok

1. Uživatel vybere dokument.
2. Vybere dostupný cílový formát/cestu.
3. Aplikace vygeneruje export.
4. Uloží nebo sdílí výstup přes Android systém.
5. Potvrdí dokončení předání, pokud to kanál umožňuje.

## Alternativní toky

- **A1:** Cílová aplikace není na zařízení dostupná.
- **A2:** Nepodporovaný formát.
- **A3:** Uživatel zruší sdílení.
- **A4:** Uživatel zvolí odeslání e-mailem → aplikace nabídne výběr příjemce z nastaveného seznamu adres a vybranou adresu předvyplní do pole Komu (viz [FR-CFG-004](../requirements/FR-CFG-004-sprava-seznamu-emailovych-adres.md), [FR-OUT-004](../requirements/FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)).

## Výsledek

Dokument zůstává lokálně uložený i při selhání exportu.

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): Export (odeslání e-mailem s výběrem příjemce ze seznamu, export do dalších nainstalovaných aplikací) a administrační E-mailoví příjemci pro správu seznamu adres.

## Související

- Požadavek: [FR-OUT-001](../requirements/FR-OUT-001-export-predani-vysledku-mimo-aplikaci.md), [FR-OUT-002](../requirements/FR-OUT-002-export-jako-text-json-a-android-share.md), [FR-OUT-003](../requirements/FR-OUT-003-export-docx-pdf-nebo-adapter-do-ciloveho-systemu.md), [FR-CFG-004](../requirements/FR-CFG-004-sprava-seznamu-emailovych-adres.md), [FR-OUT-004](../requirements/FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
