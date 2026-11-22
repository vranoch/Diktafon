# Chat-extract — Import: dialog nejde zavřít, přepnutí na ACTION_GET_CONTENT, filtr jako volba

**Datum:** 2026-11-22

## Kontext

Po předchozí opravě importu (viz [chat-extract z téhož dne](2026-11-22-import-vice-souboru-prijeti-odmitnuti-prepisu.md))
uživatel nahlásil navazující problém: systémový dialog výběru souboru při importu nejde nijak
zavřít/zrušit a vrátit se do aplikace — typicky když hledaný soubor v procházeném úložišti není.
Uživatel se ptal, jestli lze dialogu "vnutit" tlačítka OK/Cancel.

Prvním pokusem o diagnózu (mylným) bylo podezření na přílišný MIME filtr (`audio/*`) — po
upřesnění uživatelem vyšlo najevo, že filtr soubory nacházel správně a problém byl čistě v
nemožnosti dialog zavřít, když hledaný soubor na dané lokaci chyběl.

## Rozhodnutí

1. **Nelze vnutit UI do cizí aplikace** — systémový picker je obrazovka úplně jiné aplikace
   (na BOOX/Onyx zařízeních pravděpodobně vlastní "Soubory" aplikace výrobce, ne Googlí
   DocumentsUI), do které appka nemůže žádným způsobem kreslit ani zasahovat. To je tvrdá hranice
   Androidu.
2. Místo toho přepnut typ systémového požadavku z `ActivityResultContracts.OpenMultipleDocuments()`
   (`ACTION_OPEN_DOCUMENT`) na `ActivityResultContracts.GetMultipleContents()`
   (`ACTION_GET_CONTENT`) — ten nejdřív projde systémovým výběrem aplikace ("chooser"), který má
   vždy tlačítko zpět/zavřít vykreslené samotným OS, ne cílovou aplikací. `ACTION_OPEN_DOCUMENT`
   jde rovnou do jednoho konkrétního SAF poskytovatele, jehož vlastní obrazovka na některých
   zařízeních může tuto cestu ven postrádat. Appka soubor čte a kopíruje hned, nepotřebuje tedy
   trvalé (persistable) oprávnění k URI, které `ACTION_OPEN_DOCUMENT` navíc nabízí — rozdíl mezi
   oběma intenty v tomto ohledu není relevantní.
3. Filtr souborů (`audio/*` vs. `*/*`) zůstal volný (`*/*`) jako výchozí, ale přidána možnost
   zvolit v Administraci → Připojení, zda picker nabízí jen audio soubory, nebo všechny — uživatel
   to sám navrhl jako doplněk k předchozí (ukázalo se mylné) diagnóze filtru. Uloženo jako nové
   pole `ProviderConfig.importAudioOnlyFilter` (výchozí `false`).

## Vedlejší zjištění — rate limit Maven Central při testování

Při ověřování těchto změn (containerový build/test) narazil Robolectric opakovaně na
`HTTP 429` z `repo1.maven.org` při stahování `android-all-instrumented` JARu — nesouviselo to s
kódem, ale s tím, že Robolectric kešuje tento JAR mimo `diktafon-gradle-cache` (do `~/.m2`), takže
se při každém `--rm` běhu kontejneru stahoval znovu a znovu, až narazil na rate limit. Přidán
čtvrtý Docker volume (`diktafon-robolectric-cache:/root/.m2`), viz [CLAUDE.md](../../CLAUDE.md).
I po 20 minutách čekání rate limit přímým `curl`em na danou URL stále vracel 429 (ověřeno), takže
finální verze byla commitnuta po úspěšném kompilačním ověření (`assembleDebug` +
`compileDebugUnitTestKotlin`/`compileReleaseUnitTestKotlin`, včetně Room/Hilt anotací) bez
čerstvého plného běhu testovací sady — ten je potřeba doběhnout, až rate limit odezní.

## Zdroje

- Tato konverzace, 2026-11-22.
