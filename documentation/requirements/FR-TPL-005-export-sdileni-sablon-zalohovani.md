# FR-TPL-005 — Export/sdílení uložených šablon (zálohování)

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn use-case [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uložené šablony lze zkopírovat/sdílet stejným mechanismem jako jiné textové výstupy aplikace (copy/paste, Android Share — viz [FR-OUT-001](FR-OUT-001-export-predani-vysledku-mimo-aplikaci.md), [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md)), minimálně za účelem zálohování mimo zařízení. Export obsahuje vše potřebné pro budoucí znovupoužití šablony (vzorový text, odvozené schéma, instrukce, verzi — viz [FR-TPL-002](FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md)).

TODO: zpětný import exportované šablony (obnova ze zálohy / přenos na jiné zařízení) není tímto požadavkem řešen — otevřená otázka pro navazující požadavek, je-li zálohování bez možnosti obnovy nedostatečné.

## Akceptační kritéria

- [ ] Uloženou šablonu lze zkopírovat do schránky (copy/paste).
- [ ] Uloženou šablonu lze odeslat přes Android Share do libovolné dostupné aplikace (stejně jako u [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md)).
- [ ] Export šablony obsahuje vzorový text, odvozené schéma, instrukce a verzi — ne jen název.

## Související

- Use-case: [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md)
- ADR: [ADR-009](../decisions/ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
