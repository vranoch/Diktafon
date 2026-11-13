# ADR-009 — Šablony vznikají z příkladu přes LLM, s iterativním testováním

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn use-case [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md).

## Kontext

[ADR-006](ADR-006-schema-sablony-je-kontrakt.md) definuje šablonu jako verzované JSON Schema s instrukcemi. Při revizi se ukázalo, že přímá ruční editace JSON Schema na cílovém zařízení (BOOX, E Ink, netechnický uživatel — viz [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md)) není realistická. Je potřeba způsob tvorby/úpravy šablon, který nevyžaduje znalost JSON Schema.

## Rozhodnutí

Tvorba/úprava šablony probíhá tak, že uživatel zadá vzorový text výsledného dokumentu (případně s doplňujícími poznámkami k proměnným polím), a aplikace z něj pomocí LLM odvodí JSON Schema a instrukce — stejným provider-neutrálním mechanismem jako u samotného strukturování (viz [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md)). Uživatel může draft šablony před uložením opakovaně vyzkoušet na existující nahrávce/přepisu (reuse pipeline z [ADR-003](ADR-003-oddelene-kroky-stt-a-llm.md)/[UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)) a návrh upravovat, dokud není s výsledkem spokojen (viz [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md)).

## Důsledky

Odvozené schéma/instrukce podléhají stejnému principu jako výstup strukturování — jde o návrh k ověření uživatelem, ne o automaticky důvěryhodný výsledek (konzistentní s [FR-LLM-003](../requirements/FR-LLM-003-lokalni-validace-vystupu-llm.md), [FR-LLM-004](../requirements/FR-LLM-004-zakaz-domysleni-faktu-v-promptu.md), [NFR-QUAL-002](../requirements/NFR-QUAL-002-dokument-oznacen-jako-navrh-k-lidske-kontrole.md)). Vzniká potřeba UI pro iterativní zkoušení draftu šablony, nerozlišeného od uložené verze, dokud uživatel neuloží. Vzniká i potřeba zálohy/exportu uložených šablon (viz [FR-TPL-005](../requirements/FR-TPL-005-export-sdileni-sablon-zalohovani.md)), protože bez ní je ruční práce na vytvoření šablony snadno ztratitelná. Tvorba šablony nově představuje další, samostatné volání LLM (náklady/čas) nad rámec samotného zpracování diktátů.
