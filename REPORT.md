# Verslag
<!--
Clearly describe the technical design: how is the functionality implemented in your code? This should be like your DESIGN.md but updated to reflect the final application. First, give a high level overview, which helps us navigate and understand the total of your code (which components are there?).
Second, go into detail, and describe the modules/classes/functions and how they relate.
-->

## Inleiding
De app DartsWorld is bedoeld voor mensen die graag naar darts willen kijken, maar
dat niet kunnen. Deze app laat namelijk de livescores zien als er darts is. Verder
is deze app ook geschikt voor kijkers, aangezien er veel spelers- en toernooi-informatie
te vinden is.

Het homescherm|
:-------------------------:|
![](/screenshots/Dag13.1.png) |

## Technische Design
De app bestaat uit 10 activities, die allemaal een basis, genaamd BaseActivity,
hebben. Er is ook een Helper-class, die enige functies bevat die meermaals
gebruikt worden. Verder zijn er nog diverse AsyncTasks en structs. In totaal zijn
er 21 classes.

#### BaseActivity
- onBackPressed: een terugknop die ook werkt voor de navigatie.
- setUpBars: functie die de toolbar en het Hamburger-menu gereed maken.

#### LauncherActivity
Wanneer je de app opstart, wordt de LauncherActivity aangeroepen. Deze toont enkel
2.5 seconden lang een splash screen. Vervolgens stuurt deze je naar de MainActivity.
- showSplashScreen.

#### MainActivity
De MainActivity toont livescores indien er live wedstrijden zijn. Als dit niet het
geval is, dan staat er 'No live matches' bovenaan. Aangezien er nooit veel wedstrijden
tegelijk zijn en het laden van de data zeer snel gaat, was het niet nodig om de
data in de LauncherActivity te laden en in een mee te geven.<br>
De wedstrijden worden verwerkt met de Match class.
- refreshActivity: functie die eens in de refreshTime de livescores opnieuw laadt.
De refreshActivity-functie laadt de data in m.b.v. de LiveScoreAsyncTask en de
HttpRequestHelper.
- fetchLiveScore: deze functie verwerkt de livescores en maakt een ArrayList van
wedstrijden. Deze lijst wordt meegegeven aan de MatchListAdapter, die uiteindelijk
bepaalt hoe de wedstrijden weergegeven moeten worden op het scherm.
- retrievePlayerInfo: Zet de spelersnaam om in een versie die gebruiksvriendelijk
is voor Firebase. Uiteindelijk wordt deze naam doorgestuurd naaar een functie
loadPlayerInfo in de Helper-class.
- tournamentInfoClick: Haalt de toernooinaam op en stuurt deze vervolgens door
naar de Helper-functie existsTournamentInfo.

#### CalendarActivity
De eerste optie in het menu is de Kalender. Hierin krijg je een kalender te zien.
Je kan naar de gewenste datum gaan om te kijken wat voor wedstrijden er op die dag
gespeeld worden/werden.
- dateListener: Als er op een datum wordt geklikt, past deze functie de datum zo
aan, dat de data met een bepaalde link met de SofaScore API m.b.v. de functie
loadData opgehaald kan worden.
- loadData: Haalt de scores van de datum op met de DateAsyncTask.
- startDateActivity.

#### DateActivity
Hiermee zie je alle wedstrijden van een bepaalde datum. In het groen staat wie
er gewonnen heeft. Indien er een wedstrijd bezig is, dan zie je deze ook. Wanneer
je op een speler klikt, word je verwezen naar de PlayerActivity.
- processData: Verwerkt de data en toont het met een MatchListAdapter op het scherm.
Bestaat uit de functie getMatches.
- getMatches: Maakt een lijst met alle wedstrijden van een bepaalde datum.
- setMatches: Bepaalt welke wedstrijden uit de lijst geldig zijn en hoe ze er
qua tekst uit moeten zien.
- retrievePlayerInfo: Zie MainActivity.
- tournamentInfoClick: Zie MainActivity.

#### PlayersActivity
Dit is de tweede optie in het Hamburger-menu. Hier zie je een lijst, alfabetisch
gesorteerd op achternaam, met alle in Firebase ingevoerde spelers. Wanneer je op
een speler klikt, word je ook gestuurd naar de PlayerActivity.
- getPlayerInfo.
- setPlayerInfo.
- setListener: Als er op een speler geklikt wordt, ga je naar de PlayerActivity.

#### PlayerActivity
Hierin zie je de spelersinformatie van één speler, die opgehaald wordt uit Firebase.
- getFromDB: Haalt de speler op uit de database. Deze wordt op het scherm getoond
met de PropertyListAdapter.
- setPlayerName.
- setNationImage.
- processPlayerInfo: Bepaalt wat voor spelersinformatie er op een bepaalde manier
op het scherm moet komen.

#### RankingActivity
Hierin zie je een lijst met spelers en hun ranking volgens de PDC Order of Merit.
De werking is ongeveer hetzelfde als de PlayersActivity.
- getPlayerInfo: Hierin wordt de ranking lijst gemaakt. Deze wordt op het scherm
getoond m.b.v. de RankingListAdapter.
- setPlayerClickListener.

#### TournamentsActivity
Deze activity bestaat uit een lijst met alle grote toernooien.
- getTournaments: Slaat alle toernooien uit Firebase op in een lijst.
- setTournaments: Laat een lijst met de toernooien op het scherm zien.
- setListener: Als je op een toernooi klikt, ga je naar de TournamentActivity.
- startPlayerActivity.

#### TournamentActivity
Hierin wordt er informatie over één toernooi getoond.
- getTournamentInfo: Haalt toernooi-informatie op uit Firebase en stopt het in
een Tournament class.
- setTournamentInfo: Verwerkt de toernooi-informatie en toont het met de
PropertyListAdapter op het scherm.
- setTournamentImage.
- setListener: Als je op 'established' of 'defending champion' klikt, laat deze
functie alle kampioenen tot nu toe van het toernooi op de lijst zien.

#### InfoActivity
Hier staat alleen maar wat informatie in.

#### Helper
Deze class bevat functies die in meerdere activities gebruikt worden.
- navigateTo: Beheert de navigatie van het Hamburger-menu.
- isConnectedToInternet.
- displayAlertDialog: Laat een pop-up zien als er geen actieve internetverbinding is.
- existsTournamentInfo: Controleert of er toernooi-informatie van een ingevoerd
toernooi in Firebase te vinden is.
- tournamentClick: start de TournamentActivity met de ingevoerde toernooinaam.
- loadPlayerInfo: Controleert of er spelersinformatie van een ingevoerde speler
in Firebase te vinden is.
- startPlayerActivity.

## Uitdagingen tijdens het ontwikkelen van de app

#### Algemeen
Ten eerste ben ik veel nieuwe informatie tegengekomen. Niet alles daarvan heb ik
gebruikt. Ik ben te weten gekomen hoe fragments werken. Deze heb ik echter niet
gebruikt, aangezien ik niet wilde dat er diverse achtergrondtaken open bleven,
ik wilde alle opties in nieuwe activities openen. Hieronder de grootste uitdagingen:

#### Firebase
Verder weet ik nu veel meer over Firebase en hoe ik ermee om moet gaan. Vooral in
het begin gaf Firebase veel problemen, eerst was de database corrupt, daarna werd
niet altijd alles ververst, maar uiteindelijk is toch nog alles goed gekomen.
Uiteindelijk vind ik Firebase nuttig om accounts in aan te maken, maar voor
databases geef ik toch de voorkeur aan een online site, aangezien de SofaScore
data veel gemakkelijker binnengehaald werden.

#### Chat
De eerste twee weken was ik van plan om een chat toe te voegen aan de app.
Uiteindelijk heb ik dit idee laten varen, aangezien deze chat niet noodzakelijk
is voor mijn app. Dit was een verstandig besluit, aangezien een chat eigenlijk
ook een losse applicatie zou kunnen zijn.

#### Toernooien
Na de eerste presentatie werd er als feedback gegeven dat ik wat met toernooien
moest doen. Het was voorheen namelijk bijvoorbeeld erg onduidelijk wanneer er
precies toernooien waren. Verder was er geen informatie over zo een toernooi
bekend. Dit heb ik opgelost door zelf nog een kleine database met toernooi-informatie
te maken. Dit was eerst niet mijn plan, maar het heeft mijn app wel beter/bruikbaarder
gemaakt. Hierdoor kan je namelijk niet per wedstrijd wat meer informatie te weten
komen over de speler, maar ook over het toernooi wat gaande is.

## Als ik meer tijd had...
Als ik meer tijd zou hebben, dan had ik de data ergens anders online gezet. Verder
had ik dan nog een manier ontwikkeld om per maand te kijken welke toernooien er zijn,
en vervolgens in een lijst eronder de toernooien weer te geven. Als er meerdere
toernooien zijn (want zeer zelden voorkomt), dan wordt nu maar 1 toernooinaam
weergegeven. Ik ben wel tevreden met de classes en het uiterlijk van de app.
Met name op de PDC Order of Merit en op de scores per dag ben ik trots.
Ik zou wel meer functionaliteit willen toevoegen, maar ik ben tevreden met de
manier hoe ik heb geprogrammeerd. Daar zou ik niet veel aan veranderen. Wel zou
ik nog een paar functies dubbele code static maken, zoals retrievePlayerInfo in
MainActivity. Bettercodehub geeft vanwege de kortheid niet aan dat dit een
duplicaat is. Ik had helaas geen tijd meer om mijn code nog verder op te schonen.
Een aantal functies konden namelijk wel korter. Wel heb ik alle duplicaten verwijderd.
