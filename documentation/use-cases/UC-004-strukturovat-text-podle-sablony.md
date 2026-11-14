# UC-004 — Strukturovat text podle šablony

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-004
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zobrazení obsahu šablony před zpracováním není nutné, stačí zobrazit až výsledek.
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.

## Aktér

Uživatel; sekundární aktér: LLM provider.

## Předpoklady

Existuje přepis a vybraná/rozpoznaná/výchozí šablona.

## Hlavní tok

1. Uživatel potvrdí zpracování zvolenou (případně rozpoznanou nebo výchozí) šablonou — zobrazení obsahu samotné šablony před zpracováním není nutné (viz [FR-TPL-003](../requirements/FR-TPL-003-zobrazeni-aktivni-sablony-a-zaznam-jeji-verze.md), kde je volitelné), postačí zobrazit až výsledek.
2. Aplikace odešle přepis a schéma/instrukce LLM.
3. Aplikace přijme strukturu.
4. Aplikace lokálně validuje schéma.
5. Zobrazí se výsledek a nejasnosti.

## Alternativní toky

- **A1:** Nevalidní JSON.
- **A2:** Odmítnutí zpracování modelem.
- **A3:** Chybějící povinné údaje.
- **A4:** Chybná interpretace.
- **A5:** Timeout.

## Výsledek

Vznikne strukturovaný **návrh** s ID/verzí šablony, nebo je diagnostikován neúspěch.

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): Detail nahrávky (sekce Formátovaný výstup, tlačítko „Konvertovat podle šablony“) a Šablona (potvrzení rozpoznané/zvolené šablony — potvrzením se rovnou spustí zpracování a otevře Kontrola, bez samostatné mezikrokové obrazovky).

## Související

- Požadavek: [FR-TPL-001](../requirements/FR-TPL-001-vyber-cilove-sablony.md), [FR-TPL-002](../requirements/FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md), [FR-TPL-003](../requirements/FR-TPL-003-zobrazeni-aktivni-sablony-a-zaznam-jeji-verze.md), [FR-LLM-001](../requirements/FR-LLM-001-predani-prepisu-a-sablony-llm.md), [FR-LLM-002](../requirements/FR-LLM-002-strojove-zpracovatelny-vystup-dle-json-schema.md), [FR-LLM-003](../requirements/FR-LLM-003-lokalni-validace-vystupu-llm.md), [FR-LLM-004](../requirements/FR-LLM-004-zakaz-domysleni-faktu-v-promptu.md), [FR-LLM-006](../requirements/FR-LLM-006-upozorneni-na-nejasnosti-a-chybejici-udaje.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
