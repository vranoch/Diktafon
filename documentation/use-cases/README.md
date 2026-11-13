# documentation/use-cases/

Use-case nebo popis procesu — oboje používá stejnou šablonu, zvol pojmenování podle toho, co
lépe sedí (interakce uživatele se systémem → use-case; vícekrokový proces bez jednoho aktéra
→ proces). Podléhá lifecyclu a povinným metadatům podle [AGENTS.md §5](../../AGENTS.md).

## Konvence

- Soubor `UC-NNN-kratky-nazev.md`.
- Struktura: metadata blok → **Aktér** → **Předpoklady** → **Hlavní tok** → **Alternativní
  toky** (pokud jsou) → **Výsledek/Postcondition**.
- Odkazuj na související požadavky ([`requirements/`](../requirements/)) a moduly
  ([`modules/`](../modules/)), které use-case realizují.

Viz [`_template.md`](_template.md) pro šablonu nového use-case/procesu.

## Index

| ID | Název | Stav | Změněno | Shrnutí |
|---|---|---|---|---|
| [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md) | Pořídit a uložit hlasový záznam | PŘIJATO | 2026-11-13 | Zkratka do naposledy použité složky nebo volba složky; runtime oprávnění k mikrofonu bez pádu. |
| [UC-002](UC-002-importovat-externi-nahravku.md) | Importovat externí nahrávku | PŘIJATO | 2026-11-12 | Vznik nahrávky jako v UC-001 (složka/název/poznámka), import místo záznamu mikrofonem. |
| [UC-003](UC-003-prevest-nahravku-na-cesky-text.md) | Převést nahrávku na český text | PŘIJATO | 2026-11-12 | Existuje verzovaný přepis ve zvoleném jazyce (výchozí čeština), nebo jasná chyba. |
| [UC-004](UC-004-strukturovat-text-podle-sablony.md) | Strukturovat text podle šablony | PŘIJATO | 2026-11-12 | Zobrazení šablony předem není nutné; vznikne strukturovaný návrh s ID/verzí šablony. |
| [UC-005](UC-005-opravit-a-schvalit-navrh-dokumentu.md) | Opravit a schválit návrh dokumentu | PŘIJATO | 2026-11-12 | Existuje uložený, uživatelsky zkontrolovaný dokument. |
| [UC-006](UC-006-exportovat-predat-dokument.md) | Exportovat/předat dokument | PŘIJATO | 2026-11-13 | Dokument zůstává lokálně uložený i při selhání exportu; e-mail lze předvyplnit ze seznamu adres. |
| [UC-007](UC-007-nastavit-externi-api.md) | Nastavit externí API | PŘIJATO | 2026-11-13 | Existuje použitelná konfigurace, nebo srozumitelná diagnostika chyby (administrační použití). |
| [UC-008](UC-008-obnovit-prerusene-zpracovani.md) | Obnovit přerušené zpracování | PŘIJATO | 2026-11-12 | Uživatel nepřijde o dosud uložená data. |
| [UC-009](UC-009-vymazat-vlastni-data.md) | Vymazat vlastní data | PŘIJATO | 2026-11-12 | Lokální smazání je provedeno a diagnostikováno. |
| [UC-010](UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md) | Zpracovat audio s nadiktovanou šablonou | PŘIJATO | 2026-11-12 | Šablona se vybírá ze schváleného katalogu, ne volně z audia. |
| [UC-011](UC-011-organizovat-nahravky-do-slozek.md) | Organizovat nahrávky do složek | PŘIJATO | 2026-11-13 | Procházení stromu složek jako prohlížeč; přesun/mazání jen prázdné složky; vede ke kontextovým akcím na nahrávce. |
| [UC-012](UC-012-vytvorit-a-otestovat-sablonu.md) | Vytvořit, upravit a otestovat šablonu | PŘIJATO | 2026-11-13 | Šablona vzniká ze vzorového textu přes LLM, draft lze opakovaně testovat před uložením (administrační použití). |
