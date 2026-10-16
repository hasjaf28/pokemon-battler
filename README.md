# Pokémon Battler 

Min andra inlämningsuppgift i Javaprogrammering, grund. Det är en konsolapplikation som jag har byggt vidare 
på Pokédexen från inlämningsuppgift 1. Man kan hantera Pokémon och utmana slumpmässiga motståndarpokémon 
i en strid där spelaren och motståndaren turas om.

## Vad programmet kan göra:

- Visa alla Pokémoner i en lista.
- Lägga till en ny Pokémon.
- Redigera en Pokémon: namn, type, maxHp, attacker.
- Ta bort en Pokémon.
- Spara hela listan med Pokémoner till en JSON-fil.
- Ladda tillbaka Pokémonslista från den sparade filen.
- Återställa till startdata (6 Pokémon som finns från start).
- Starta en strid mot en slumpmässig motståndarpokémon.
- Visa stridsresultat (vinster, förluster, ratio per Pokémon, mest använda attack och mest använda Pokémon).
- Avsluta programmet.

## Hur man kör programmet

Öppna projektet i IntelliJ eller annat IDE som stödjer Maven. Leta upp `Main.java` i projektet:

`pokemon-battler/src/main/java/se/systementor/battler/ui/Main.java`

Öppna `Main.java` och klicka på den gröna playknappen.

## Exempel på hur programmet ser ut 

```
=== Välkommen till POKÉMON BATTLER ===
1. Visa alla Pokémoner
2. Lägga till ny Pokémon
3. Redigera Pokémon
4. Ta bort Pokémon
5. Spara till fil
6. Ladda från fil
7. Återställ till seedad data
8. Starta strid
9. Stridsresultat
10. Avsluta
Välj: 1
1. Pikachu      | Type: ELECTRIC | HP: 200/200 | Attacker: 2
2. Charizard    | Type: FIRE     | HP: 250/250 | Attacker: 2
3. Arceus       | Type: NORMAL   | HP: 310/310 | Attacker: 2
4. Gyarados     | Type: WATER    | HP: 270/270 | Attacker: 2
5. Snorlax      | Type: GRASS    | HP: 390/390 | Attacker: 2
6. Thanos       | Type: FIRE     | HP: 450/450 | Attacker: 2
...
Välj: 8
Vilken Pokémon vill du använda för striden? (1-6): 6
Striden börjar! Din Thanos möter motståndarens Snorlax!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 1
Thanos använder Power Stone!
Attacken var supereffektiv!
Thanos har nu 450/450 HP!
Snorlax har nu 220/390 HP!
Snorlax använder Normal Impact!
Snorlax har nu 220/390 HP!
Thanos har nu 383/450 HP!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 1
Thanos använder Power Stone!
Attacken var supereffektiv!
Thanos har nu 383/450 HP!
Snorlax har nu 24/390 HP!
Snorlax använder Snuvan!
Attacken missade!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 2
Thanos använder Soul Catcher!
Attacken missade!
Snorlax använder Snuvan!
Snorlax har nu 24/390 HP!
Thanos har nu 305/450 HP!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 2
Thanos använder Soul Catcher!
Thanos har nu 305/450 HP!
Snorlax har nu 0/390 HP!
Snorlax har svimmat!
Du vann striden! Din Thanos vann över motståndarens Snorlax!
...
Välj: 9
===== Stridsresultat =====
Vinster: 1
Förluster: 0
Mest använda attack: Power Stone
Mest använda Pokémon: Thanos

Ratio per Pokémon:
Thanos: 100%
...
Utskriften är förkortad med `...` där menyn upprepas mellan menyval. 
```

## Projektstruktur 

```
pokemon-battler/src/main/java/se/systementor/battler/

model/
- Attack.java               (abstrakt basklass för attacker)
- DamageAttack.java         (attack som gör skada)
- HealAttack.java           (attack som läker Pokémon)
- Pokemon.java              (datamodellen för Pokémon)
- Type.java                 (enum med Pokémontyper och typeffektivitet)
- BattleStats.java          (statistik: vinster, förluster, attacker och Pokémon)

repository/ 
- PokemonRepository.java    (sparar och laddar Pokédexen som JSON)
- StatsRepository.java      (sparar och laddar statistik som JSON)
- AttackData.java           (hjälpklass för JSON, en attack)
- PokemonData.java          (hjälpklass för JSON, en Pokémon)

service/ 
- BattleService.java        (Stridslogik: träff, skada, crit, logg och turer)

ui/ 
- Main.java                 (huvudmenyn)
- BattleMenu.java           (stridsmenyn) 
- InputHelper.java          (säker inläsning från tangentbordet)
```

## OOP-val 

Jag gjorde Attack abstrakt eftersom man aldrig skapar en "vanlig" attack utan alltid en specifik
sort. Både DamageAttack och HealAttack ärver från Attack med `extends`. Attack har en abstrakt metod 
`execute(Pokemon attacker, Pokemon defender, int amount)` som subklasserna måste skriva själva. 
DamageAttack gör skada på motståndaren och HealAttack läker den som attackerar, samma metodanrop men 
olika saker händer. BattleService anropar `attack.execute(...)` och vet inte vilken typ av attack det är. 
Utan arv hade alla attacktyper behövt ligga i samma klass med if-satser och den hade blivit större för 
varje ny attacktyp. 

Jag har delat upp koden i fyra paket: model, repository, service och ui. Varje paket har ett eget ansvar, 
model håller dataklasserna, repository sparar och laddar filer, service har stridslogiken och ui hanterar 
menyer och inläsningen. Detta gör koden lättare att hitta i och att ändra eftersom varje del gör en sak. 
Main innehåller t.ex. ingen stridslogik. BattleService har reglerna för en enskild tur (träffsäkerhet, skada och logg), 
medan BattleMenu styr flödet och registrerar vinst eller förlust. 

För JSON-persistensen finns två hjälpklasser i paketet repository, `AttackData` och `PokemonData` eftersom 
`Attack` är abstrakt och Jackson kan inte skapa en abstrakt klass från JSON. Repositoryklassen översätter mellan 
de riktiga klasserna och dessa enkla dataobjekt både vid sparning och inläsning. Fälten är public istället för 
private med getters och setters. Det fungerar här eftersom klasserna bara flyttar data mellan Java och JSON. 
Det finns inget att skydda. Modellklasserna gör tvärtom. `Pokemon` och `BattleStats` har private fält och 
setters som validerar eftersom där finns regler som måste hållas. `Pokemon` begränsar HP så det aldrig går under 
0 eller över maxHp och `BattleStats` avvisar negativa vinster och förluster. Med publicfält hade ogiltiga 
värden kunnat hamna rakt in. Skillnaden är om klassen har regler att skydda eller bara flyttar data. 

Typeffektivitet och kritiska träffar fungerar på samma sätt. `Attack` har två vanliga metoder `getMultiplier(...)` 
och `getCritMultiplier()` som båda returnerar 1.0 i basklassen. `DamageAttack` skriver över båda, ena slår upp attackens 
typ mot försvararens typ och den andra returnerar 2.0 vid en kritisk träff. `HealAttack` rörs inte av dem och ärver 
1.0 och påverkas därför varken av typeffektivitet eller crit. `BattleService` anropar metoderna på `Attack` utan 
att veta vilken sort det är och slipper på så sätt specialfall för läkning. 


## Stridssystem 

Striden går till så att spelaren börjar och sen turas spelaren och motståndaren (cpu) om. Varje tur väljer spelaren 
en attack från menyn och motståndarens attack väljs slumpmässigt. Träffsäkerheten avgör om attacken träffar eller inte 
och om attacken träffar minskar HP för den som blev attackerad. Striden fortsätter tills ena sidan har 0 HP kvar. 

### Skadeformel 

skada = basskada * slumpfaktor / 100 * typmultiplikator * critmultiplikator

Slumpfaktorn är ett tal mellan 85 och 100 och slumpas fram med hjälp av `random.nextInt(16) + 85`. Typmultiplikatorn 
kommer från attackens typ mot försvararens typ. Typmultiplikatorn blir 2.0 när attacken är supereffektiv, 0.5 när den 
inte är särskilt effektiv och 1.0 i normala fall. Critmultiplikatorn är oftast 1.0 men blir 2.0 vid kritiska träffar 
vilket händer 1 gång av 16. Efter det gör programmet om talet till int och golvar på 1 med `Math.max`. 

Exempel utan typeffektivitet eller crit: basskada 90 och slumpfaktor 90 ger 90 * 90 / 100 = 81 i skada. 

Exempel med typeffektivitet och crit: basskada 90 och slumpfaktor 90 ger 90 * 90 / 100 = 81 i skada vilket med 
typeffektivitet (2x) blir 162. Om det dessutom blir crit (2.0) blir skadan 324.  

### Turordning 

Spelaren attackerar alltid först varje runda. Efter spelarens attack kollar programmet om motståndaren har svimmat 
eller inte och om motståndaren har svimmat får den inte attackera och avslutas striden. Om motståndaren fortfarande 
lever attackerar den tillbaka. Sedan kollar programmet om spelaren har svimmat eller inte. Striden fortsätter tills en 
av dem svimmar. 

Jag valde Alternativ A (spelaren attackerar först) eftersom den var enkel och förutsägbar. Spelaren vet alltid att 
den börjar och spelet har ingen speedegenskap som skulle kunna avgöra turordningen på ett rättvist sätt. 
Alternativ B var att Pokémon med högst maxHp skulle börja, men jag valde bort det för att maxHp handlar om hur mycket 
stryk en Pokémon tål och inte vem som är snabbast. 

## Kända begränsningar 

- Om en Pokémon bara har läkande attacker kan striden pågå för evigt eftersom den läker bort varje attack som 
motståndaren gör. 
- Om en rad i pokemon.json är trasig hoppas den raden över när filen laddas och raden försvinner helt nästa gång 
man sparar till fil. 
- Om man avbryter en strid med Ctrl+D sparas den skadade HP:n eftersom återställning till maxHP bara sker efter 
avslutad strid. 
- Det finns ingen gräns för hur långa ett namn får vara, så listan i "Visa alla Pokémoner" kan hamna snett.
- Avgörandet av vem som vann och registreringen av vinster och förluster ligger i BattleMenu (ui) istället för i 
BattleService (service). Själva stridsloopen ligger med rätta i BattleMenu (ui) eftersom den läser inmatningen 
från spelaren. 
- HealAttack (model) använder samma fält (baseDamage) för läkning som DamageAttack använder för skada vilket kan 
vara lite förvirrande. 
- Om pokemon.json är trasig eller inte går att läsa startar programmet med tom Pokédex och filen skrivs över 
när man avslutar. 
- Statistiken hänger ihop med Pokémonens namn. Byter man namn ligger den gamla statistiken kvar, och två 
Pokémon med samma namn delar statistik. 

## VG-krav

Alla fyra VG-krav är implementerade: 

- **Typeffektivitet:** Tabellen ligger i `Type.damageMultiplier(...)` och `DamageAttack` använder den via 
`getMultiplier(...)` medan `HealAttack` ärver 1.0 så att läkningen inte påverkas. Loggen visar "supereffektiv" vid 
2.0 och "inte särskilt effektiv" vid 0.5. Vid 1.0 skrivs inget. 
- **Kritiska träffar:** `BattleService` har 1 av 16 chans att dubbla skadan. När det händer visas "Attacken var 
kritisk!" i loggen.
- **JSON-persistens:** Allt sparas som JSON med Jackson. `PokemonRepository` sköter `pokemon.json` och 
`StatsRepository` sköter `stats.json`. `AttackData` och `PokemonData` finns som mellanhänder vid sparning och 
inläsning eftersom `Attack` är abstrakt. 
- **Persistent statistik:** `BattleStats` sparar totala vinster och förluster, vinster och förluster per Pokémon 
och användning per attack. Statistiken finns kvar mellan körningar och visas i menyval 9 (mest använda attack, 
mest använda Pokémon och ratio per Pokémon).

## Extrainfo 

Vid första körningen finns ingen sparad fil och programmet skapar 6 Pokémon som startdata. Efter att programmet har 
körts sparas datan i `pokemon.json` i arbetsmappen och läses igen när man startar programmet på nytt. Efter varje 
strid sparas resultatet i `stats.json` i samma mapp. Båda filerna är git-ignorerade via `.gitignore` och ska 
inte checkas in i repot. 

När en strid är avslutad läks spelarens Pokémon tillbaka till maxHp, därför visar listan full HP efter en avslutad
strid. 