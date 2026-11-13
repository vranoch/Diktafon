# documentation/requirements/

Funkční a nefunkční požadavky na systém. Každý požadavek je samostatný soubor, podléhá
lifecyclu a povinným metadatům podle [AGENTS.md §5](../../AGENTS.md).

## Konvence

- **Funkční požadavek:** `FR-NNN-kratky-nazev.md`
- **Nefunkční požadavek:** `NFR-NNN-kratky-nazev.md`
- Čísluje se průběžně a napříč typy se čísla needitelí (FR a NFR mají oddělené řady).
- Struktura: metadata blok → **Popis** → **Akceptační kritéria** → volitelně **Související**
  (odkazy na use-case/ADR/modul, kterého se požadavek týká).
- Zrušený požadavek se neodstraňuje — přejde do stavu `ZAMÍTNUTO`/`CANCELLED` a zůstává.

Viz [`_template.md`](_template.md) pro šablonu nového požadavku.

## Index

### Funkční požadavky (FR)

| ID | Název | Stav | Změněno | Shrnutí |
|---|---|---|---|---|
| [FR-AUD-001](FR-AUD-001-novy-zaznam-vestavenym-mikrofonem.md) | Nový záznam vestavěným mikrofonem | PŘIJATO | 2026-11-12 | Uživatel může vytvořit nový zvukový záznam vestavěným mikrofonem zařízení. |
| [FR-AUD-002](FR-AUD-002-import-existujici-nahravky.md) | Import existující nahrávky | PŘIJATO | 2026-11-12 | Import existující nahrávky z úložiště/externího diktafonu přes systémový file picker. |
| [FR-AUD-003](FR-AUD-003-ui-stavu-nahravani-a-ochrana-pred-zahozenim.md) | UI stavu nahrávání a ochrana před zahozením | PŘIJATO | 2026-11-12 | UI umožňuje Start/Pauza/Pokračovat/Stop (nahrávka po částech) a chrání před náhodným zahozením. |
| [FR-AUD-004](FR-AUD-004-lokalni-ulozeni-audia-pred-odeslanim-do-stt.md) | Lokální uložení audia před odesláním do STT | PŘIJATO | 2026-11-12 | Originální audio se uloží lokálně před odesláním do STT; chyba API jej nesmí smazat. |
| [FR-AUD-005](FR-AUD-005-seznam-nahravek-s-metadaty.md) | Seznam nahrávek s metadaty, názvem a poznámkou | PŘIJATO | 2026-11-12 | Název/poznámka editovatelné kdykoliv; při startu nahrávání nabídnut předgenerovaný název. |
| [FR-AUD-006](FR-AUD-006-prehrani-nahravky-pred-odeslanim.md) | Přehrání nahrávky před odesláním | PŘIJATO | 2026-11-12 | Uživatel si může záznam před odesláním přehrát. |
| [FR-AUD-007](FR-AUD-007-nahravani-pri-zhasnutem-displeji-na-pozadi.md) | Nahrávání při zhasnutém displeji / na pozadí | PŘIJATO | 2026-11-12 | Záznam při zhasnutém displeji/na pozadí — vyžaduje ověření na HW (OVĚŘIT). |
| [FR-AUD-008](FR-AUD-008-externi-mikrofon-a-tlacitko-start-stop.md) | Externí mikrofon a tlačítko Start/Stop | PŘIJATO | 2026-11-12 | Podpora externího mikrofonu a tlačítka Start/Stop (NEXT). |
| [FR-AUD-009](FR-AUD-009-spojovani-epizod-do-jednoho-zaznamu.md) | Spojování epizod do jednoho záznamu | PŘIJATO | 2026-11-12 | Spojení více nahrávacích epizod do jednoho logického záznamu (NEXT). |
| [FR-AUD-010](FR-AUD-010-rozpoznani-nadiktovane-sablony.md) | Rozpoznání nadiktované šablony | PŘIJATO | 2026-11-12 | Rozpoznání nadiktované šablony jako návrh; uživatel jej může před zpracováním změnit. |
| [FR-AUD-011](FR-AUD-011-organizace-nahravek-do-slozek.md) | Organizace nahrávek do složek | PŘIJATO | 2026-11-13 | Složky jako strom libovolné hloubky; mazání jen prázdné; naposledy použitá složka jako výchozí. |
| [FR-TPL-001](FR-TPL-001-vyber-cilove-sablony.md) | Výběr cílové šablony | PŘIJATO | 2026-11-12 | Výběr v UI, nebo nadiktováním v nahrávce s možností změny před zpracováním. |
| [FR-TPL-002](FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md) | Struktura šablony (ID, verze, schéma, instrukce) | PŘIJATO | 2026-11-12 | Šablona obsahuje ID, verzi, popis polí, instrukce a JSON Schema výstupu. |
| [FR-TPL-003](FR-TPL-003-zobrazeni-aktivni-sablony-a-zaznam-jeji-verze.md) | Zobrazení aktivní šablony a záznam její verze | PŘIJATO | 2026-11-12 | Zobrazení šablony před zpracováním je volitelné; ID/verze se vždy zaznamená do dokumentu. |
| [FR-TPL-004](FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md) | Vytvoření a úprava šablony z příkladu s testováním na nahrávce | PŘIJATO | 2026-11-12 | Šablona vzniká z příkladového textu přes LLM; lze ji zkoušet na nahrávce před uložením. |
| [FR-TPL-005](FR-TPL-005-export-sdileni-sablon-zalohovani.md) | Export/sdílení uložených šablon (zálohování) | PŘIJATO | 2026-11-12 | Šablonu lze zkopírovat/sdílet stejně jako jiný text, kvůli záloze. |
| [FR-STT-001](FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md) | Odeslání audia do STT a uložení přepisu | PŘIJATO | 2026-11-12 | Odeslání audia do STT; jazyk přepisu volitelný, výchozí čeština. |
| [FR-STT-002](FR-STT-002-jazykove-a-terminologicke-napovedy-pro-stt.md) | Jazykové a terminologické nápovědy pro STT | PŘIJATO | 2026-11-12 | Předání jazyka a terminologických nápověd STT službě, pokud je podporuje. |
| [FR-STT-003](FR-STT-003-editovatelnost-prepisu.md) | Editovatelnost přepisu | PŘIJATO | 2026-11-12 | Přepis lze upravit před i po strukturování, i perem/stylusem dle zařízení. |
| [FR-LLM-001](FR-LLM-001-predani-prepisu-a-sablony-llm.md) | Předání přepisu a šablony LLM | PŘIJATO | 2026-11-12 | Předání přepisu a vybrané šablony externímu LLM pro vytvoření dokumentu. |
| [FR-LLM-002](FR-LLM-002-strojove-zpracovatelny-vystup-dle-json-schema.md) | Strojově zpracovatelný výstup dle JSON Schema | PŘIJATO | 2026-11-12 | LLM musí vrátit výstup validovatelný podle JSON Schema. |
| [FR-LLM-003](FR-LLM-003-lokalni-validace-vystupu-llm.md) | Lokální validace výstupu LLM | PŘIJATO | 2026-11-12 | Lokální validace výstupu LLM a jasná signalizace chyb schématu. |
| [FR-LLM-004](FR-LLM-004-zakaz-domysleni-faktu-v-promptu.md) | Zákaz domýšlení faktů v promptu | PŘIJATO | 2026-11-12 | Prompt zakazuje domýšlení faktů; nejisté/chybějící hodnoty se viditelně označí, nevymyslí. |
| [FR-LLM-005](FR-LLM-005-opakovani-jen-stt-nebo-jen-strukturovani.md) | Opakování jen STT nebo jen strukturování | PŘIJATO | 2026-11-12 | Opakování jednotlivého kroku (STT nebo strukturování) s použitím uloženého přepisu. |
| [FR-LLM-006](FR-LLM-006-upozorneni-na-nejasnosti-a-chybejici-udaje.md) | Upozornění na nejasnosti a chybějící údaje | PŘIJATO | 2026-11-12 | Nejasnosti/chybějící údaje jsou v samostatném seznamu i viditelně označené u pole. |
| [FR-AI-001](FR-AI-001-zamenitelna-rozhrani-provideru-stt-llm.md) | Zaměnitelná rozhraní providerů STT/LLM | PŘIJATO | 2026-11-12 | STT a LLM integrace přes zaměnitelná rozhraní providerů. |
| [FR-AI-002](FR-AI-002-multimodalni-pipeline-audiostruktura.md) | Multimodální pipeline audio→struktura | PŘIJATO | 2026-11-12 | Alternativní multimodální pipeline audio→struktura (NEXT). |
| [FR-REV-001](FR-REV-001-kontrola-a-oprava-vysledneho-dokumentu.md) | Kontrola a oprava výsledného dokumentu | PŘIJATO | 2026-11-12 | Oprava dokumentu před předáním, i perem/stylusem dle zařízení. |
| [FR-REV-002](FR-REV-002-odliseni-prepisu-od-strukturovaneho-vysledku-v-ui.md) | Odlišení přepisu od strukturovaného výsledku v UI | PŘIJATO | 2026-11-12 | UI rozlišuje přepis od interpretovaného strukturovaného výsledku. |
| [FR-REV-003](FR-REV-003-zachovani-editaci-pri-obnove-ui-rotaci-ukonceni.md) | Zachování editací při obnově UI/rotaci/ukončení | PŘIJATO | 2026-11-12 | Editace se nesmí ztratit při obnově UI, rotaci ani ukončení aplikace. |
| [FR-OUT-001](FR-OUT-001-export-predani-vysledku-mimo-aplikaci.md) | Export/předání výsledku mimo aplikaci | PŘIJATO | 2026-11-13 | Potvrzeno: copy/paste a sdílení do libovolného dostupného kanálu (e-mail, OneNote, Drive...). |
| [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md) | Export jako formátovaný text a Android Share (JSON doplňkově) | PŘIJATO | 2026-11-13 | Export v zobrazeném formátu (např. MD), copy/paste, Android Share; JSON jen doplňkově. |
| [FR-OUT-003](FR-OUT-003-export-docx-pdf-nebo-adapter-do-ciloveho-systemu.md) | Export DOCX/PDF nebo adaptér do cílového systému | PŘIJATO | 2026-11-12 | Export DOCX/PDF nebo adaptér do cílového IS (NEXT). |
| [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md) | Výběr příjemce ze seznamu adres při odeslání e-mailem | PŘIJATO | 2026-11-13 | Výběr z nastaveného seznamu adres a předvyplnění do pole Komu. |
| [FR-CFG-001](FR-CFG-001-sprava-api-klice.md) | Správa API klíče | PŘIJATO | 2026-11-12 | Zadání, ověření, změna a smazání API klíče bez zobrazení v logu. |
| [FR-CFG-002](FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md) | Nastavení providera/modelu a výchozí šablony | PŘIJATO | 2026-11-12 | Nastavení providera/modelu, výchozí šablony a výchozího jazyka přepisu (čeština). |
| [FR-CFG-003](FR-CFG-003-informovani-o-chybach-limitech-a-offline-stavu.md) | Informování o chybách, limitech a offline stavu | PŘIJATO | 2026-11-12 | Informování o chybě/limitu/kreditu/offline stavu a dalším kroku. |
| [FR-CFG-004](FR-CFG-004-sprava-seznamu-emailovych-adres.md) | Správa seznamu e-mailových adres příjemců | PŘIJATO | 2026-11-13 | Administrace seznamu e-mailových adres pro opakované odesílání. |
| [FR-DATA-001](FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md) | Mazání nahrávek, přepisů a výsledků | PŘIJATO | 2026-11-12 | Mazání jednotlivých artefaktů s potvrzením a respektem k závislostem. |
| [FR-DATA-002](FR-DATA-002-konfigurovatelna-retencni-politika.md) | Konfigurovatelná retenční politika | PŘIJATO | 2026-11-12 | Retenční politika je konfigurovatelná, výchozí nemaže rozpracované úlohy. |
| [FR-NOTE-001](FR-NOTE-001-rucni-poznamky-perem-prilozene-k-zaznamu.md) | Ruční poznámky perem přiložené k záznamu | PŘIJATO | 2026-11-12 | Ruční poznámky perem přiložené k záznamu/dokumentu (NEXT). |
| [FR-SYNC-001](FR-SYNC-001-vlastni-cloudova-synchronizace.md) | Vlastní cloudová synchronizace | PŘIJATO | 2026-11-12 | Vlastní cloudová synchronizace dat mezi zařízeními — WON'T (MVP). |

### Nefunkční požadavky (NFR)

| ID | Název | Stav | Změněno | Shrnutí |
|---|---|---|---|---|
| [NFR-UX-001](NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md) | Jednoduché UX pro netechnického uživatele | PŘIJATO | 2026-11-12 | Tři úrovně použití (běžné/rozšířené/administrační); hlavní průchody max. jednoduché. |
| [NFR-UX-002](NFR-UX-002-e-ink-optimalizace-ui.md) | E Ink optimalizace UI | PŘIJATO | 2026-11-12 | Zvětšitelný text a velké/jasné prvky pro běžné použití; administrace není nutná. |
| [NFR-UX-003](NFR-UX-003-pristupna-ceska-lokalizace.md) | Přístupná česká lokalizace | PŘIJATO | 2026-11-12 | Česká lokalizace včetně chyb a potvrzovacích dialogů. |
| [NFR-REL-001](NFR-REL-001-zachovani-dat-pri-selhani-ai-volani.md) | Zachování dat při selhání AI volání | PŘIJATO | 2026-11-12 | Selhání AI volání nesmí smazat audio ani potvrzené editace. |
| [NFR-REL-002](NFR-REL-002-perzistence-stavu-workflow.md) | Perzistence stavu workflow | PŘIJATO | 2026-11-12 | Stav workflow se perzistuje pro bezpečnou obnovu po pádu. |
| [NFR-REL-003](NFR-REL-003-omezene-opakovani-sitovych-chyb.md) | Omezené opakování síťových chyb | PŘIJATO | 2026-11-12 | Omezený backoff; ochrana před vícenásobným placeným voláním. |
| [NFR-PERF-001](NFR-PERF-001-limity-delky-velikosti-nahravky.md) | Limity délky/velikosti nahrávky | PŘIJATO | 2026-11-12 | Max. délka/velikost dle limitů STT API a paměti (OVĚŘIT testem). |
| [NFR-PERF-002](NFR-PERF-002-responzivni-ui-behem-zpracovani.md) | Responzivní UI během zpracování | PŘIJATO | 2026-11-12 | UI zůstává responzivní, síťové volání neblokuje UI vlákno. |
| [NFR-SEC-001](NFR-SEC-001-https-a-validace-certifikatu.md) | HTTPS a validace certifikátu | PŘIJATO | 2026-11-12 | Komunikace s API pouze přes HTTPS s validací certifikátu. |
| [NFR-SEC-002](NFR-SEC-002-ochrana-api-klicu.md) | Ochrana API klíčů | PŘIJATO | 2026-11-12 | API klíče nejsou v APK/repozitáři/zálohách; chráněny Keystore. |
| [NFR-SEC-003](NFR-SEC-003-dpia-pred-zpracovanim-osobnich-zdravotnich-udaju.md) | DPIA před zpracováním osobních/zdravotních údajů | PŘIJATO | 2026-11-12 | DPIA/právní posouzení před zpracováním citlivých dat. |
| [NFR-SEC-004](NFR-SEC-004-soukrome-uloziste-a-ochrana-dat-v-klidu.md) | Soukromé úložiště a ochrana dat v klidu | PŘIJATO | 2026-11-12 | Soukromé úložiště, šifrování v klidu, omezení systémových záloh. |
| [NFR-SEC-005](NFR-SEC-005-rozpoctove-limity-api.md) | Rozpočtové limity API | PŘIJATO | 2026-11-12 | Vlastní evidence čerpání, zobrazení na vyžádání, nastavitelný měsíční limit. |
| [NFR-MAINT-001](NFR-MAINT-001-oddeleni-vrstev-architektury.md) | Oddělení vrstev architektury | PŘIJATO | 2026-11-12 | Oddělení UI/workflow/domény/úložiště/provider adaptérů. |
| [NFR-MAINT-002](NFR-MAINT-002-verzovani-sablon-a-migrace-dat.md) | Verzování šablon a migrace dat | PŘIJATO | 2026-11-12 | Verzování šablon/schémat a reprodukovatelné migrace dat. |
| [NFR-OBS-001](NFR-OBS-001-bezpecna-diagnostika-bez-citliveho-obsahu.md) | Bezpečná diagnostika bez citlivého obsahu | PŘIJATO | 2026-11-12 | Diagnostika bez obsahu audia/přepisu/dokumentu v běžných logách. |
| [NFR-PORT-001](NFR-PORT-001-prenositelnost-na-standardni-android.md) | Přenositelnost na standardní Android | PŘIJATO | 2026-11-12 | Funkčnost na běžném Androidu bez proprietárních BOOX API. |
| [NFR-QUAL-001](NFR-QUAL-001-vecna-kvalita-stt-a-extrakce-poli.md) | Věcná kvalita STT a extrakce polí | PŘIJATO | 2026-11-12 | Věcný test kvality STT a extrakce; JSON-validita nestačí. |
| [NFR-QUAL-002](NFR-QUAL-002-dokument-oznacen-jako-navrh-k-lidske-kontrole.md) | Dokument označen jako návrh k lidské kontrole | PŘIJATO | 2026-11-12 | Dokument je v UI označen jako návrh, ne autoritativní záznam. |
