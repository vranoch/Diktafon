# Chat-extract — Zpětná vazba při odvozování, editovatelný draft, automaticky doplňovaná pole

**Datum:** 2026-11-16

## Kontext diskuze

Po zprovoznění reálného OpenAI odvozování draftu šablony (viz
[chat-extract z téhož dne o odvozování draftu](2026-11-16-sablony-ukladani-mazani-format.md) a
navazující oprava) uživatel při dalším testu zjistil tři věci:

1. Při spuštění "Odvodit draft šablony" / testu na přepisu appka nijak nenaznačí, že síťový
   požadavek běží — žádný indikátor průběhu.
2. Odvozená pole (názvy, povinnost) ani odvozené instrukce pro LLM nešlo po odvození nijak
   upravit — zobrazovala se jen jako prostý, needitovatelný text.
3. Vzorový text obsahuje datum a čas vzniku záznamu (např. "15. 7. 2025, 8:00"), ale protože se
   mluvené slovo při nahrávání/přepisu obvykle nediktuje, LLM takové pole při strukturování podle
   šablony nemá z čeho vyplnit.

## Rozhodnutí uživatele

1. Přidat zpětnou vazbu (indikátor průběhu) při odvozování draftu i při testu na přepisu.
2. Umožnit odvozená pole (název, povinnost) i instrukce pro LLM před uložením upravit.
3. Umožnit u vybraných polí šablony (typicky datum a čas) nastavit automatické doplnění z
   metadat nahrávky místo spoléhání na LLM — konkrétně datum a čas vzniku nahrávky.

## Navazující artefakty

- [FR-TPL-007 — Automaticky doplňovaná pole šablony](../../requirements/FR-TPL-007-automaticky-doplnovana-pole-sablony.md)
