# FR-OUT-004 — Výběr příjemce ze seznamu adres při odeslání e-mailem

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Nový požadavek uživatele v rámci revize, 2026-11-13 (tato konverzace).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Pokud uživatel při exportu/sdílení (nahrávky, přepisu i výsledného dokumentu — viz [FR-OUT-001](FR-OUT-001-export-predani-vysledku-mimo-aplikaci.md), [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md)) zvolí odeslání e-mailem, aplikace mu místo (nebo před) otevřením obecného systémového sdílení nabídne výběr z nastaveného seznamu e-mailových adres (viz [FR-CFG-004](FR-CFG-004-sprava-seznamu-emailovych-adres.md)). Po výběru adresy (případně více adres) ji aplikace předvyplní do pole příjemce (Komu) otevíraného e-mailu. Uživatel může výběr přeskočit nebo adresu ručně upravit/doplnit — výběr ze seznamu usnadňuje časté opakované odesílání, nenahrazuje běžné odeslání e-mailu.

## Akceptační kritéria

- [ ] Při volbě odeslání e-mailem nabídne aplikace výběr z nastaveného seznamu adres (FR-CFG-004).
- [ ] Po výběru se adresa/adresy předvyplní do pole Komu otevíraného e-mailu.
- [ ] Výběr lze přeskočit; adresu lze i po výběru ručně upravit nebo doplnit další adresy.
- [ ] Mechanismus platí jak pro export/sdílení nahrávky nebo přepisu, tak pro export/sdílení výsledného dokumentu.

## Související

- Use-case: [UC-006](../use-cases/UC-006-exportovat-predat-dokument.md)
- Požadavek: [FR-CFG-004](FR-CFG-004-sprava-seznamu-emailovych-adres.md), [FR-OUT-001](FR-OUT-001-export-predani-vysledku-mimo-aplikaci.md), [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [`documentation/modules/README.md`](../modules/README.md))
