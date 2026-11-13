# ADR-006 — Schéma šablony je kontrakt

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-006

## Kontext

Výstup LLM musí být strojově zpracovatelný a konzistentně strukturovaný, aby šel validovat a mapovat do UI.

## Rozhodnutí

Každá šablona je definována verzovaným JSON Schema, doprovodnými instrukcemi pro LLM a lokální validací odpovědi vůči tomuto schématu.

## Důsledky

Validita podle schématu nepotvrzuje věcnou pravdivost obsahu — lidská kontrola výsledku (viz [NFR-QUAL-002](../requirements/NFR-QUAL-002-dokument-oznacen-jako-navrh-k-lidske-kontrole.md)) zůstává povinná.
