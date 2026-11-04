# GAME DESIGN DOCUMENT (GDD) & PRODUKTSPECIFIKATION

**Projekt:** Movie Discovery (Arbetstitel: CineCascade / MovieTrail)
**Dokumentets syfte:** Komplett specifikation och överlämningsunderlag. Dokumentet täcker vision, spelmekanik, teknisk arkitektur, nuvarande implementation och framtida planer.

---

## 1. Sammanfattning & Vision (Executive Summary)

### 1.1 High Concept & Pitch
CineCascade transformerar den passiva och ofta tröttsamma processen att "leta film en fredagskväll" till ett kinetiskt, beroendeframkallande och spelifierat utforskningsäventyr (*an infinite rabbit hole of entertainment*).

Genom ett vertikalt kedjat kaskadsystem av horisontella karuseller rör sig spelaren djupare och djupare in i ett visuellt filmträd. Varje val, varje fokusering och varje snurr i en karusell förgrenar omedelbart universumet under spelaren med nya rekommendationer – drivna av en hybridmotor bestående av en 8-dimensionell lokal likhetsalgoritm, realtidsdata från OMDb och generativ intelligens via Google Gemini.

### 1.2 Kärnfilosofi & "Game Feel"
- **Zero Dead Ends (Noll återvändsgränder):** Det ska vara omöjligt att fastna. Varje film öppnar alltid en ny dörr.
- **Kinetisk Taktilitet (Juicy Feedback):** Allt reagerar med fysisk fjädrande rörelse (spring physics), haptisk återkoppling (haptic bumps) och visuella förstoringar när ett kort når blickfånget.
- **Reaktiv Förgrening (Real-time Cascade):** Spelaren behåller alltid kontexten: du ser var du kom ifrån (Slot 1), var du är (Slot 2), och vad nästa steg blir (Slot 3).
- **Resan är Målet:** Att utforska och hitta dolda film-pärlor är lika engagerande som att faktiskt titta på filmen.

## 2. Kärnmekanik & Spelloop (Core Gameplay Loop)

```
        ┌────────────────────────────────────────────────────────┐
        │  [Level 0] TEMAVAL / VIBE (t.ex. "Space Exploration")  │
        └───────────────────────────┬────────────────────────────┘
                                    │
                            Kaskad (Swipe UP / Klick)
                                    ▼
        ┌────────────────────────────────────────────────────────┐
        │  [Level 1] TITELNIVÅ 1 (t.ex. "Interstellar")          │
        └───────────────────────────┬────────────────────────────┘
                                    │
       Horisontellt snurr i Slot 2 ─┼─► Driver Slot 3 i realtid
                                    │
                            Kaskad (Swipe UP / Klick)
                                    ▼
        ┌────────────────────────────────────────────────────────┐
        │  [Level 2] REKOMMENDERADE TITLAR (t.ex. "Gravity")     │
        └───────────────────────────┬────────────────────────────┘
                                    │
                                    ▼  (Oändlig förgrening N+1...)
```

### 2.1 Loopen i detalj
- **Startpunkt (Level 0 – The Vibe / Temaraden):** Spelaren möts av karaktäristiska "Theme Cards" med emojis, färgglada toningar och stämningstaggar (t.ex. Space Exploration, Nordic Noir, Cyberpunk Grid, Whodunit Mystery, Epic Fantasy, Post-Apocalyptic Survival, Psychological Thriller).
- **Horisontell Navigering & Center-Fokus:** När spelaren drar i karusellen snäpper korten till centrum. Det kort som hamnar i den optiska mitten expanderar i skala (från 0.94x till 1.05x) och får en lysande ram. Haptisk feedback avfyras subtilt vid passering av varje kort.
- **Realtidsprojektion (Slot 2 styr Slot 3):** Medan spelaren rullar karusellen i mitten (Slot 2), uppdateras den underliggande karusellen (Slot 3) omedelbart i realtid. Rullar du till *Interstellar* fylls Slot 3 direkt med *Dune*, *Gravity*, *The Expanse*. Rullar du ett steg till *The Martian* uppdateras Slot 3 i flykten. Det minimalistiska IMDb-kortet längst ner uppdateras synkront med titel, betyg, regissör och synopsis för det centrerade objektet.
- **Kaskadhopp / Avancering (Gå djupare i trädet):** Genom att trycka på det fokuserade kortet (eller göra en distinkt vertikal swipe uppåt) "faller" spelaren ner en nivå i trädet. Den tidigare aktiva nivån glider upp och krymper till en kondenserad historikrad (Slot 1). Slot 3 expanderar och blir den nya aktiva nivån (Slot 2). En helt ny generation av rekommendationer skapas under (Slot 3).
- **Bakåtnavigering & Grenbyte (Rewind & Branch):** Genom att swipa nedåt eller trycka på Slot 1 längst upp backar spelaren upp i trädet. Om spelaren väljer en annan film på en tidigare nivå, kapas automatiskt alla djupare nivåer av och ett nytt sidospår öppnas.
- **Globala Filterridåer (Curtain Menus):** Två ovala reglage längst upp ("Movies/Shows" och "Genres") fälls mjukt ner som gardiner. Multi-select gör det möjligt att kombinera t.ex. Sci-Fi + Mystery eller enbart Series. När ett filter aktiveras byggs hela det vertikala trädet om dynamiskt i minnet utan att bryta spelarens position.

## 3. Gränssnittsarkitektur & Skärmlayout

Gränssnittet är strukturerat i fem moduler anpassade för handhållet enhandsbruk:

```
┌────────────────────────────────────────────────────────┐
│  [TOPPBAR]  (Movies/Shows)  (Genres)  [Filter Reset]   │
├────────────────────────────────────────────────────────┤
│  [SLOT 1]  Föregående nivå (Kondenserad brödsmula)     │
│            Visar vart vi kom ifrån. Klickbar för att   │
│            backa. Halvt nedtonad med monokrom look.    │
├────────────────────────────────────────────────────────┤
│  [SLOT 2]  AKTIV KARUSELL (Optiskt centrerad, snäpp,   │
│            scale 1.05x, fjädrande fysik, testTag)      │
├────────────────────────────────────────────────────────┤
│  [SLOT 3]  KOMMANDE NIVÅ (Minikort baserade på         │
│            centrerad film i Slot 2. Uppdateras live)   │
├────────────────────────────────────────────────────────┤
│  [BOTTEN]  IMDb STATUSPANEL / THEME DETAIL CARD        │
│            Mörkt, minimalistiskt, betyg, år, bio, tags │
└────────────────────────────────────────────────────────┘
```

- **Slot 1 (Previous Level Banner):** Höjd ~95dp. Ligger som en dämpad referens och brödsmula. Innehåller uppåt-pil, namn på föräldravalet och miniatyrer i gråskala med vald nod markerad.
- **Slot 2 (Active Main Carousel):** Höjd ~230dp. Kortens bredd: 135dp. Content padding beräknas exakt: `(screenWidth - 135.dp) / 2` för att garantera att det valda kortet vilar perfekt i skärmens horisontella mittpunkt.
- **Slot 3 (Preview Recommendations Carousel):** Höjd ~115dp. Mindre kort (bredd 85dp) med kompakt betygstagg och omslag.
- **Bottenkort (Minimalist IMDb Card):** Höjd ~125dp. Elegant monokrom bakgrund (#131317), diskret gult betyg, IMDb-badge, regissör/skådespelare, år och tvåradig synopsis.

## 4. Teknisk Arkitektur & Dataflöde

### 4.1 Datamodeller
- **ThemeVibe:** ID, namn, stämningstagg, emoji, temafärger och gradienter.
- **MediaItem:** ID, titel, typ (MOVIE eller SERIES), årtal, betyg (0.0-10.0), tema-ID, genrer, regissör, rollista, synopsis, bild-URL, färgprofil.
- **CarouselNode:**
  - `parentItem`: Filmen som genererade denna rad.
  - `parentTheme`: Temat som genererade denna rad (om nivå 0).
  - `selectedItem`: Det aktuella fokuserade objektet i raden.
  - `selectedTheme`: Det fokuserade temat (om nivå 0).
  - `isThemeRow`: Boolesk flagga.
  - `themes`: Lista av visade teman.
  - `items`: Lista av rekommenderade titlar i raden.
- **DiscoveryUiState:**
  - `levels`: Dynamisk lista av CarouselNode (representerar kaskadkedjan från rot till botten).
  - `activeLevelIndex`: Håller koll på vilken nivå som för tillfället visas i Slot 2.
  - `selectedMediaTypes`: Set av MediaType (multival).
  - `selectedGenres`: Set av String (multival).
  - `isFormatCurtainOpen` / `isGenreCurtainOpen`: Tillstånd för rullgardinsmenyerna.

### 4.2 Rekommendationsmotor (8-dimensionell hybridalgoritm)
När ett objekt väljs eller fokuseras i en karusell, rankar `MovieRepository.findRecommendations()` hela katalogen utifrån följande poängsystem:

1. **Regissör / Skapare:** +15 poäng (om samma regissör).
2. **Delade Huvudrollsinnehavare:** +10 poäng per delad skådespelare.
3. **Kreativt Tema / Vibe:** +8 poäng (om inom samma kuraterade sfär).
4. **Delade Genrer:** +6 poäng för primär genre, +3 poäng för ytterligare matchningar.
5. **Titellikhet & Nyckelord:** +5 poäng per matchande semantiskt ord.
6. **Tidsmässig Närhet:** +4 poäng (samma år), +2 poäng (inom 2 år), +1 poäng (inom 5 år).
7. **Kvalitets-/Betygsnärhet:** +3 poäng (inom 0.5 på IMDb), +1 poäng (inom 1.0).
8. **Formatmatchning:** +2 poäng (film med film, serie med serie).

### 4.3 AI & Generativ Expansion (Google Gemini)
- **Modell:** gemini-3.5-flash via Gemini REST API.
- **Debounce-mekanik:** 450 ms fördröjning vid användarens rullande för att förhindra onödiga API-anrop.
- **Roll:** När användaren landar på en film skickas en prompt med kontext (titel, årtal, genrer, tema, filter). Gemini genererar en strukturerad JSON med liknande titlar och exakta IMDb-ID:n som automatiskt registreras i katalogen och skickas till OMDb för hämtning av affischer.

### 4.4 Metadatapipeline (OMDb API)
- Bakgrundssynkronisering via coroutines och OkHttp.
- Hämtar högupplösta officiella filmaffischer, exakt IMDb-betyg, uppdaterad plot, fullständig rollista och regissör.
- Bilderna cachas i minnet och på disk med Coil.

## 5. Nuvarande Status (Vad som är byggt & Verifierat)

| Komponent / Funktion | Status | Detaljer |
|---|---|---|
| Optisk Center-Snäppning | KLAR | Matematisk formel med `horizontalPaddingPx = (screenWidth - 135.dp) / 2` och scroll-offset 0. |
| Realtidskaskad (Slot 2 -> Slot 3) | KLAR | `snapshotFlow` med `rememberUpdatedState` uppdaterar Slot 3 live utan lagg eller omstartade effekter. |
| Vertikal Avancering & Revert | KLAR | Rader med animerad position (fjäderfysik) — karusellerna glider upp/ner och ändrar storlek mellan slottarna. |
| Vertikal Geststyrning | KLAR | Drag-gester med tröskelvärde på 150f (Swipe UP avancerar, Swipe DOWN backar). Raderna följer fingret live. |
| Filtergardiner (Curtain Menus) | KLAR | Fälls ner mjukt från toppen. Stödjer multi-select för både format och genrer. |
| Minimalistiskt IMDb-kort | KLAR | Visar data för det fokuserade objektet i dämpad monokrom palett. |
| Katalog & Likhetsmotor | KLAR | Inbyggd katalog med teman, IMDb-mappning och 8-dimensionell rankning. |
| Gemini AI Integration | KLAR | Tjänst med gemini-3.5-flash och strukturerad JSON-avkodning via Moshi. |
| OMDb Berikning | KLAR | Automatisk asynkron hämtning av affischer och metadata. |

## 6. Framtida Vision & Backlog (Game & Feature Roadmap)

### 6.1 Spellägen & Utmaningar ("The Movie Quest")
- **"Six Degrees of Cinema" (Filmstigen):** Spelaren ges en startfilm (t.ex. *The Matrix*) och en målfilm (t.ex. *Grand Budapest Hotel*). Mål: Navigera mellan filmerna enbart genom att välja rekommendationer i karusellerna på så få kaskadhopp som möjligt. Par-system: "Klarade du det på 4 hopp? Brons. 3 hopp? Silver. 2 hopp? Guld."
- **"Blind Vibe Challenge":** Titlar och affischer är dolda bakom mystiska citat, stämningsbeskrivningar eller ljudklipp. Spelaren navigerar baserat på ren "känsla" tills målet nås och hela kedjan avslöjas.
- **"Daily Discovery Run":** En daglig kuraterad utmaning där spelare världen över får samma startkarusell och tävlar om att hitta den mest unika pärlan.

### 6.2 Socialt Läge & Matchning ("Couch Co-op / Date Night Match")
Två spelare ansluter sina enheter. Båda utforskar i sina egna kaskader från samma rot. När spelarnas förgrenade stigar korsar samma film utlöser appen en "IT'S A MATCH!"-animation med konfetti och direktlänk till streaming.

### 6.3 "Catch & Stash" (Watchlist & Filmfångst)
- Snabb "Swipe Right" eller långtryck på ett kort fångar filmen till spelarens personliga samling.
- Skapa tematiska samlingar ("Mina Cyberpunk-nätter", "Middagsmys", "Hjärngympa").
- Erfarenhetspoäng (XP) och badges: "Sci-Fi Explorer Rank 5", "Nordic Noir Detective".

### 6.4 Streaming-tillgänglighet (Watch Providers)
- Integration med JustWatch API eller TMDB Watch Providers.
- Tydliga, minimalistiska ikoner under IMDb-kortet som visar var filmen finns tillgänglig (Netflix, HBO Max, Disney+, SVT Play etc. baserat på region).

### 6.5 Audiovisuell Kinetik & Ljuddesign
- **Projektor-klick:** Subtilt mekaniskt tickande ljud vid snäppning i karusellen.
- **Kaskadswoosh:** Mörkt, djupt filmatiskt swoosh-ljud vid kaskadhopp nedåt i nivå.
- **Dynamisk Bakgrund:** Bakgrundsfärgen tonas mycket subtilt till filmens dominerande färg när den stannar i mitten.

### 6.6 Inline Mikrotrailers
Vid stillastående i mer än 2 sekunder på ett kort tonar affischen över till en ljudlös, textad miniloop/trailer.

## 7. Överlämningsguide för Utvecklingsteamet

### 7.1 Förutsättningar
- **Språk & Ramverk:** Kotlin 1.9+ / Jetpack Compose (Material 3).
- **Arkitektur:** MVVM med `MovieDiscoveryViewModel` och Kotlin `StateFlow`.
- **Nätverk & Data:** OkHttp3, Moshi, Coil (`SubcomposeAsyncImage`).
- **Hemligheter:** `GEMINI_API_KEY` och `OMDB_API_KEY` injiceras via `BuildConfig`.

### 7.2 Kritiska Implementationstekniska Regler
- **Karusellcentrering:** Ändra inte formeln `horizontalPaddingPx = (screenWidth - 135.dp) / 2` och scroll-offset 0. Om denna ändras blir centreringen och `snapshotFlow`-beräkningen asymmetrisk.
- **SnapshotFlow & LaunchedEffect:** För att undvika omritningsloopar vid scrollning, använd alltid `rememberUpdatedState` för listan och nivån inuti `LaunchedEffect(listState, targetIndex)`.
- **Nivåavklippning (Tree Truncation):** När användaren väljer ett kort på en tidigare nivå kapas djupare nivåer av med `levels.subList(0, levelIndex + 1)` innan nästa rekommendationsnod läggs till.
