# ADR-003 — Oddělené kroky STT a LLM

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-003

## Kontext

Zpracování audia na strukturovaný dokument lze řešit buď odděleně (přepis → strukturování), nebo jedním multimodálním voláním audio→struktura.

## Rozhodnutí

Výchozí workflow je audio → přepis (STT) → strukturování (LLM) jako dva oddělené kroky.

## Důsledky

Oddělení umožňuje auditovatelnost, editaci přepisu mezi kroky, izolované opakování jednotlivých kroků a porovnání providerů. Multimodální audio→struktura v jednom volání zůstává experimentální alternativou pro fázi NEXT (viz [FR-AI-002](../requirements/FR-AI-002-multimodalni-pipeline-audio-struktura.md)).
