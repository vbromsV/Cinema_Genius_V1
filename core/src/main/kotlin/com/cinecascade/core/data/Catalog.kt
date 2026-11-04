package com.cinecascade.core.data

import com.cinecascade.core.model.ColorProfile
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.model.ThemeVibe

/**
 * Built-in curated catalog. Ratings, cast and plots are seed values; OMDb replaces them with
 * live data (and adds posters) as soon as a title becomes visible in a carousel.
 *
 * Titles are looked up on OMDb by title + year + type, so no hand-typed IMDb ids are needed.
 */
object Catalog {

    val themes: List<ThemeVibe> = listOf(
        ThemeVibe(
            "space", "Space Exploration", "Cosmic awe", "🚀",
            "Wormholes, lonely orbits and the vast silence between the stars.",
            ColorProfile(0xFF1B2A6B, 0xFF7B4DFF), listOf("Sci-Fi", "Adventure"),
        ),
        ThemeVibe(
            "nordic", "Nordic Noir", "Bleak & brooding", "🌨️",
            "Grey skies, frozen crime scenes and detectives carrying old wounds.",
            ColorProfile(0xFF1E3A4C, 0xFF8FB3C7), listOf("Crime", "Mystery"),
        ),
        ThemeVibe(
            "cyberpunk", "Cyberpunk Grid", "Neon dystopia", "🌃",
            "Rain-slick neon, rogue AIs and hackers jacked into the system.",
            ColorProfile(0xFFB0145E, 0xFF00B8D4), listOf("Sci-Fi", "Action"),
        ),
        ThemeVibe(
            "whodunit", "Whodunit Mystery", "Clever & sinister", "🕵️",
            "A body, a mansion full of suspects and one detective who sees everything.",
            ColorProfile(0xFF5C3A1E, 0xFFD4A24C), listOf("Mystery", "Crime"),
        ),
        ThemeVibe(
            "fantasy", "Epic Fantasy", "Myth & legend", "🐉",
            "Quests, dragons and kingdoms on the brink.",
            ColorProfile(0xFF2E5E3A, 0xFFE0B84F), listOf("Fantasy", "Adventure"),
        ),
        ThemeVibe(
            "postapoc", "Post-Apocalyptic Survival", "Dust & grit", "☢️",
            "The world ended. Now comes the hard part.",
            ColorProfile(0xFF6B3A1E, 0xFFE07B39), listOf("Sci-Fi", "Drama"),
        ),
        ThemeVibe(
            "psych", "Psychological Thriller", "Mind games", "🧠",
            "Unreliable minds, twisted motives and twists you never saw coming.",
            ColorProfile(0xFF3A1E4C, 0xFFC0392B), listOf("Thriller", "Mystery"),
        ),
        ThemeVibe(
            "cozy", "Cozy Feel-Good", "Warm & witty", "☕",
            "Big hearts, small adventures and endings that make you smile.",
            ColorProfile(0xFFB5523B, 0xFFE8B86D), listOf("Comedy", "Romance"),
        ),
        ThemeVibe(
            "heist", "Heists & Cons", "Slick & clever", "💎",
            "Crews, plans and the con that nobody saw coming.",
            ColorProfile(0xFF6B5A1E, 0xFFE0C04F), listOf("Crime", "Thriller"),
        ),
        ThemeVibe(
            "horror", "Horror Night", "Lights off", "👻",
            "Slow dread, sudden screams and things in the dark.",
            ColorProfile(0xFF3B0D0D, 0xFFB71C1C), listOf("Horror", "Thriller"),
        ),
        ThemeVibe(
            "animated", "Animated Wonders", "Colourful & heartfelt", "🎨",
            "Hand-drawn dreams, pixel-perfect worlds and anime epics.",
            ColorProfile(0xFF1E5E7B, 0xFFFF8A65), listOf("Animation", "Adventure"),
        ),
        ThemeVibe(
            "true", "True Stories", "Based on reality", "📜",
            "Real people, real events, more gripping than fiction.",
            ColorProfile(0xFF3E3E4A, 0xFFB0A58C), listOf("Biography", "Drama"),
        ),
    )

    val items: List<MediaItem> = buildList {
        theme("space") {
            movie("Interstellar", 2014, 8.7, "Christopher Nolan", "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine", "Sci-Fi, Adventure, Drama", "wormhole time father blackhole", "Explorers travel through a wormhole in a desperate bid to secure humanity's future.")
            movie("Gravity", 2013, 7.7, "Alfonso Cuarón", "Sandra Bullock, George Clooney", "Sci-Fi, Thriller, Drama", "orbit survival debris astronaut", "Two astronauts fight to survive after debris destroys their shuttle.")
            movie("The Martian", 2015, 8.0, "Ridley Scott", "Matt Damon, Jessica Chastain, Jeff Daniels", "Sci-Fi, Adventure, Drama", "mars survival stranded astronaut", "Stranded on Mars, an astronaut must science his way to survival.")
            movie("Arrival", 2016, 7.9, "Denis Villeneuve", "Amy Adams, Jeremy Renner, Forest Whitaker", "Sci-Fi, Drama, Mystery", "aliens language time contact", "A linguist races to decode the language of mysterious visitors.")
            movie("Dune", 2021, 8.0, "Denis Villeneuve", "Timothée Chalamet, Rebecca Ferguson, Zendaya, Oscar Isaac", "Sci-Fi, Adventure, Drama", "desert empire prophecy spice", "A noble heir is thrust into a war for the galaxy's most precious resource.")
            movie("Dune: Part Two", 2024, 8.5, "Denis Villeneuve", "Timothée Chalamet, Zendaya, Rebecca Ferguson, Austin Butler", "Sci-Fi, Adventure, Action", "desert empire prophecy spice", "Paul Atreides unites with the Fremen on a path of revenge.")
            movie("2001: A Space Odyssey", 1968, 8.3, "Stanley Kubrick", "Keir Dullea, Gary Lockwood", "Sci-Fi, Adventure", "monolith evolution jupiter", "A mysterious monolith guides humanity's evolution toward Jupiter.")
            movie("Ad Astra", 2019, 6.5, "James Gray", "Brad Pitt, Tommy Lee Jones, Liv Tyler", "Sci-Fi, Drama, Adventure", "father neptune astronaut", "An astronaut journeys to the edge of the solar system to find his father.")
            movie("Moon", 2009, 7.8, "Duncan Jones", "Sam Rockwell, Dominique McElligott", "Sci-Fi, Drama, Mystery", "clone isolation lunar", "A lone worker on a lunar base nears the end of his contract—and the truth.")
            movie("Sunshine", 2007, 7.2, "Danny Boyle", "Cillian Murphy, Chris Evans, Rose Byrne, Michelle Yeoh", "Sci-Fi, Thriller", "mission dying star", "A crew carries a bomb to reignite the dying sun.")
            movie("First Man", 2018, 7.3, "Damien Chazelle", "Ryan Gosling, Claire Foy, Jason Clarke", "Drama, History, Biography", "moon astronaut apollo", "Neil Armstrong's long road to the Moon.")
            movie("Apollo 13", 1995, 7.7, "Ron Howard", "Tom Hanks, Kevin Bacon, Bill Paxton, Ed Harris", "Drama, Adventure, History", "moon astronaut apollo nasa", "A crippled spacecraft must somehow bring its crew home.")
            movie("Contact", 1997, 7.5, "Robert Zemeckis", "Jodie Foster, Matthew McConaughey", "Sci-Fi, Drama, Mystery", "signal aliens faith contact", "A scientist receives the first message from an alien civilisation.")
            movie("Alien", 1979, 8.5, "Ridley Scott", "Sigourney Weaver, Tom Skerritt, John Hurt", "Horror, Sci-Fi", "creature ship isolation", "The crew of a cargo ship is hunted by a deadly creature.")
            movie("Solaris", 1972, 8.0, "Andrei Tarkovsky", "Natalya Bondarchuk, Donatas Banionis", "Sci-Fi, Drama, Mystery", "ocean memory station", "A psychologist on a space station confronts the ghosts of his past.")
            series("The Expanse", 2015, 8.5, "Mark Fergus", "Steven Strait, Dominique Tipper, Wes Chatham, Shohreh Aghdashloo", "Sci-Fi, Drama, Mystery", "belt mars protomolecule", "A detective and a ship's crew uncover a conspiracy threatening the solar system.")
            series("For All Mankind", 2019, 8.1, "Ronald D. Moore", "Joel Kinnaman, Shantel VanSanten, Wrenn Schmidt", "Sci-Fi, Drama, History", "moon nasa alternate", "What if the space race never ended?")
        }
        theme("nordic") {
            series("The Bridge", 2011, 8.6, "Hans Rosenfeldt", "Sofia Helin, Kim Bodnia", "Crime, Drama, Mystery", "border detective øresund", "A body found on the Øresund bridge unites a Swedish and a Danish detective.")
            series("The Killing", 2007, 8.3, "Søren Sveistrup", "Sofie Gråbøl, Lars Mikkelsen", "Crime, Drama, Mystery", "detective copenhagen murder", "Day by day, one murder investigation in Copenhagen unravels.")
            series("The Chestnut Man", 2021, 7.6, "Søren Sveistrup", "Danica Curcic, Mikkel Boe Følsgaard", "Crime, Drama, Thriller", "detective copenhagen killer", "A killer leaves a chestnut doll at every gruesome crime scene.")
            series("Trapped", 2015, 8.1, "Baltasar Kormákur", "Ólafur Darri Ólafsson, Ilmur Kristjánsdóttir", "Crime, Drama, Thriller", "iceland snowstorm detective", "A storm traps a murderer in a small Icelandic town.")
            series("Deadwind", 2018, 7.4, "Rike Jokela", "Pihla Viitala, Lauri Tilkanen", "Crime, Drama, Mystery", "helsinki detective", "A grieving detective investigates a murder tied to a wind-farm project.")
            series("Wallander", 2008, 7.9, "Henning Mankell", "Kenneth Branagh, Sarah Smart", "Crime, Drama, Mystery", "detective sweden ystad", "Inspector Kurt Wallander solves crimes in the bleak landscape of Skåne.")
            movie("The Girl with the Dragon Tattoo", 2009, 7.7, "Niels Arden Oplev", "Noomi Rapace, Michael Nyqvist", "Crime, Drama, Mystery", "hacker journalist millennium", "A journalist and a hacker hunt a decades-old killer.")
            movie("The Girl with the Dragon Tattoo", 2011, 7.8, "David Fincher", "Daniel Craig, Rooney Mara, Stellan Skarsgård", "Crime, Drama, Mystery", "hacker journalist millennium", "Fincher's version of the Millennium mystery.")
            movie("Insomnia", 1997, 7.2, "Erik Skjoldbjærg", "Stellan Skarsgård, Sverre Anker Ousdal", "Crime, Mystery, Thriller", "midnight sun detective", "Under the midnight sun, a detective covers up a fatal mistake.")
            movie("Insomnia", 2002, 7.2, "Christopher Nolan", "Al Pacino, Robin Williams, Hilary Swank", "Drama, Mystery, Thriller", "midnight sun detective alaska", "Two LA detectives hunt a killer in a town where the sun never sets.")
            movie("The Snowman", 2017, 5.2, "Tomas Alfredson", "Michael Fassbender, Rebecca Ferguson", "Crime, Horror, Mystery", "snow detective killer oslo", "Detective Harry Hole hunts a killer who strikes during the first snow.")
            movie("Headhunters", 2011, 7.5, "Morten Tyldum", "Aksel Hennie, Nikolaj Coster-Waldau", "Action, Crime, Thriller", "heist art chase", "A recruiter who steals art picks the wrong target.")
            movie("The Keeper of Lost Causes", 2013, 7.1, "Mikkel Nørgaard", "Nikolaj Lie Kaas, Fares Fares", "Crime, Mystery, Thriller", "cold case department", "Department Q reopens a five-year-old disappearance.")
            movie("Let the Right One In", 2008, 7.8, "Tomas Alfredson", "Kåre Hedebrant, Lina Leandersson", "Drama, Horror, Romance", "vampire snow stockholm", "A bullied boy befriends the strange girl next door.")
        }
        theme("cyberpunk") {
            movie("Blade Runner", 1982, 8.1, "Ridley Scott", "Harrison Ford, Rutger Hauer, Sean Young", "Sci-Fi, Action, Drama", "replicant android neon", "A blade runner hunts rogue replicants in a rain-soaked Los Angeles.")
            movie("Blade Runner 2049", 2017, 8.0, "Denis Villeneuve", "Ryan Gosling, Harrison Ford, Ana de Armas", "Sci-Fi, Drama, Mystery", "replicant android neon", "A new blade runner unearths a secret that could plunge society into chaos.")
            movie("The Matrix", 1999, 8.7, "The Wachowskis", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss", "Sci-Fi, Action", "simulation hacker reality", "A hacker learns his reality is a simulation run by machines.")
            movie("Ghost in the Shell", 1995, 7.9, "Mamoru Oshii", "Atsuko Tanaka, Akio Ōtsuka", "Animation, Sci-Fi, Action", "cyborg hacker identity", "A cyborg agent hunts a mysterious hacker known as the Puppet Master.")
            movie("Akira", 1988, 8.0, "Katsuhiro Otomo", "Mitsuo Iwata, Nozomu Sasaki", "Animation, Sci-Fi, Action", "neo-tokyo psychic biker", "A biker gang member gains devastating psychic powers in Neo-Tokyo.")
            movie("Ex Machina", 2014, 7.7, "Alex Garland", "Alicia Vikander, Domhnall Gleeson, Oscar Isaac", "Sci-Fi, Drama, Thriller", "android consciousness test", "A programmer is invited to test a humanoid AI.")
            movie("Her", 2013, 8.0, "Spike Jonze", "Joaquin Phoenix, Scarlett Johansson, Amy Adams", "Drama, Romance, Sci-Fi", "operating system love loneliness", "A lonely writer falls in love with his operating system.")
            movie("Minority Report", 2002, 7.6, "Steven Spielberg", "Tom Cruise, Colin Farrell, Samantha Morton", "Action, Crime, Sci-Fi", "precrime future police", "A cop who arrests future murderers is accused of one himself.")
            movie("Upgrade", 2018, 7.5, "Leigh Whannell", "Logan Marshall-Green, Betty Gabriel", "Action, Sci-Fi, Thriller", "implant revenge", "A chip implant gives a paralysed man the means for revenge.")
            movie("Dredd", 2012, 7.1, "Pete Travis", "Karl Urban, Olivia Thirlby, Lena Headey", "Action, Sci-Fi, Crime", "megacity judge tower", "A judge and a rookie are trapped in a 200-storey slum tower.")
            series("Mr. Robot", 2015, 8.5, "Sam Esmail", "Rami Malek, Christian Slater, Carly Chaikin", "Crime, Drama, Thriller", "hacker conspiracy", "A troubled hacker is recruited to take down the world's largest corporation.")
            series("Black Mirror", 2011, 8.7, "Charlie Brooker", "Daniel Kaluuya, Bryce Dallas Howard", "Drama, Sci-Fi, Thriller", "technology anthology", "Twisted tales about technology and human nature.")
            series("Westworld", 2016, 8.5, "Jonathan Nolan", "Evan Rachel Wood, Jeffrey Wright, Ed Harris, Thandiwe Newton", "Drama, Mystery, Sci-Fi", "android park consciousness", "Android hosts in a western theme park begin to wake up.")
            series("Altered Carbon", 2018, 7.9, "Laeta Kalogridis", "Joel Kinnaman, James Purefoy", "Action, Drama, Sci-Fi", "consciousness body sleeve", "In a future where minds can be swapped between bodies, a soldier solves a murder.")
            series("Cyberpunk: Edgerunners", 2022, 8.3, "Hiroyuki Imaishi", "KENN, Aoi Yûki", "Animation, Action, Sci-Fi", "neon implant night city", "A street kid becomes an edgerunner in Night City.")
        }
        theme("whodunit") {
            movie("Knives Out", 2019, 7.9, "Rian Johnson", "Daniel Craig, Ana de Armas, Chris Evans, Jamie Lee Curtis", "Comedy, Crime, Drama", "detective family inheritance", "Detective Benoit Blanc investigates the death of a crime novelist.")
            movie("Glass Onion: A Knives Out Mystery", 2022, 7.1, "Rian Johnson", "Daniel Craig, Edward Norton, Janelle Monáe", "Comedy, Crime, Drama", "detective island billionaire", "Benoit Blanc peels back the layers of a tech billionaire's island game.")
            movie("Murder on the Orient Express", 2017, 6.5, "Kenneth Branagh", "Kenneth Branagh, Penélope Cruz, Michelle Pfeiffer", "Crime, Drama, Mystery", "poirot train detective", "Poirot must solve a murder aboard a snowbound train.")
            movie("Death on the Nile", 2022, 6.3, "Kenneth Branagh", "Kenneth Branagh, Gal Gadot, Emma Mackey", "Crime, Drama, Mystery", "poirot cruise detective", "A honeymoon cruise on the Nile turns deadly.")
            movie("Gosford Park", 2001, 7.2, "Robert Altman", "Maggie Smith, Michael Gambon, Kristin Scott Thomas", "Comedy, Drama, Mystery", "manor servants murder", "A shooting party at an English country house ends in murder.")
            movie("Clue", 1985, 7.2, "Jonathan Lynn", "Tim Curry, Madeline Kahn, Christopher Lloyd", "Comedy, Crime, Mystery", "mansion guests murder", "Six guests, one mansion and a body in the study.")
            movie("The Name of the Rose", 1986, 7.7, "Jean-Jacques Annaud", "Sean Connery, Christian Slater", "Crime, Drama, Mystery", "monastery monk detective", "A friar investigates deaths in a medieval abbey.")
            movie("Zodiac", 2007, 7.7, "David Fincher", "Jake Gyllenhaal, Robert Downey Jr., Mark Ruffalo", "Crime, Drama, Mystery", "killer cipher journalist", "A cartoonist becomes obsessed with the Zodiac killer.")
            movie("See How They Run", 2022, 6.5, "Tom George", "Sam Rockwell, Saoirse Ronan, Adrien Brody", "Comedy, Crime, Mystery", "theatre detective london", "A murder backstage at a West End play.")
            movie("Enola Holmes", 2020, 6.6, "Harry Bradbeer", "Millie Bobby Brown, Henry Cavill, Sam Claflin", "Action, Adventure, Crime", "sherlock sister detective", "Sherlock's teenage sister sets out to find their missing mother.")
            series("Sherlock", 2010, 9.0, "Steven Moffat", "Benedict Cumberbatch, Martin Freeman", "Crime, Drama, Mystery", "detective london sherlock", "Sherlock Holmes and Dr Watson solve crimes in modern London.")
            series("Only Murders in the Building", 2021, 8.1, "Steve Martin", "Steve Martin, Martin Short, Selena Gomez", "Comedy, Crime, Drama", "podcast neighbours murder", "Three neighbours start a true-crime podcast about a death in their building.")
            series("Broadchurch", 2013, 8.4, "Chris Chibnall", "David Tennant, Olivia Colman", "Crime, Drama, Mystery", "detective seaside town", "The death of a boy tears apart a small coastal town.")
            series("True Detective", 2014, 8.9, "Nic Pizzolatto", "Matthew McConaughey, Woody Harrelson", "Crime, Drama, Mystery", "detective louisiana killer", "Two detectives hunt a ritualistic killer across seventeen years.")
            series("Mare of Easttown", 2021, 8.4, "Brad Ingelsby", "Kate Winslet, Julianne Nicholson, Evan Peters", "Crime, Drama, Mystery", "detective pennsylvania town", "A detective investigates a murder while her life falls apart.")
        }
        theme("fantasy") {
            movie("The Lord of the Rings: The Fellowship of the Ring", 2001, 8.9, "Peter Jackson", "Elijah Wood, Ian McKellen, Viggo Mortensen, Orlando Bloom", "Action, Adventure, Drama, Fantasy", "ring quest middle-earth", "A hobbit sets out to destroy a ring of terrible power.")
            movie("The Lord of the Rings: The Two Towers", 2002, 8.8, "Peter Jackson", "Elijah Wood, Ian McKellen, Viggo Mortensen, Orlando Bloom", "Action, Adventure, Drama, Fantasy", "ring quest middle-earth", "The fellowship is broken, but the war for Middle-earth has begun.")
            movie("The Lord of the Rings: The Return of the King", 2003, 9.0, "Peter Jackson", "Elijah Wood, Ian McKellen, Viggo Mortensen, Orlando Bloom", "Action, Adventure, Drama, Fantasy", "ring quest middle-earth", "The final battle for Middle-earth begins.")
            movie("The Hobbit: An Unexpected Journey", 2012, 7.8, "Peter Jackson", "Martin Freeman, Ian McKellen, Richard Armitage", "Adventure, Fantasy", "dragon quest middle-earth", "Bilbo joins thirteen dwarves on a quest to reclaim their home.")
            movie("Pan's Labyrinth", 2006, 8.2, "Guillermo del Toro", "Ivana Baquero, Sergi López, Maribel Verdú", "Drama, Fantasy, War", "faun labyrinth war", "A girl escapes into a dark fairy-tale world during the Spanish Civil War.")
            movie("Stardust", 2007, 7.6, "Matthew Vaughn", "Charlie Cox, Claire Danes, Michelle Pfeiffer, Robert De Niro", "Adventure, Family, Fantasy", "fallen star quest witch", "A young man crosses into a magical realm to retrieve a fallen star.")
            movie("The Green Knight", 2021, 6.6, "David Lowery", "Dev Patel, Alicia Vikander, Joel Edgerton", "Adventure, Drama, Fantasy", "knight quest arthurian", "Sir Gawain embarks on a quest to face the Green Knight.")
            movie("Willow", 1988, 7.2, "Ron Howard", "Warwick Davis, Val Kilmer, Joanne Whalley", "Action, Adventure, Comedy", "sorcerer quest baby", "A farmer must protect a special baby from an evil queen.")
            movie("Spirited Away", 2001, 8.6, "Hayao Miyazaki", "Rumi Hiiragi, Miyu Irino", "Animation, Adventure, Family", "spirits bathhouse", "A girl must work in a spirit bathhouse to free her parents.")
            movie("Princess Mononoke", 1997, 8.3, "Hayao Miyazaki", "Yôji Matsuda, Yuriko Ishida", "Animation, Action, Adventure", "forest gods war", "A prince is caught in a war between forest gods and humans.")
            movie("Dungeons & Dragons: Honor Among Thieves", 2023, 7.2, "John Francis Daley", "Chris Pine, Michelle Rodriguez, Regé-Jean Page", "Action, Adventure, Comedy", "heist quest dragon", "A charming thief and a band of adventurers attempt an epic heist.")
            series("Game of Thrones", 2011, 9.2, "David Benioff", "Emilia Clarke, Peter Dinklage, Kit Harington, Lena Headey", "Action, Adventure, Drama", "dragon throne war", "Noble families fight for the Iron Throne as an ancient threat returns.")
            series("House of the Dragon", 2022, 8.3, "Ryan Condal", "Paddy Considine, Emma D'Arcy, Matt Smith", "Action, Adventure, Drama", "dragon throne war", "The Targaryen civil war, two hundred years before Game of Thrones.")
            series("The Witcher", 2019, 8.0, "Lauren Schmidt Hissrich", "Henry Cavill, Anya Chalotra, Freya Allan", "Action, Adventure, Drama", "monster sorcery destiny", "A monster hunter struggles to find his place in a world of cruel people.")
            series("The Lord of the Rings: The Rings of Power", 2022, 6.9, "J.D. Payne", "Morfydd Clark, Charlie Vickers", "Action, Adventure, Drama", "ring middle-earth elves", "The forging of the rings of power in Middle-earth's Second Age.")
        }
        theme("postapoc") {
            movie("Mad Max: Fury Road", 2015, 8.1, "George Miller", "Tom Hardy, Charlize Theron, Nicholas Hoult", "Action, Adventure, Sci-Fi", "wasteland chase desert", "A drifter and a rebel warrior flee a tyrant across the wasteland.")
            movie("Furiosa: A Mad Max Saga", 2024, 7.5, "George Miller", "Anya Taylor-Joy, Chris Hemsworth, Tom Burke", "Action, Adventure, Sci-Fi", "wasteland revenge desert", "The origin of the warrior Furiosa.")
            movie("Children of Men", 2006, 7.9, "Alfonso Cuarón", "Clive Owen, Julianne Moore, Michael Caine", "Action, Drama, Sci-Fi", "infertility refugee dystopia", "In a world without children, one pregnant woman is humanity's last hope.")
            movie("The Road", 2009, 7.2, "John Hillcoat", "Viggo Mortensen, Kodi Smit-McPhee, Charlize Theron", "Drama, Thriller", "father son ash survival", "A father and son walk through a burned America.")
            movie("28 Days Later", 2002, 7.5, "Danny Boyle", "Cillian Murphy, Naomie Harris, Brendan Gleeson", "Drama, Horror, Sci-Fi", "virus london infected", "A man wakes from a coma to find London overrun by the infected.")
            movie("A Quiet Place", 2018, 7.5, "John Krasinski", "Emily Blunt, John Krasinski, Millicent Simmonds", "Drama, Horror, Sci-Fi", "silence creatures family", "A family must live in silence to hide from creatures that hunt by sound.")
            movie("I Am Legend", 2007, 7.2, "Francis Lawrence", "Will Smith, Alice Braga", "Action, Drama, Sci-Fi", "virus survivor new-york", "The last man in New York searches for a cure.")
            movie("Snowpiercer", 2013, 7.1, "Bong Joon Ho", "Chris Evans, Tilda Swinton, Song Kang-ho", "Action, Drama, Sci-Fi", "train frozen revolution", "The last humans circle a frozen Earth aboard a class-divided train.")
            movie("Dawn of the Planet of the Apes", 2014, 7.6, "Matt Reeves", "Andy Serkis, Jason Clarke, Gary Oldman", "Action, Adventure, Drama", "apes virus war", "A fragile peace between apes and surviving humans collapses.")
            movie("Oblivion", 2013, 7.0, "Joseph Kosinski", "Tom Cruise, Morgan Freeman, Andrea Riseborough", "Action, Adventure, Sci-Fi", "drone ruined earth memory", "A drone repairman on a ruined Earth questions his mission.")
            movie("WALL·E", 2008, 8.4, "Andrew Stanton", "Ben Burtt, Elissa Knight", "Animation, Adventure, Family", "robot earth love", "A lonely trash-compacting robot falls in love.")
            movie("The Book of Eli", 2010, 6.9, "The Hughes Brothers", "Denzel Washington, Gary Oldman, Mila Kunis", "Action, Adventure, Drama", "wasteland book journey", "A lone wanderer protects a sacred book across a ravaged America.")
            series("The Last of Us", 2023, 8.6, "Craig Mazin", "Pedro Pascal, Bella Ramsey", "Action, Adventure, Drama", "fungus infected journey", "A smuggler escorts a teenage girl across a ruined America.")
            series("Station Eleven", 2021, 7.5, "Patrick Somerville", "Mackenzie Davis, Himesh Patel", "Drama, Sci-Fi", "pandemic theatre survivors", "A travelling theatre troupe keeps art alive after a pandemic.")
            series("Fallout", 2024, 8.3, "Geneva Robertson-Dworet", "Ella Purnell, Aaron Moten, Walton Goggins", "Action, Adventure, Drama", "vault wasteland nuclear", "A vault dweller ventures into the irradiated wasteland of Los Angeles.")
        }
        theme("psych") {
            movie("Inception", 2010, 8.8, "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Tom Hardy", "Action, Adventure, Sci-Fi", "dreams heist subconscious", "A thief who steals secrets through dreams is tasked with planting an idea.")
            movie("Memento", 2000, 8.4, "Christopher Nolan", "Guy Pearce, Carrie-Anne Moss, Joe Pantoliano", "Mystery, Thriller", "memory revenge tattoos", "A man with short-term memory loss hunts his wife's killer.")
            movie("The Prestige", 2006, 8.5, "Christopher Nolan", "Christian Bale, Hugh Jackman, Scarlett Johansson, Michael Caine", "Drama, Mystery, Sci-Fi", "magicians rivalry obsession", "Two rival magicians become obsessed with the ultimate illusion.")
            movie("Shutter Island", 2010, 8.2, "Martin Scorsese", "Leonardo DiCaprio, Emily Mortimer, Mark Ruffalo", "Mystery, Thriller", "asylum island detective", "A marshal investigates a disappearance at an island asylum.")
            movie("Gone Girl", 2014, 8.1, "David Fincher", "Ben Affleck, Rosamund Pike, Neil Patrick Harris", "Drama, Mystery, Thriller", "marriage disappearance media", "A husband becomes the prime suspect when his wife vanishes.")
            movie("Se7en", 1995, 8.6, "David Fincher", "Morgan Freeman, Brad Pitt, Gwyneth Paltrow", "Crime, Drama, Mystery", "killer sins detective", "Two detectives hunt a killer who uses the seven deadly sins.")
            movie("Fight Club", 1999, 8.8, "David Fincher", "Brad Pitt, Edward Norton, Helena Bonham Carter", "Drama", "insomnia identity anarchy", "An insomniac and a soap maker form an underground fight club.")
            movie("Black Swan", 2010, 8.0, "Darren Aronofsky", "Natalie Portman, Mila Kunis, Vincent Cassel", "Drama, Thriller", "ballet obsession double", "A ballerina's pursuit of perfection consumes her.")
            movie("Prisoners", 2013, 8.1, "Denis Villeneuve", "Hugh Jackman, Jake Gyllenhaal, Paul Dano", "Crime, Drama, Mystery", "abduction detective father", "A father takes matters into his own hands when his daughter disappears.")
            movie("Enemy", 2013, 6.9, "Denis Villeneuve", "Jake Gyllenhaal, Mélanie Laurent, Sarah Gadon", "Mystery, Thriller", "double identity toronto", "A teacher discovers an actor who looks exactly like him.")
            movie("Nightcrawler", 2014, 7.8, "Dan Gilroy", "Jake Gyllenhaal, Rene Russo, Riz Ahmed", "Crime, Drama, Thriller", "news night camera", "A driven man enters the world of nocturnal crime journalism.")
            movie("Parasite", 2019, 8.5, "Bong Joon Ho", "Song Kang-ho, Lee Sun-kyun, Cho Yeo-jeong", "Drama, Thriller", "class family deception", "A poor family schemes its way into a wealthy household.")
            movie("Oldboy", 2003, 8.3, "Park Chan-wook", "Choi Min-sik, Yoo Ji-tae, Kang Hye-jung", "Action, Drama, Mystery", "revenge imprisonment", "Imprisoned for fifteen years without reason, a man seeks answers.")
            series("Severance", 2022, 8.7, "Dan Erickson", "Adam Scott, Britt Lower, Patricia Arquette", "Drama, Mystery, Sci-Fi", "office memory identity", "Office workers have their work memories surgically separated.")
            series("Mindhunter", 2017, 8.6, "Joe Penhall", "Jonathan Groff, Holt McCallany, Anna Torv", "Crime, Drama, Thriller", "profiling killers fbi", "FBI agents interview serial killers to understand how they think.")
        }
        theme("cozy") {
            movie("The Grand Budapest Hotel", 2014, 8.1, "Wes Anderson", "Ralph Fiennes, Tony Revolori, Saoirse Ronan", "Adventure, Comedy, Crime", "hotel concierge painting", "A legendary concierge and his lobby boy are caught up in an art theft.")
            movie("Moonrise Kingdom", 2012, 7.8, "Wes Anderson", "Jared Gilman, Kara Hayward, Bruce Willis, Bill Murray", "Comedy, Drama, Romance", "island scouts love", "Two twelve-year-olds fall in love and run away together.")
            movie("Fantastic Mr. Fox", 2009, 7.9, "Wes Anderson", "George Clooney, Meryl Streep, Bill Murray", "Animation, Adventure, Comedy", "fox farmers heist", "A fox can't resist one last raid on three nasty farmers.")
            movie("Amélie", 2001, 8.3, "Jean-Pierre Jeunet", "Audrey Tautou, Mathieu Kassovitz", "Comedy, Romance", "paris kindness", "A shy waitress decides to change the lives of those around her.")
            movie("Paddington 2", 2017, 7.8, "Paul King", "Ben Whishaw, Hugh Grant, Sally Hawkins", "Adventure, Comedy, Family", "bear london kindness", "Paddington is framed for the theft of a precious pop-up book.")
            movie("The Secret Life of Walter Mitty", 2013, 7.3, "Ben Stiller", "Ben Stiller, Kristen Wiig, Sean Penn", "Adventure, Comedy, Drama", "daydream journey iceland", "A daydreamer embarks on a real global adventure.")
            movie("About Time", 2013, 7.8, "Richard Curtis", "Domhnall Gleeson, Rachel McAdams, Bill Nighy", "Comedy, Drama, Fantasy", "time travel love family", "A young man discovers he can travel back in time.")
            movie("Chef", 2014, 7.3, "Jon Favreau", "Jon Favreau, Sofía Vergara, John Leguizamo", "Adventure, Comedy, Drama", "food truck father", "A chef starts over with a food truck and reconnects with his son.")
            movie("Little Miss Sunshine", 2006, 7.8, "Jonathan Dayton", "Steve Carell, Toni Collette, Paul Dano", "Comedy, Drama", "road trip family pageant", "A dysfunctional family road-trips to a children's beauty pageant.")
            movie("My Neighbor Totoro", 1988, 8.1, "Hayao Miyazaki", "Noriko Hidaka, Chika Sakamoto", "Animation, Comedy, Family", "forest spirits sisters", "Two sisters discover friendly forest spirits near their new home.")
            movie("The Intouchables", 2011, 8.5, "Olivier Nakache", "François Cluzet, Omar Sy", "Biography, Comedy, Drama", "friendship paris caregiver", "An unlikely friendship between an aristocrat and his caregiver.")
            movie("Groundhog Day", 1993, 8.0, "Harold Ramis", "Bill Murray, Andie MacDowell", "Comedy, Drama, Fantasy", "time loop weatherman", "A weatherman relives the same day again and again.")
            series("Ted Lasso", 2020, 8.8, "Bill Lawrence", "Jason Sudeikis, Hannah Waddingham, Brett Goldstein", "Comedy, Drama, Sport", "football coach optimism", "An American football coach takes over an English soccer team.")
            series("Schitt's Creek", 2015, 8.5, "Dan Levy", "Eugene Levy, Catherine O'Hara, Dan Levy", "Comedy", "family small town", "A wealthy family loses everything and moves to a tiny town.")
            series("Abbott Elementary", 2021, 8.2, "Quinta Brunson", "Quinta Brunson, Tyler James Williams, Janelle James", "Comedy", "school teachers mockumentary", "Dedicated teachers at an underfunded Philadelphia school.")
        }
        theme("space") {
            movie("Star Wars: A New Hope", 1977, 8.6, "George Lucas", "Mark Hamill, Harrison Ford, Carrie Fisher, Alec Guinness", "Action, Adventure, Fantasy", "rebels empire force", "A farm boy joins a rebellion against a galactic empire.")
            movie("Star Wars: The Empire Strikes Back", 1980, 8.7, "Irvin Kershner", "Mark Hamill, Harrison Ford, Carrie Fisher", "Action, Adventure, Fantasy", "rebels empire force", "The rebels are on the run as Darth Vader hunts Luke.")
            movie("Star Trek", 2009, 7.9, "J.J. Abrams", "Chris Pine, Zachary Quinto, Zoe Saldana", "Action, Adventure, Sci-Fi", "starship crew federation", "A new crew of the Enterprise faces a vengeful Romulan.")
            movie("Guardians of the Galaxy", 2014, 8.0, "James Gunn", "Chris Pratt, Zoe Saldana, Dave Bautista", "Action, Adventure, Comedy", "misfits galaxy heist", "A band of misfits fights to save the galaxy.")
            movie("Prometheus", 2012, 7.0, "Ridley Scott", "Noomi Rapace, Michael Fassbender, Charlize Theron", "Adventure, Mystery, Sci-Fi", "origin creature expedition", "Scientists follow a star map to humanity's origins.")
            movie("Event Horizon", 1997, 6.6, "Paul W.S. Anderson", "Laurence Fishburne, Sam Neill", "Horror, Sci-Fi, Mystery", "ship haunted rescue", "A rescue crew finds a ship that has returned from hell.")
            movie("Gattaca", 1997, 7.7, "Andrew Niccol", "Ethan Hawke, Uma Thurman, Jude Law", "Drama, Sci-Fi, Thriller", "genetics future dream", "In a genetic caste society, an 'invalid' dreams of space.")
            movie("Annihilation", 2018, 6.8, "Alex Garland", "Natalie Portman, Jennifer Jason Leigh, Tessa Thompson", "Adventure, Drama, Mystery", "shimmer expedition mutation", "A biologist enters a mysterious zone where nature is rewritten.")
            movie("Edge of Tomorrow", 2014, 7.9, "Doug Liman", "Tom Cruise, Emily Blunt", "Action, Sci-Fi", "time loop aliens war", "A soldier relives the same day of an alien invasion.")
            movie("District 9", 2009, 7.9, "Neill Blomkamp", "Sharlto Copley, David James", "Action, Sci-Fi, Thriller", "aliens slum apartheid", "Aliens stranded over Johannesburg live in a fenced-off slum.")
            movie("Silent Running", 1972, 6.6, "Douglas Trumbull", "Bruce Dern", "Adventure, Drama, Sci-Fi", "forest ship robots", "A botanist protects the last forests of Earth in space.")
            movie("Galaxy Quest", 1999, 7.3, "Dean Parisot", "Tim Allen, Sigourney Weaver, Alan Rickman", "Adventure, Comedy, Sci-Fi", "actors aliens parody", "Washed-up TV actors are recruited by real aliens.")
            series("Star Trek: The Next Generation", 1987, 8.7, "Gene Roddenberry", "Patrick Stewart, Jonathan Frakes, Brent Spiner", "Action, Adventure, Sci-Fi", "starship crew federation", "Captain Picard and the Enterprise-D explore the galaxy.")
            series("Battlestar Galactica", 2004, 8.7, "Ronald D. Moore", "Edward James Olmos, Mary McDonnell, Jamie Bamber", "Action, Drama, Sci-Fi", "fleet cylons survival", "The last human fleet flees the machines that destroyed their worlds.")
            series("Foundation", 2021, 7.4, "David S. Goyer", "Jared Harris, Lee Pace, Lou Llobell", "Drama, Sci-Fi", "empire prophecy mathematics", "A mathematician predicts the fall of a galactic empire.")
            series("Andor", 2022, 8.4, "Tony Gilroy", "Diego Luna, Stellan Skarsgård, Genevieve O'Reilly", "Action, Adventure, Drama", "rebellion spy empire", "A thief becomes a spy in the rebellion against the empire.")
            series("The Mandalorian", 2019, 8.7, "Jon Favreau", "Pedro Pascal, Carl Weathers", "Action, Adventure, Sci-Fi", "bounty hunter child", "A lone bounty hunter protects a mysterious child.")
        }
        theme("nordic") {
            movie("The Hunt", 2012, 8.3, "Thomas Vinterberg", "Mads Mikkelsen, Thomas Bo Larsen", "Drama, Thriller", "accusation village lie", "A teacher's life collapses after a false accusation.")
            movie("Force Majeure", 2014, 7.2, "Ruben Östlund", "Johannes Kuhnke, Lisa Loven Kongsli", "Comedy, Drama", "avalanche family alps", "A family's holiday unravels after an avalanche scare.")
            movie("Border", 2018, 7.0, "Ali Abbasi", "Eva Melander, Eero Milonoff", "Drama, Fantasy, Mystery", "customs troll sweden", "A customs officer who can smell guilt meets a mysterious traveller.")
            movie("Another Round", 2020, 7.7, "Thomas Vinterberg", "Mads Mikkelsen, Thomas Bo Larsen", "Comedy, Drama", "alcohol teachers friendship", "Four teachers test a theory that a little alcohol improves life.")
            movie("Midsommar", 2019, 7.1, "Ari Aster", "Florence Pugh, Jack Reynor, Will Poulter", "Drama, Horror, Mystery", "cult sweden festival", "A grieving woman joins a Swedish midsummer festival.")
            series("Borgen", 2010, 8.4, "Adam Price", "Sidse Babett Knudsen, Birgitte Hjort Sørensen", "Drama", "politics denmark prime minister", "A woman rises to become Denmark's first female prime minister.")
            series("Bordertown", 2016, 7.7, "Miikko Oikkonen", "Ville Virtanen, Matleena Kuusniemi", "Crime, Drama, Mystery", "detective finland", "A brilliant detective moves to a quiet Finnish border town.")
            series("Jordskott", 2015, 7.5, "Henrik Björn", "Moa Gammel, Göran Ragnerstam", "Crime, Drama, Fantasy", "forest mystery sweden", "A woman returns home to investigate a missing child and a forest's secrets.")
            series("Ragnarok", 2020, 7.0, "Adam Price", "David Stakston, Jonas Strand Gravli", "Action, Drama, Fantasy", "norse gods norway", "A teenager discovers he is the reincarnation of Thor.")
            series("Occupied", 2015, 7.5, "Jo Nesbø", "Henrik Mestad, Eldar Skar", "Drama, Thriller", "russia norway oil", "Norway is quietly occupied by Russia after an energy crisis.")
            series("The Rain", 2018, 6.3, "Jannik Tai Mosholt", "Alba August, Lucas Lynggaard Tønnesen", "Drama, Sci-Fi, Thriller", "virus denmark survival", "Siblings survive a virus carried in rain.")
        }
        theme("cyberpunk") {
            movie("Tron: Legacy", 2010, 6.8, "Joseph Kosinski", "Garrett Hedlund, Jeff Bridges, Olivia Wilde", "Action, Adventure, Sci-Fi", "digital grid program", "A man enters the digital world his father created.")
            movie("Total Recall", 1990, 7.5, "Paul Verhoeven", "Arnold Schwarzenegger, Sharon Stone", "Action, Adventure, Sci-Fi", "memory mars identity", "A worker's implanted memories hide a mission on Mars.")
            movie("RoboCop", 1987, 7.6, "Paul Verhoeven", "Peter Weller, Nancy Allen", "Action, Crime, Sci-Fi", "cyborg detroit corporation", "A murdered cop is rebuilt as a cyborg.")
            movie("Looper", 2012, 7.4, "Rian Johnson", "Joseph Gordon-Levitt, Bruce Willis, Emily Blunt", "Action, Drama, Sci-Fi", "time travel assassin", "A hitman must kill his own future self.")
            movie("Elysium", 2013, 6.6, "Neill Blomkamp", "Matt Damon, Jodie Foster, Sharlto Copley", "Action, Drama, Sci-Fi", "class space station", "A worker fights to reach a paradise orbiting Earth.")
            movie("Alita: Battle Angel", 2019, 7.3, "Robert Rodriguez", "Rosa Salazar, Christoph Waltz", "Action, Adventure, Sci-Fi", "cyborg warrior city", "A cyborg with no memory discovers her past in a scrapyard city.")
            movie("Terminator 2: Judgment Day", 1991, 8.6, "James Cameron", "Arnold Schwarzenegger, Linda Hamilton, Edward Furlong", "Action, Sci-Fi", "terminator skynet future", "A reprogrammed Terminator protects the boy who will save humanity.")
            movie("The Terminator", 1984, 8.1, "James Cameron", "Arnold Schwarzenegger, Michael Biehn, Linda Hamilton", "Action, Sci-Fi", "terminator skynet future", "A cyborg is sent back to kill the mother of the future leader.")
            movie("The Matrix Reloaded", 2003, 7.2, "The Wachowskis", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss", "Action, Sci-Fi", "simulation hacker reality", "Neo and the rebels race to save Zion.")
            movie("Paprika", 2006, 7.7, "Satoshi Kon", "Megumi Hayashibara, Tōru Furuya", "Animation, Mystery, Sci-Fi", "dreams device therapy", "A therapist enters dreams as a device is stolen.")
            series("Love, Death & Robots", 2019, 8.4, "Tim Miller", "Nolan North, Mackenzie Davis", "Animation, Action, Sci-Fi", "anthology robots", "An anthology of short animated sci-fi stories.")
            series("Devs", 2020, 7.6, "Alex Garland", "Sonoya Mizuno, Nick Offerman", "Drama, Mystery, Sci-Fi", "quantum determinism tech", "A programmer investigates her boyfriend's death at a secretive tech company.")
            series("Silo", 2023, 8.1, "Graham Yost", "Rebecca Ferguson, Tim Robbins", "Drama, Mystery, Sci-Fi", "bunker secrets dystopia", "Thousands live in an underground silo with strict rules.")
        }
        theme("whodunit") {
            movie("Rear Window", 1954, 8.5, "Alfred Hitchcock", "James Stewart, Grace Kelly, Raymond Burr", "Mystery, Thriller", "neighbours voyeur murder", "A photographer confined to his apartment suspects a neighbour of murder.")
            movie("Vertigo", 1958, 8.3, "Alfred Hitchcock", "James Stewart, Kim Novak", "Mystery, Romance, Thriller", "obsession detective", "A detective is hired to follow a woman and becomes obsessed.")
            movie("Chinatown", 1974, 8.1, "Roman Polanski", "Jack Nicholson, Faye Dunaway", "Drama, Mystery, Thriller", "detective water noir", "A private eye uncovers corruption in 1930s Los Angeles.")
            movie("L.A. Confidential", 1997, 7.8, "Curtis Hanson", "Kevin Spacey, Russell Crowe, Guy Pearce", "Crime, Drama, Mystery", "police corruption noir", "Three very different cops uncover conspiracy in 1950s LA.")
            movie("The Maltese Falcon", 1941, 8.0, "John Huston", "Humphrey Bogart, Mary Astor", "Film-Noir, Mystery", "detective statue noir", "A private eye is caught in the hunt for a priceless statuette.")
            movie("Mystic River", 2003, 7.9, "Clint Eastwood", "Sean Penn, Tim Robbins, Kevin Bacon", "Crime, Drama, Mystery", "murder friends boston", "A murder reunites three childhood friends.")
            movie("Sherlock Holmes", 2009, 7.6, "Guy Ritchie", "Robert Downey Jr., Jude Law, Rachel McAdams", "Action, Adventure, Mystery", "detective sherlock london", "Holmes and Watson battle a secret society.")
            movie("Identity", 2003, 7.3, "James Mangold", "John Cusack, Ray Liotta, Amanda Peet", "Mystery, Thriller, Horror", "motel storm killer", "Strangers stranded at a motel are killed one by one.")
            series("Agatha Christie's Poirot", 1989, 8.2, "Clive Exton", "David Suchet, Hugh Fraser", "Crime, Drama, Mystery", "poirot detective belgian", "The fastidious Belgian detective solves murders.")
            series("Line of Duty", 2012, 8.6, "Jed Mercurio", "Martin Compston, Vicky McClure, Adrian Dunbar", "Crime, Drama, Thriller", "police corruption anti-corruption", "An anti-corruption unit investigates dirty cops.")
            series("The Sinner", 2017, 7.4, "Derek Simonds", "Bill Pullman, Jessica Biel", "Crime, Drama, Mystery", "detective motive past", "A detective digs into why ordinary people commit shocking crimes.")
            series("Big Little Lies", 2017, 8.5, "David E. Kelley", "Reese Witherspoon, Nicole Kidman, Shailene Woodley", "Crime, Drama, Mystery", "murder mothers monterey", "Three mothers are entangled in a murder.")
        }
        theme("fantasy") {
            movie("Harry Potter and the Sorcerer's Stone", 2001, 7.6, "Chris Columbus", "Daniel Radcliffe, Emma Watson, Rupert Grint", "Adventure, Family, Fantasy", "wizard school magic", "A boy learns he is a wizard and goes to Hogwarts.")
            movie("Harry Potter and the Prisoner of Azkaban", 2004, 7.9, "Alfonso Cuarón", "Daniel Radcliffe, Emma Watson, Rupert Grint", "Adventure, Family, Fantasy", "wizard school magic", "Harry faces an escaped prisoner of Azkaban.")
            movie("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe", 2005, 6.9, "Andrew Adamson", "Georgie Henley, Tilda Swinton", "Adventure, Family, Fantasy", "wardrobe witch lion", "Four siblings enter a magical land through a wardrobe.")
            movie("The NeverEnding Story", 1984, 7.4, "Wolfgang Petersen", "Barret Oliver, Noah Hathaway", "Adventure, Family, Fantasy", "book fantasia nothing", "A boy is drawn into the pages of a magical book.")
            movie("Labyrinth", 1986, 7.3, "Jim Henson", "David Bowie, Jennifer Connelly", "Adventure, Family, Fantasy", "goblin king maze", "A girl must solve a maze to rescue her baby brother.")
            movie("Excalibur", 1981, 7.2, "John Boorman", "Nigel Terry, Nicol Williamson, Helen Mirren", "Adventure, Drama, Fantasy", "arthur sword camelot", "The legend of King Arthur and his sword.")
            movie("Conan the Barbarian", 1982, 6.9, "John Milius", "Arnold Schwarzenegger, James Earl Jones", "Action, Adventure, Fantasy", "barbarian revenge sorcery", "A barbarian avenges his parents' murder.")
            series("His Dark Materials", 2019, 7.8, "Jack Thorne", "Dafne Keen, Ruth Wilson, James McAvoy", "Adventure, Drama, Fantasy", "parallel worlds daemon", "A girl searches for her kidnapped friend across parallel worlds.")
            series("Shadow and Bone", 2021, 7.5, "Eric Heisserer", "Jessie Mei Li, Ben Barnes", "Action, Adventure, Fantasy", "grisha darkness fold", "A mapmaker discovers a power that could free her land from darkness.")
            series("The Wheel of Time", 2021, 7.1, "Rafe Judkins", "Rosamund Pike, Daniel Henney", "Action, Adventure, Drama", "prophecy dragon reborn", "A sorceress searches for the Dragon Reborn.")
            series("Merlin", 2008, 7.8, "Julian Jones", "Colin Morgan, Bradley James", "Adventure, Drama, Fantasy", "arthur camelot magic", "A young Merlin protects Prince Arthur in secret.")
        }
        theme("postapoc") {
            movie("Mad Max 2: The Road Warrior", 1981, 7.6, "George Miller", "Mel Gibson, Bruce Spence", "Action, Adventure, Sci-Fi", "wasteland chase desert", "Max helps settlers defend their fuel against marauders.")
            movie("Waterworld", 1995, 6.2, "Kevin Reynolds", "Kevin Costner, Jeanne Tripplehorn", "Action, Adventure, Sci-Fi", "ocean survival drifter", "A drifter protects a girl on a flooded Earth.")
            movie("Zombieland", 2009, 7.6, "Ruben Fleischer", "Jesse Eisenberg, Woody Harrelson, Emma Stone", "Adventure, Comedy, Horror", "zombies road trip rules", "Survivors road-trip through a zombie-infested America.")
            movie("Train to Busan", 2016, 7.6, "Yeon Sang-ho", "Gong Yoo, Ma Dong-seok", "Action, Horror, Thriller", "zombies train korea", "Passengers fight to survive a zombie outbreak on a train.")
            movie("Bird Box", 2018, 6.6, "Susanne Bier", "Sandra Bullock, Trevante Rhodes", "Drama, Horror, Sci-Fi", "blindfold creatures family", "A mother guides her children blindfolded through a hostile world.")
            movie("Planet of the Apes", 1968, 7.9, "Franklin J. Schaffner", "Charlton Heston, Roddy McDowall", "Action, Adventure, Sci-Fi", "apes twist astronaut", "Astronauts crash on a planet ruled by apes.")
            movie("The Day After Tomorrow", 2004, 6.4, "Roland Emmerich", "Dennis Quaid, Jake Gyllenhaal", "Action, Adventure, Sci-Fi", "climate ice storm", "A sudden ice age engulfs the northern hemisphere.")
            series("The Walking Dead", 2010, 8.1, "Frank Darabont", "Andrew Lincoln, Norman Reedus", "Drama, Horror, Thriller", "zombies survival group", "Survivors try to live in a zombie apocalypse.")
            series("Sweet Tooth", 2021, 7.8, "Jim Mickle", "Christian Convery, Nonso Anozie", "Adventure, Drama, Fantasy", "hybrid boy journey", "A hybrid boy journeys across a world after a pandemic.")
            series("The 100", 2014, 7.6, "Jason Rothenberg", "Eliza Taylor, Bob Morley", "Drama, Mystery, Sci-Fi", "teenagers earth return", "Teenagers return to a radioactive Earth.")
            series("Years and Years", 2019, 8.0, "Russell T Davies", "Emma Thompson, Rory Kinnear", "Drama, Sci-Fi", "family future politics", "A family faces the near future as the world unravels.")
        }
        theme("psych") {
            movie("Requiem for a Dream", 2000, 8.3, "Darren Aronofsky", "Ellen Burstyn, Jared Leto, Jennifer Connelly", "Drama", "addiction spiral", "Four people's addictions destroy their dreams.")
            movie("Mulholland Drive", 2001, 7.9, "David Lynch", "Naomi Watts, Laura Harring", "Drama, Mystery, Thriller", "hollywood dream identity", "An actress helps an amnesiac woman in Hollywood.")
            movie("The Silence of the Lambs", 1991, 8.6, "Jonathan Demme", "Jodie Foster, Anthony Hopkins", "Crime, Drama, Thriller", "killer fbi cannibal", "A young FBI trainee consults a cannibal to catch another killer.")
            movie("Donnie Darko", 2001, 8.0, "Richard Kelly", "Jake Gyllenhaal, Jena Malone", "Drama, Sci-Fi, Thriller", "rabbit time apocalypse", "A troubled teenager is guided by a mysterious rabbit.")
            movie("Eternal Sunshine of the Spotless Mind", 2004, 8.3, "Michel Gondry", "Jim Carrey, Kate Winslet", "Drama, Romance, Sci-Fi", "memory erase love", "A couple erases each other from their memories.")
            movie("Joker", 2019, 8.4, "Todd Phillips", "Joaquin Phoenix, Robert De Niro", "Crime, Drama, Thriller", "clown madness gotham", "A failed comedian descends into madness.")
            movie("Split", 2016, 7.3, "M. Night Shyamalan", "James McAvoy, Anya Taylor-Joy", "Horror, Thriller", "identities kidnapping", "A man with 23 personalities kidnaps three girls.")
            movie("The Sixth Sense", 1999, 8.2, "M. Night Shyamalan", "Bruce Willis, Haley Joel Osment", "Drama, Mystery, Thriller", "ghosts child twist", "A boy who sees dead people is helped by a child psychologist.")
            movie("Whiplash", 2014, 8.5, "Damien Chazelle", "Miles Teller, J.K. Simmons", "Drama, Music", "drummer obsession teacher", "A young drummer is pushed to the brink by his teacher.")
            movie("Mother!", 2017, 6.6, "Darren Aronofsky", "Jennifer Lawrence, Javier Bardem", "Drama, Horror, Mystery", "house guests allegory", "A couple's home is invaded by uninvited guests.")
            series("Dark", 2017, 8.7, "Baran bo Odar", "Louis Hofmann, Lisa Vicari", "Crime, Drama, Mystery", "time travel germany family", "A child's disappearance exposes a time-travelling web.")
            series("Hannibal", 2013, 8.5, "Bryan Fuller", "Hugh Dancy, Mads Mikkelsen", "Crime, Drama, Horror", "cannibal profiler", "A profiler is drawn into a dance with Hannibal Lecter.")
            series("You", 2018, 7.7, "Greg Berlanti", "Penn Badgley, Elizabeth Lail", "Crime, Drama, Romance", "stalker obsession", "A charming bookstore manager becomes obsessed.")
            series("Twin Peaks", 1990, 8.8, "Mark Frost", "Kyle MacLachlan, Sheryl Lee", "Crime, Drama, Mystery", "fbi town dream", "An FBI agent investigates a homecoming queen's murder.")
        }
        theme("cozy") {
            movie("Notting Hill", 1999, 7.2, "Roger Michell", "Julia Roberts, Hugh Grant", "Comedy, Drama, Romance", "bookshop movie star london", "A bookseller falls for a famous actress.")
            movie("Love Actually", 2003, 7.6, "Richard Curtis", "Hugh Grant, Colin Firth, Emma Thompson", "Comedy, Drama, Romance", "christmas love london", "Interwoven love stories in the weeks before Christmas.")
            movie("The Princess Bride", 1987, 8.0, "Rob Reiner", "Cary Elwes, Robin Wright, Mandy Patinkin", "Adventure, Family, Fantasy", "swordfight true love", "A farmhand rescues his true love from a prince.")
            movie("Back to the Future", 1985, 8.5, "Robert Zemeckis", "Michael J. Fox, Christopher Lloyd", "Adventure, Comedy, Sci-Fi", "time travel delorean", "A teenager is sent back to 1955 in a time-travelling car.")
            movie("The Holiday", 2006, 6.9, "Nancy Meyers", "Cameron Diaz, Kate Winslet, Jude Law", "Comedy, Romance", "house swap christmas", "Two women swap homes for the holidays.")
            movie("Crazy Rich Asians", 2018, 6.9, "Jon M. Chu", "Constance Wu, Henry Golding", "Comedy, Drama, Romance", "wedding singapore family", "A woman meets her boyfriend's ultra-wealthy family.")
            movie("Forrest Gump", 1994, 8.8, "Robert Zemeckis", "Tom Hanks, Robin Wright, Gary Sinise", "Drama, Romance", "history simple life", "A kind man drifts through decades of American history.")
            series("Parks and Recreation", 2009, 8.6, "Greg Daniels", "Amy Poehler, Nick Offerman, Aziz Ansari", "Comedy", "town government mockumentary", "A deputy director tries to improve her small town.")
            series("Friends", 1994, 8.9, "David Crane", "Jennifer Aniston, Courteney Cox, Matt LeBlanc", "Comedy, Romance", "friends new-york apartment", "Six friends navigate life and love in Manhattan.")
            series("The Office", 2005, 9.0, "Greg Daniels", "Steve Carell, Rainn Wilson, John Krasinski", "Comedy", "workplace mockumentary", "A mockumentary about the staff of a paper company.")
            series("Brooklyn Nine-Nine", 2013, 8.4, "Dan Goor", "Andy Samberg, Andre Braugher", "Comedy, Crime", "police precinct", "Detectives of a Brooklyn precinct joke their way through cases.")
            series("Fleabag", 2016, 8.7, "Phoebe Waller-Bridge", "Phoebe Waller-Bridge, Sian Clifford", "Comedy, Drama", "london family grief", "A sharp-tongued woman navigates life and loss in London.")
            series("Derry Girls", 2018, 8.4, "Lisa McGee", "Saoirse-Monica Jackson, Nicola Coughlan", "Comedy", "teenagers northern-ireland", "Teenage friends in 1990s Northern Ireland.")
        }
        theme("heist") {
            movie("Ocean's Eleven", 2001, 7.7, "Steven Soderbergh", "George Clooney, Brad Pitt, Julia Roberts, Matt Damon", "Crime, Thriller", "casino vault crew", "A crew plans to rob three Las Vegas casinos at once.")
            movie("Heat", 1995, 8.3, "Michael Mann", "Al Pacino, Robert De Niro, Val Kilmer", "Action, Crime, Drama", "robbery detective los-angeles", "A detective and a master thief clash in Los Angeles.")
            movie("The Italian Job", 2003, 7.0, "F. Gary Gray", "Mark Wahlberg, Charlize Theron, Edward Norton", "Action, Crime, Thriller", "gold mini revenge", "A crew plots revenge on a former partner who stole gold.")
            movie("Inside Man", 2006, 7.6, "Spike Lee", "Denzel Washington, Clive Owen, Jodie Foster", "Crime, Drama, Mystery", "hostage bank negotiator", "A detective negotiates with bank robbers who have a hidden plan.")
            movie("Baby Driver", 2017, 7.5, "Edgar Wright", "Ansel Elgort, Kevin Spacey, Lily James", "Action, Crime, Drama", "getaway driver music", "A music-loving getaway driver wants out.")
            movie("The Town", 2010, 7.5, "Ben Affleck", "Ben Affleck, Rebecca Hall, Jeremy Renner", "Crime, Drama, Thriller", "bank robbers boston", "A bank robber falls for a hostage.")
            movie("Logan Lucky", 2017, 7.0, "Steven Soderbergh", "Channing Tatum, Adam Driver, Daniel Craig", "Comedy, Crime, Drama", "speedway robbery family", "Two brothers plan a heist at a NASCAR race.")
            movie("Snatch", 2000, 8.3, "Guy Ritchie", "Jason Statham, Brad Pitt, Benicio Del Toro", "Comedy, Crime", "diamond boxing london", "A stolen diamond and an illegal boxing match collide.")
            movie("Lock, Stock and Two Smoking Barrels", 1998, 8.1, "Guy Ritchie", "Jason Statham, Jason Flemyng", "Comedy, Crime", "gang debt london", "Four friends owe a fortune after a rigged card game.")
            movie("The Usual Suspects", 1995, 8.5, "Bryan Singer", "Kevin Spacey, Gabriel Byrne, Benicio Del Toro", "Crime, Drama, Mystery", "lineup keyser soze", "A survivor tells of the criminals behind a massacre.")
            movie("Catch Me If You Can", 2002, 8.1, "Steven Spielberg", "Leonardo DiCaprio, Tom Hanks", "Biography, Crime, Drama", "con forger fbi", "A teen con man is chased by an FBI agent.")
            movie("The Sting", 1973, 8.3, "George Roy Hill", "Paul Newman, Robert Redford", "Comedy, Crime, Drama", "con mob 1930s", "Two grifters set up an elaborate con on a crime boss.")
            movie("Now You See Me", 2013, 7.2, "Louis Leterrier", "Jesse Eisenberg, Mark Ruffalo, Woody Harrelson", "Crime, Mystery, Thriller", "magicians robbery fbi", "Magicians rob banks during their shows.")
            series("Money Heist", 2017, 8.2, "Álex Pina", "Úrsula Corberó, Álvaro Morte", "Action, Crime, Mystery", "professor mint masks", "A mastermind leads a crew in an assault on the Royal Mint.")
            series("Peaky Blinders", 2013, 8.7, "Steven Knight", "Cillian Murphy, Paul Anderson, Helen McCrory", "Crime, Drama", "gang birmingham 1920s", "A gangster family rises in post-war Birmingham.")
            series("Lupin", 2021, 7.5, "George Kay", "Omar Sy, Ludivine Sagnier", "Action, Crime, Drama", "thief paris gentleman", "A gentleman thief takes revenge on a wealthy family.")
            series("Breaking Bad", 2008, 9.5, "Vince Gilligan", "Bryan Cranston, Aaron Paul, Anna Gunn", "Crime, Drama, Thriller", "chemistry teacher drugs", "A chemistry teacher turns to making meth.")
            series("Better Call Saul", 2015, 8.9, "Vince Gilligan", "Bob Odenkirk, Rhea Seehorn", "Crime, Drama", "lawyer new-mexico", "The rise of a small-time lawyer.")
        }
        theme("horror") {
            movie("Hereditary", 2018, 7.3, "Ari Aster", "Toni Collette, Alex Wolff, Milly Shapiro", "Drama, Horror, Mystery", "family grief cult", "A family unravels after their matriarch dies.")
            movie("The Shining", 1980, 8.4, "Stanley Kubrick", "Jack Nicholson, Shelley Duvall", "Drama, Horror", "hotel winter madness", "A writer and his family are isolated in a haunted hotel.")
            movie("Get Out", 2017, 7.8, "Jordan Peele", "Daniel Kaluuya, Allison Williams", "Horror, Mystery, Thriller", "family weekend hypnosis", "A man visits his girlfriend's family with sinister secrets.")
            movie("The Conjuring", 2013, 7.5, "James Wan", "Vera Farmiga, Patrick Wilson, Lili Taylor", "Horror, Mystery, Thriller", "haunted house demonologists", "Paranormal investigators help a family terrorised by a presence.")
            movie("It Follows", 2014, 6.8, "David Robert Mitchell", "Maika Monroe, Keir Gilchrist", "Horror, Mystery", "curse followed", "A young woman is pursued by a relentless entity.")
            movie("The Witch", 2015, 6.9, "Robert Eggers", "Anya Taylor-Joy, Ralph Ineson", "Drama, Fantasy, Horror", "puritan family forest", "A 1630s family is haunted by a witch in the woods.")
            movie("The Exorcist", 1973, 8.1, "William Friedkin", "Ellen Burstyn, Linda Blair", "Horror", "possession priest", "Two priests try to save a possessed girl.")
            movie("Halloween", 1978, 7.7, "John Carpenter", "Jamie Lee Curtis, Donald Pleasence", "Horror, Thriller", "slasher babysitter", "A killer stalks babysitters on Halloween night.")
            movie("The Thing", 1982, 8.2, "John Carpenter", "Kurt Russell, Wilford Brimley", "Horror, Mystery, Sci-Fi", "antarctica shapeshifter paranoia", "Researchers in Antarctica face a shapeshifting alien.")
            movie("A Nightmare on Elm Street", 1984, 7.4, "Wes Craven", "Heather Langenkamp, Robert Englund", "Horror", "dreams killer teenagers", "A killer attacks teens in their dreams.")
            movie("Scream", 1996, 7.4, "Wes Craven", "Neve Campbell, Courteney Cox, David Arquette", "Horror, Mystery", "slasher ghostface", "A masked killer targets a small town.")
            movie("Us", 2019, 6.8, "Jordan Peele", "Lupita Nyong'o, Winston Duke", "Horror, Mystery, Thriller", "doppelgangers family", "A family is attacked by their doubles.")
            movie("Talk to Me", 2022, 7.1, "Danny Philippou", "Sophie Wilde, Alexandra Jensen", "Horror, Thriller", "hand possession teenagers", "Teens use an embalmed hand to talk to the dead.")
            movie("Barbarian", 2022, 7.0, "Zach Cregger", "Georgina Campbell, Bill Skarsgård", "Horror, Mystery, Thriller", "rental house basement", "A double-booked rental holds a terrible secret.")
            movie("The Babadook", 2014, 6.8, "Jennifer Kent", "Essie Davis, Noah Wiseman", "Drama, Horror", "monster grief mother", "A widow and son are haunted by a children's book monster.")
            series("The Haunting of Hill House", 2018, 8.6, "Mike Flanagan", "Michiel Huisman, Carla Gugino", "Drama, Horror, Mystery", "haunted house family", "Siblings confront the house that haunted their childhood.")
            series("Stranger Things", 2016, 8.7, "The Duffer Brothers", "Millie Bobby Brown, Finn Wolfhard, Winona Ryder", "Drama, Fantasy, Horror", "upside-down kids 1980s", "Kids face supernatural forces in a small town.")
            series("Midnight Mass", 2021, 7.7, "Mike Flanagan", "Hamish Linklater, Kate Siegel", "Drama, Horror, Mystery", "island priest faith", "A charismatic priest arrives on an isolated island.")
        }
        theme("animated") {
            movie("Spider-Man: Into the Spider-Verse", 2018, 8.4, "Bob Persichetti", "Shameik Moore, Jake Johnson, Hailee Steinfeld", "Animation, Action, Adventure", "multiverse teenager hero", "Miles Morales meets other Spider-People.")
            movie("Coco", 2017, 8.4, "Lee Unkrich", "Anthony Gonzalez, Gael García Bernal", "Animation, Adventure, Family", "music dead mexico", "A boy enters the Land of the Dead to find his family's secret.")
            movie("Inside Out", 2015, 8.1, "Pete Docter", "Amy Poehler, Phyllis Smith", "Animation, Adventure, Comedy", "emotions mind girl", "A girl's emotions guide her through a big move.")
            movie("Up", 2009, 8.3, "Pete Docter", "Ed Asner, Jordan Nagai", "Animation, Adventure, Comedy", "balloons house adventure", "An old man flies his house to South America.")
            movie("Toy Story", 1995, 8.3, "John Lasseter", "Tom Hanks, Tim Allen", "Animation, Adventure, Comedy", "toys friendship", "A cowboy doll feels threatened by a new space ranger.")
            movie("Your Name", 2016, 8.4, "Makoto Shinkai", "Ryunosuke Kamiki, Mone Kamishiraishi", "Animation, Drama, Fantasy", "body swap comet", "Two teenagers swap bodies across distance and time.")
            movie("Howl's Moving Castle", 2004, 8.2, "Hayao Miyazaki", "Chieko Baisho, Takuya Kimura", "Animation, Adventure, Family", "castle witch war", "A girl cursed by a witch seeks help from a wizard.")
            movie("The Lion King", 1994, 8.5, "Roger Allers", "Matthew Broderick, Jeremy Irons, James Earl Jones", "Animation, Adventure, Drama", "lion pride revenge", "A lion prince flees his kingdom after his father's death.")
            movie("Shrek", 2001, 7.9, "Andrew Adamson", "Mike Myers, Eddie Murphy, Cameron Diaz", "Animation, Adventure, Comedy", "ogre fairy-tale", "An ogre rescues a princess to get his swamp back.")
            movie("How to Train Your Dragon", 2010, 8.1, "Dean DeBlois", "Jay Baruchel, Gerard Butler", "Animation, Action, Adventure", "viking dragon friendship", "A young Viking befriends a dragon.")
            movie("Finding Nemo", 2003, 8.2, "Andrew Stanton", "Albert Brooks, Ellen DeGeneres", "Animation, Adventure, Comedy", "ocean father fish", "A clownfish crosses the ocean to find his son.")
            movie("Ratatouille", 2007, 8.1, "Brad Bird", "Patton Oswalt, Lou Romano", "Animation, Adventure, Comedy", "rat chef paris", "A rat dreams of becoming a chef in Paris.")
            movie("The Incredibles", 2004, 8.0, "Brad Bird", "Craig T. Nelson, Holly Hunter", "Animation, Action, Adventure", "superheroes family", "A family of superheroes must save the world.")
            movie("Soul", 2020, 8.0, "Pete Docter", "Jamie Foxx, Tina Fey", "Animation, Adventure, Comedy", "jazz afterlife purpose", "A jazz musician's soul is separated from his body.")
            movie("Grave of the Fireflies", 1988, 8.5, "Isao Takahata", "Tsutomu Tatsumi, Ayano Shiraishi", "Animation, Drama, War", "war siblings japan", "Two siblings struggle to survive in wartime Japan.")
            series("Arcane", 2021, 9.0, "Christian Linke", "Hailee Steinfeld, Kevin Alejandro", "Animation, Action, Adventure", "sisters city war", "Two sisters are caught in a conflict between two cities.")
            series("Avatar: The Last Airbender", 2005, 9.3, "Michael Dante DiMartino", "Zach Tyler Eisen, Mae Whitman", "Animation, Action, Adventure", "bending avatar war", "A young airbender must master the elements.")
            series("Rick and Morty", 2013, 9.1, "Justin Roiland", "Justin Roiland, Chris Parnell", "Animation, Adventure, Comedy", "scientist multiverse grandson", "A mad scientist drags his grandson through the multiverse.")
            series("BoJack Horseman", 2014, 8.8, "Raphael Bob-Waksberg", "Will Arnett, Amy Sedaris", "Animation, Comedy, Drama", "actor hollywood depression", "A washed-up sitcom star, a horse, tries to find meaning.")
            series("Attack on Titan", 2013, 9.1, "Hajime Isayama", "Yuki Kaji, Yui Ishikawa", "Animation, Action, Adventure", "giants walls survival", "Humanity fights giant man-eating Titans.")
            series("Death Note", 2006, 8.9, "Tetsurō Araki", "Mamoru Miyano, Kappei Yamaguchi", "Animation, Crime, Drama", "notebook detective kira", "A student finds a notebook that kills.")
        }
        theme("true") {
            movie("Oppenheimer", 2023, 8.3, "Christopher Nolan", "Cillian Murphy, Emily Blunt, Robert Downey Jr.", "Biography, Drama, History", "atomic bomb physicist", "The story of the father of the atomic bomb.")
            movie("The Social Network", 2010, 7.8, "David Fincher", "Jesse Eisenberg, Andrew Garfield, Justin Timberlake", "Biography, Drama", "facebook startup lawsuit", "The founding of Facebook and the lawsuits that followed.")
            movie("Spotlight", 2015, 8.1, "Tom McCarthy", "Mark Ruffalo, Michael Keaton, Rachel McAdams", "Biography, Crime, Drama", "journalists church investigation", "Reporters uncover abuse in the Catholic church.")
            movie("Argo", 2012, 7.7, "Ben Affleck", "Ben Affleck, Bryan Cranston, Alan Arkin", "Biography, Drama, Thriller", "hostage iran movie", "A CIA agent fakes a film to rescue hostages.")
            movie("12 Years a Slave", 2013, 8.1, "Steve McQueen", "Chiwetel Ejiofor, Michael Fassbender", "Biography, Drama, History", "slavery freedom", "A free man is kidnapped and sold into slavery.")
            movie("The Wolf of Wall Street", 2013, 8.2, "Martin Scorsese", "Leonardo DiCaprio, Jonah Hill, Margot Robbie", "Biography, Comedy, Crime", "stockbroker fraud excess", "The rise and fall of a stockbroker.")
            movie("Bohemian Rhapsody", 2018, 7.9, "Bryan Singer", "Rami Malek, Lucy Boynton", "Biography, Drama, Music", "queen band freddie", "The story of Freddie Mercury and Queen.")
            movie("Schindler's List", 1993, 9.0, "Steven Spielberg", "Liam Neeson, Ben Kingsley, Ralph Fiennes", "Biography, Drama, History", "holocaust factory", "A businessman saves over a thousand Jews.")
            movie("The King's Speech", 2010, 8.0, "Tom Hooper", "Colin Firth, Geoffrey Rush, Helena Bonham Carter", "Biography, Drama, History", "stammer king therapist", "A king overcomes his stammer with an unorthodox therapist.")
            movie("Moneyball", 2011, 7.6, "Bennett Miller", "Brad Pitt, Jonah Hill", "Biography, Drama, Sport", "baseball statistics", "A manager builds a team with statistics.")
            movie("Hidden Figures", 2016, 7.8, "Theodore Melfi", "Taraji P. Henson, Octavia Spencer, Janelle Monáe", "Biography, Drama, History", "nasa mathematicians", "Three Black mathematicians help launch John Glenn.")
            movie("Dunkirk", 2017, 7.8, "Christopher Nolan", "Fionn Whitehead, Tom Hardy, Cillian Murphy", "Action, Drama, History", "evacuation war beach", "Allied soldiers are evacuated from Dunkirk.")
            movie("1917", 2019, 8.2, "Sam Mendes", "George MacKay, Dean-Charles Chapman", "Drama, War", "trench messenger war", "Two soldiers cross enemy territory to deliver a message.")
            movie("Saving Private Ryan", 1998, 8.6, "Steven Spielberg", "Tom Hanks, Matt Damon, Tom Sizemore", "Drama, War", "d-day rescue war", "Soldiers search for a paratrooper behind enemy lines.")
            series("Chernobyl", 2019, 9.3, "Craig Mazin", "Jared Harris, Stellan Skarsgård, Emily Watson", "Drama, History, Thriller", "nuclear disaster soviet", "The 1986 nuclear disaster and the cover-up.")
            series("The Crown", 2016, 8.6, "Peter Morgan", "Claire Foy, Olivia Colman, Imelda Staunton", "Biography, Drama, History", "queen monarchy britain", "The reign of Queen Elizabeth II.")
            series("Band of Brothers", 2001, 9.4, "Tom Hanks", "Damian Lewis, Ron Livingston", "Drama, History, War", "paratroopers company war", "The story of Easy Company in WWII.")
            series("The Queen's Gambit", 2020, 8.5, "Scott Frank", "Anya Taylor-Joy, Bill Camp", "Drama", "chess prodigy addiction", "An orphan becomes a chess prodigy.")
            series("Narcos", 2015, 8.8, "Chris Brancato", "Wagner Moura, Pedro Pascal", "Biography, Crime, Drama", "escobar cartel colombia", "The rise and fall of Pablo Escobar.")
        }
    }

    // ---- DSL -------------------------------------------------------------------------------

    private class ThemeScope(val theme: ThemeVibe, val sink: MutableList<MediaItem>) {
        fun movie(title: String, year: Int, rating: Double, director: String, cast: String, genres: String, keywords: String, synopsis: String) =
            add(MediaType.MOVIE, title, year, rating, director, cast, genres, keywords, synopsis)

        fun series(title: String, year: Int, rating: Double, creator: String, cast: String, genres: String, keywords: String, synopsis: String) =
            add(MediaType.SERIES, title, year, rating, creator, cast, genres, keywords, synopsis)

        private fun add(type: MediaType, title: String, year: Int, rating: Double, director: String, cast: String, genres: String, keywords: String, synopsis: String) {
            sink += MediaItem(
                id = MediaItem.slug(title, year),
                title = title,
                type = type,
                year = year,
                rating = rating,
                themeId = theme.id,
                genres = genres.splitList(),
                director = director,
                cast = cast.splitList(),
                synopsis = synopsis,
                keywords = keywords.split(' ').filter { it.isNotBlank() },
                colorProfile = ColorMath.profileFor(theme.colors, title),
            )
        }
    }

    private fun MutableList<MediaItem>.theme(id: String, block: ThemeScope.() -> Unit) {
        ThemeScope(themes.first { it.id == id }, this).block()
    }

    private fun String.splitList(): List<String> = split(',').map { it.trim() }.filter { it.isNotEmpty() }
}

/** Tiny color helpers that work on plain ARGB longs (core is platform independent). */
object ColorMath {
    /** Deterministic per-title variation of a theme palette so placeholders don't all look the same. */
    fun profileFor(base: ColorProfile, seed: String): ColorProfile {
        val h = seed.hashCode()
        val shift = ((h ushr 3) % 40 - 20) / 100f // -0.20 .. 0.19
        return ColorProfile(
            primary = adjust(base.primary, shift),
            secondary = adjust(base.secondary, -shift / 2f),
        )
    }

    private fun adjust(argb: Long, amount: Float): Long {
        fun ch(shift: Int): Int {
            val c = ((argb shr shift) and 0xFF).toInt()
            val v = if (amount >= 0) c + (255 - c) * amount else c * (1 + amount)
            return v.toInt().coerceIn(0, 255)
        }
        return (0xFFL shl 24) or (ch(16).toLong() shl 16) or (ch(8).toLong() shl 8) or ch(0).toLong()
    }
}
