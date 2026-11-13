@AGENTS.md

## Claude Code — specifické příkazy

Build/test běží jen v kontejneru (viz [ADR-011](documentation/decisions/ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)), nikdy na hostiteli přímo:

```bash
docker build -t diktafon-dev -f .devcontainer/Dockerfile .devcontainer
docker run --rm -v "$(pwd)":/workspace diktafon-dev sh -c "cd /workspace && ./gradlew test"
```

## Klíčové soubory pro orientaci

- [AGENTS.md](AGENTS.md) — pravidla a konvence (jediný zdroj pravdy)
- [README.md](README.md) — přehled projektu
- [ai/skills/](ai/skills/) — návody "jak na to"
- [documentation/decisions/](documentation/decisions/) — architektonická rozhodnutí

## Konvence

- Commit zprávy: TBD (doporučení: anglicky, konvenční formát)
- Kód: TBD jazyk/styl podle zvoleného stacku
