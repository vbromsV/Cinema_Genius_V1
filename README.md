# CineCascade 🎬

Spelifierad filmupptäckt för Android (Kotlin + Jetpack Compose). Se [GDD.md](GDD.md) för hela specifikationen.

Du rör dig nedåt i ett oändligt filmträd. **Slot 1** visar var du kom ifrån, **Slot 2** är karusellen du snurrar på och **Slot 3** visar live vad nästa nivå blir. Swipea upp (eller tryck på det centrerade kortet) för att gå djupare, och swipea ner (eller tryck på Slot 1) för att backa.

## Kaskadanimationen

Varje rad i kaskaden har en animerad *position* (−1 = Slot 1, 0 = Slot 2, 1 = Slot 3, ±2 = utanför skärmen). Allt visuellt interpoleras från positionen: y-läge, radhöjd, kortbredd (44 → 135 → 85 dp), hörnradie, färgmättnad (gråskala i Slot 1), opacitet, titel-overlay och betygstagg.

- **Framåt:** alla rader får målposition −1 samtidigt. Slot 3 glider upp och växer till Slot 2. Slot 2 krymper och tonas till gråskala i Slot 1. Slot 1 glider ut uppåt och en ny förhandsrad stiger in nedifrån.
- **Bakåt:** exakt omvänd rörelse. De djupare nivåerna sparas, så du kan swipea upp igen längs samma stig.
- **Under drag:** raderna följer fingret live, med gummiband när det inte går att gå längre. När du släpper över 150 px fortsätter rörelsen sömlöst från där fingret var.
- Rörelsen drivs av fjäderfysik med lite studs. Haptik ger ett tick per kort och en stöt vid kaskadhopp.

Centreringen följer GDD §7.2: content padding = `(skärmbredd − kortbredd) / 2` och scroll-offset 0. Eftersom kortbredden animeras räknas paddingen om varje bildruta, så det valda kortet håller sig i mitten medan raden ändrar storlek.

## Arkitektur

```
core/   Ren Kotlin/JVM (testbar utan Android)
  model/       ThemeVibe, MediaItem, DiscoveryFilters
  data/        Kuraterad katalog: 8 teman, ~120 titlar
  engine/      8-dimensionell rekommendationsmotor (+ AI-boost)
  cascade/     CarouselNode, CascadeTree, CascadeNavigator (avancera/backa/förgrena/bygga om)
  remote/      OmdbClient (OkHttp + Moshi), GeminiClient (strukturerad JSON)
  repository/  MovieRepository + diskcache för OMDb-svar
app/    Android-app (Compose, Material 3, MVVM)
  viewmodel/   MovieDiscoveryViewModel (StateFlow), DiscoveryUiState
  ui/          DiscoveryScreen, CascadeStack, CascadeRow, kort, IMDb-panel, filtergardiner
```

## API-nycklar

| Nyckel | Standard | Används till |
|---|---|---|
| `OMDB_API_KEY` | *(tom = bara inbyggd data)* | Affischer, IMDb-betyg, handling, rollista |
| `GEMINI_API_KEY` | *(tom = AI av)* | Generativ expansion av rekommendationer |
| `GEMINI_MODEL` | `gemini-3.5-flash` | Gemini-modell |

Lägg nycklarna i `local.properties` i projektroten. Filen committas inte:

```properties
OMDB_API_KEY=din-omdb-nyckel
GEMINI_API_KEY=din-gemini-nyckel
```

För APK:n som byggs i GitHub Actions lägger du in samma namn som repository secrets (*Settings → Secrets and variables → Actions*).

OMDb anropas via **https** (`https://www.omdbapi.com/?apikey=…&t=…&y=…`), eftersom Android blockerar okrypterad http som standard. Titlarna i katalogen slås upp på titel + år + typ. AI-förslag slås upp på IMDb-id. Svaren cachas på disk (`omdb-cache.json`), så varje titel hämtas bara en gång. Det spelar roll eftersom gratisnivån hos OMDb tillåter 1 000 anrop per dygn. Om `Poster` saknas används `img.omdbapi.com` som reserv.

## Bygga

Öppna projektet i Android Studio (Ladybug eller senare) och kör `app`. Eller från terminalen:

```bash
./gradlew :core:test
```

```bash
./gradlew :app:assembleDebug
```

GitHub Actions (`.github/workflows/android.yml`) kör testerna och bygger en debug-APK vid varje push. APK:n kan laddas ner under fliken *Actions* → körningen → *Artifacts*.
