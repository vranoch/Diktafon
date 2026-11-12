# AGENTS.md — Diktafon

Toto je **jediný primární zdroj pravdy** o tom, jak se v tomto repozitáři pracuje — platí pro
všechny AI nástroje (Claude Code, Codex, Copilot, Cursor, …) i pro lidi. Nástroj-specifické
soubory (`CLAUDE.md`, `.codex/...`, atd.) smí obsahovat jen odkaz sem + příkazy specifické pro
daný nástroj. Žádná provozní znalost (konvence, workflow, pravidla) se nesmí duplikovat jinde —
pokud se najde jen v nástroj-specifickém souboru, je to chyba a patří přesunout sem.

> **Stav projektu:** založení repozitáře, technologie a architektura zatím nejsou zvoleny.
> Sekce níže jsou kostra k doplnění — neobsahují ještě žádné rozhodnutí o stacku.

## 1. O projektu

- **Diktafon** — TBD: stručný popis účelu aplikace, až bude znám.
- **Tech stack:** TBD.
- **Jazyk kódu / dokumentace:** TBD (doporučení: kód anglicky, dokumentace v jazyce týmu).

## 2. Princip agnosticity nástrojů

- `AGENTS.md` (tento soubor) = jediné místo s provozní znalostí o projektu a pravidly práce.
- `CLAUDE.md`, `.codex/`, případně další nástroj-specifické soubory smí obsahovat pouze:
  odkaz na tento soubor, build/run/test příkazy specifické pro daný nástroj, a odkazy do
  `ai/skills/`.
- Nové "jak na to" postupy patří do [`ai/skills/`](ai/skills/), ne do nástroj-specifických
  souborů.

## 3. Primární zdroje pravdy (v pořadí priority)

1. Tento soubor (`AGENTS.md`)
2. [`documentation/decisions/`](documentation/decisions/) — architektonická rozhodnutí (ADR)
3. [`documentation/requirements/`](documentation/requirements/) a
   [`documentation/use-cases/`](documentation/use-cases/) — požadavky a use-cases/procesy
4. [`documentation/modules/`](documentation/modules/) — specifikace modulů
5. [`ai/skills/`](ai/skills/), [`ai/workflows/`](ai/workflows/) a
   [`documentation/guides/`](documentation/guides/) — postupy, workflow a návody
6. Kód a jeho testy

## 4. Mapa projektu

| Složka / soubor | Co tam je |
|---|---|
| `AGENTS.md` | Tento soubor — pravidla a konvence pro AI i lidi |
| `CLAUDE.md` | Tenký wrapper pro Claude Code (odkaz + build příkazy) |
| `README.md` | Lidsky čitelný přehled projektu a quickstart |
| `ai/skills/` | Krátké, nástroj-agnostické návody "jak na to" |
| `ai/workflows/` | Opakované vícekrokové postupy (checklisty) |
| `documentation/requirements/` | Funkční a nefunkční požadavky (FR/NFR) |
| `documentation/decisions/` | Architektonická rozhodnutí (ADR) |
| `documentation/use-cases/` | Use-cases a procesy (UC) |
| `documentation/modules/` | Specifikace modulů (coverage tabulka požadavků) |
| `documentation/guides/` | Živé referenční návody (bez lifecyclu, viz §5.6) |
| `documentation/source-materials/` | Surové vstupy: originály, poznámky, chat-extracty (read-only) |
| `_workspace/` | Dočasný scratch prostor — `inbox/`, `processing/`, `archive/`, `rules/` (viz README uvnitř) |

Tabulka se doplňuje s tím, jak přibývají nové složky (kód, testy, infrastruktura, ...).

## 5. Formální artefakty — metadata, životní cyklus, zdroje

"Formální artefakt" = cokoliv trvalého v `documentation/` (požadavek, ADR, use-case/proces,
specifikace modulu, ...), na rozdíl od obsahu `_workspace/`, `documentation/guides/` (§5.6)
nebo kódu samotného. Rozdělení kategorií:

- [`documentation/requirements/`](documentation/requirements/) — funkční/nefunkční požadavky
- [`documentation/decisions/`](documentation/decisions/) — architektonická rozhodnutí (ADR)
- [`documentation/use-cases/`](documentation/use-cases/) — use-cases a procesy
- [`documentation/modules/`](documentation/modules/) — specifikace modulů

### 5.1 Povinná metadata

Každý formální artefakt má na začátku souboru:

```
**Stav:** NAVRŽENO | PŘIJATO | SUPERSEDED by ADR-NNN | ZAMÍTNUTO
**Vytvořeno:** YYYY-MM-DD
**Změněno:** YYYY-MM-DD
**Zdroje:** (odkazy na chat-extract, diskuzi, issue, jiný artefakt)
```

Pole `Zdroje` je páteř dohledatelnosti — díky němu lze zpětně dohledat, proč artefakt vznikl.

### 5.2 Životní cyklus artefaktu

```
WORKING  --(review + schválení)-->  APPROVED
APPROVED --(přišel nový vstup/změna)-->  WORKING
WORKING / APPROVED --(rozhodnutí zrušit)--> CANCELLED   (terminální stav)
```

- `WORKING` = rozpracováno, může se měnit přímo.
- `APPROVED` = schváleno; úprava přímo v artefaktu je **zakázaná** — pokud přijde nový vstup,
  nejdřív se oprav zdroj (požadavek, diskuze), pak se vygeneruje delta/nová verze artefaktu a
  stav se vrátí na `WORKING` → znovu schválit.
- `CANCELLED` je terminální — artefakt se nemaže, zůstává jako historický záznam.

### 5.3 Index per kategorie + pravidlo "update before commit"

Každá kategorie formálních artefaktů i `documentation/guides/` (viz §5.6) má vlastní
`README.md` s indexem
(tabulka ID | Název | Stav | Datum | Shrnutí | odkaz). **Index se musí aktualizovat ve stejném
commitu**, který přidává nebo mění artefakt dané kategorie — nikdy ne později "až bude čas".

### 5.4 Konvence křížových odkazů

Každá zmínka jiného souboru/složky repozitáře v dokumentaci musí být klikatelný relativní
markdown odkaz (`[popis](cesta/k/souboru.md)`), nikdy jen text nebo backtick bez odkazu. Usnadňuje
to navigaci lidem i agentům a umožňuje to automatizovanou kontrolu platnosti odkazů.

### 5.5 Chat-extract konvence

Pokud rozhodnutí nebo požadavek vzniká v delší diskuzi (s uživatelem, v chatu s AI), před
označením souvisejícího artefaktu jako `APPROVED` se vytvoří **datovaný chat-extract** —
stručný záznam diskuze a závěru do [`documentation/source-materials/chat-extracts/`](documentation/source-materials/chat-extracts/),
soubor pojmenovaný `YYYY-MM-DD-kratky-popis.md`. Artefakt pak v poli `Zdroje` odkazuje na tento
extract. Chat-extract se nikdy nemaže ani needituje — je to historický záznam.

### 5.6 Guides — živé dokumenty bez lifecyclu

[`documentation/guides/`](documentation/guides/) jsou referenční návody (onboarding, provoz,
troubleshooting apod.), které se na rozdíl od ostatních kategorií **neschvalují** přes
`WORKING/APPROVED` — jsou to živé dokumenty, průběžně aktualizované tak, jak se mění realita,
kterou popisují. Mají jen metadata `Vytvořeno`/`Změněno`/`Zdroje` (bez `Stav`) a stále platí
pravidlo indexu (§5.3) a křížových odkazů (§5.4). Podrobná konvence a šablona viz
[`documentation/guides/README.md`](documentation/guides/README.md).

## 6. Workflow pro agenty

- Před větší změnou zkontroluj, jestli existuje relevantní ADR v `documentation/decisions/`
  nebo skill v `ai/skills/`.
- Pokud během práce vznikne nové rozhodnutí o architektuře/designu, zapiš ho jako ADR
  (viz šablona a index v `documentation/decisions/`).
- Nejisté věci (předpoklady, otevřené otázky) označuj přímo v textu tagem `ASSUMPTION:` nebo
  `TODO:`, ať jsou dohledatelné a není nutné je domýšlet.
- Dočasné/rozpracované soubory patří do `_workspace/`, ne do trvalé struktury repozitáře.
- Při přechodu artefaktu na `APPROVED` po delší diskuzi nezapomeň založit chat-extract
  (viz 5.5) a odkázat na něj v poli `Zdroje`.

## 7. Guardrails

Bez výslovného schválení uživatele agent **nesmí**:

- force-push, `git reset --hard`, mazat větve
- přidávat nové závislosti/balíčky
- měnit již schválené ADR dokumenty (vytvoří se nový ADR, který ten starý nahrazuje)
- vymýšlet architekturu nebo požadavky, které nejsou nikde podložené — takové kroky je třeba
  označit jako `ASSUMPTION` a nechat potvrdit

## 8. Definition of Done

- [ ] Kód odpovídá konvencím zvoleným pro daný stack (viz budoucí sekce "Konvence kódu")
- [ ] Relevantní ADR je založen/aktualizován, pokud změna ovlivňuje architekturu
- [ ] Index dané kategorie artefaktů (viz 5.3) je aktualizovaný ve stejném commitu
- [ ] Křížové odkazy na jiné soubory repozitáře jsou klikatelné markdown odkazy (viz 5.4)
- [ ] Dočasné soubory z `_workspace/` jsou uklizeny nebo přesunuty na trvalé místo
- [ ] Nic citlivého (klíče, tokeny, osobní údaje) není commitnuto
