# FR-TPL-006 — Smazání šablony nebo její vybrané verze

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-16
**Změněno:** 2026-11-16
**Zdroje:**
- [Chat-extract — Ukládání/mazání šablon a formátování vzorového textu, 2026-11-16](../source-materials/chat-extracts/2026-11-16-sablony-ukladani-mazani-format.md)
- [Chat-extract — Mazání vybrané verze šablony, 3 možnosti v dialogu ukládání, 2026-11-16](../source-materials/chat-extracts/2026-11-16-mazani-vybrane-verze-sablony.md) — upřesnění: primárně jde o smazání jedné vybrané verze, ne nutně všech najednou.
**Priorita:** SHOULD / NEXT

## Popis

Ze správy šablon lze zobrazit všechny verze jedné šablony a smazat buď jen vybranou verzi
(ostatní verze zůstanou zachované), nebo všechny verze dané šablony najednou. Smazání vyžaduje
explicitní potvrzení (stejný princip jako u ostatních destruktivních akcí, viz
[FR-DATA-001](FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md)). Dokumenty dříve zpracované
smazanou šablonou (nebo její smazanou verzí) zůstávají beze změny — svůj vlastní obsah polí si
nesou nezávisle na existenci šablony v katalogu (viz [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)).

## Akceptační kritéria

- [ ] Ze seznamu šablon lze zobrazit všechny verze jedné šablony.
- [ ] Lze smazat jen jednu vybranou verzi šablony po explicitním potvrzení; ostatní verze
      zůstanou zachované.
- [ ] Lze smazat i všechny verze dané šablony najednou po explicitním potvrzení.
- [ ] Dříve vytvořené strukturované dokumenty zůstanou po smazání šablony/verze čitelné beze
      změny.

## Související

- Use-case: [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md)
- FR: [FR-TPL-002](FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md), [FR-DATA-001](FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [documentation/modules/README.md](../modules/README.md))
