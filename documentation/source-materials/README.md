# documentation/source-materials/

Surové vstupy — **read-only**, nejsou to výstupy, jen podklady, ze kterých vznikají formální
artefakty jinde v `documentation/`. Nic odsud se needituje zpětně — pokud je potřeba podklad
opravit, přidá se nový soubor/verze, starý zůstává.

- `chat-extracts/` — datované záznamy delších diskuzí (s uživatelem, s AI), které vedly
  k nějakému rozhodnutí nebo požadavku. Jmenná konvence: `YYYY-MM-DD-kratky-popis.md`.
  Viz [AGENTS.md §5.5](../../AGENTS.md).
- další podsložky (poznámky ze schůzek, originály specifikací, ...) se přidávají podle potřeby.

## Index — chat-extracts

| Datum | Soubor | Shrnutí |
|---|---|---|
| 2026-11-14 | [2026-11-14-ux-ui-principy-pro-e-ink.md](chat-extracts/2026-11-14-ux-ui-principy-pro-e-ink.md) | Rozhodnutí o cílovém zařízení, konkrétních číslech v NFR-UX-002 a procesu záznamu UX návrhu (vedlo k ADR-015). |
| 2026-11-14 | [2026-11-14-sprava-jazyku-prepisu.md](chat-extracts/2026-11-14-sprava-jazyku-prepisu.md) | Z ručního testu APK vzešlý požadavek na správu seznamu povolených jazyků přepisu (vedlo k FR-CFG-005). |
| 2026-11-15 | [2026-11-15-realne-stt-llm-openai.md](chat-extracts/2026-11-15-realne-stt-llm-openai.md) | Rozhodnutí implementovat reálné OpenAI STT/LLM API s přepínačem mock (výchozí)/OpenAI (vedlo k aktualizaci ADR-004). |
| 2026-11-16 | [2026-11-16-revize-pred-spustenim-nahravani.md](chat-extracts/2026-11-16-revize-pred-spustenim-nahravani.md) | Rozhodnutí vložit mezikrok revize názvu/poznámky před zahájením nahrávání (vedlo k aktualizaci FR-AUD-005). |
| 2026-11-16 | [2026-11-16-sablony-ukladani-mazani-format.md](chat-extracts/2026-11-16-sablony-ukladani-mazani-format.md) | Potvrzení přepsání/nové šablony před uložením, mazání šablon, Markdown formátování vzoru (vedlo k FR-TPL-006). |
| 2026-11-16 | [2026-11-16-mazani-vybrane-verze-sablony.md](chat-extracts/2026-11-16-mazani-vybrane-verze-sablony.md) | Upřesnění: mazání vybrané jednotlivé verze šablony, 3 možnosti v dialogu ukládání (přepsat verzi/nová verze/nová šablona). |
| 2026-11-16 | [2026-11-16-zpetna-vazba-editace-draftu-auto-doplneni.md](chat-extracts/2026-11-16-zpetna-vazba-editace-draftu-auto-doplneni.md) | Zpětná vazba při odvozování, editovatelný draft polí/instrukcí, automaticky doplňovaná pole (vedlo k FR-TPL-007). |
