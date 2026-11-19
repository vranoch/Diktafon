# ADR-006 — Schéma šablony je kontrakt

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-19
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-006

## Kontext

Výstup LLM musí být strojově zpracovatelný a konzistentně strukturovaný, aby šel validovat a mapovat do UI.

## Rozhodnutí

Každá šablona je definována verzovaným JSON Schema, doprovodnými instrukcemi pro LLM a lokální validací odpovědi vůči tomuto schématu.

**Aktualizace 2026-11-19:** pole šablony má oddělený technický klíč (`key` — stabilní, strojový,
generovaný automaticky při vzniku pole) a zobrazovaný popisek (`label` — libovolný text, který
uživatel může kdykoliv přejmenovat). Dřív mělo pole jen jediné `name`, použité současně jako
klíč do mapy vyplněných hodnot (`document.fields`) i jako text vykreslený ve výsledném
dokumentu — přejmenování pole v editoru šablony tak přímo měnilo i technický klíč, na který se
odkazovaly už strukturované dokumenty. Klíč se po vzniku pole už nemění, takže přejmenování
popisku je teď bezpečné a nerozbije dříve vytvořené dokumenty. U polí odvozených z příkladu
(mock i OpenAI, viz [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md)) se klíč
generuje jako snake_case bez diakritiky z odvozeného popisku. Starší uložená data (jen s
`name`) se při načtení dál korektně zobrazí — `key` i `label` dostanou stejnou (neslugifikovanou)
hodnotu `name`, aby zůstal zachovaný soulad s klíči v už existujících strukturovaných
dokumentech.

## Důsledky

Validita podle schématu nepotvrzuje věcnou pravdivost obsahu — lidská kontrola výsledku (viz [NFR-QUAL-002](../requirements/NFR-QUAL-002-dokument-oznacen-jako-navrh-k-lidske-kontrole.md)) zůstává povinná.
