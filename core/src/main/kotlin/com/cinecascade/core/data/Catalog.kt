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
