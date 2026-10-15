# Pokémon Battler 

Min andra inlämningsuppgift i Javaprogrammering, grund. Det är en konsolapplikation som jag har byggt vidare 
på Pokédexen från inlämningsuppgift 1. Man kan hantera Pokémon och utmana slumpmässiga motståndarpokémon 
i en strid där spelaren och motståndaren turas om.

## Vad programmet kan göra:

- Visa alla Pokémoner i en lista.
- Lägga till en ny Pokémon.
- Redigera en Pokémon: namn, type, maxHp, attacker.
- Ta bort en Pokémon.
- Spara hela listan med Pokémoner till en textfil.
- Ladda tillbaka Pokémonslista från den sparade filen.
- Återställa till startdata (6 Pokémon som finns från start).
- Starta en strid mot en slumpmässig motståndarpokémon.
- Visa stridsresultat (vinster och förluster).
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
Striden börjar! Din Thanos möter motståndarens Gyarados!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 2
Thanos använder Soul Catcher!
Thanos har nu 450/450 HP!
Gyarados har nu 194/270 HP!
Gyarados använder Water splash!
Gyarados har nu 194/270 HP!
Thanos har nu 391/450 HP!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 1
Thanos använder Power Stone!
Thanos har nu 391/450 HP!
Gyarados har nu 94/270 HP!
Gyarados använder Water splash!
Gyarados har nu 94/270 HP!
Thanos har nu 327/450 HP!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 1
Thanos använder Power Stone!
Thanos har nu 327/450 HP!
Gyarados har nu 7/270 HP!
Gyarados använder Ronin!
Gyarados har nu 7/270 HP!
Thanos har nu 239/450 HP!
1. Power Stone
2. Soul Catcher
Välj Attack (1-2): 2
Thanos använder Soul Catcher!
Thanos har nu 239/450 HP!
Gyarados har nu 0/270 HP!
Gyarados har svimmat!
Du vann striden! Din Thanos vann över motståndarens Gyarados!
...
Välj: 9
===== Stridsresultat =====
Vinster: 5
Förluster: 0
```

## Projektstruktur 

```
pokemon-battler/src/main/java/se/systementor/battler/

model/
- Attack.java               (abstrakt basklass för attacker)
- DamageAttack.java         (attack som gör skada)
- HealAttack.java           (attack som läker Pokémon)
- Pokemon.java              (datamodellen för Pokémon)
- Type.java                 (enum med Pokémontyper)
- BattleStats.java          (vinster och förluster)

repository/ 
- PokemonRepository.java    (spara och ladda Pokédexen)
- StatsRepository.java      (spara och ladda statistik)

service/ 
- BattleService.java        (Stridslogik: träff, skada, logg och turer)

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
olika saker händer. BattleService anropar bara `attack.execute(...)` och vet inte vilken typ av attack 
det är. Utan arv hade alla attacktyper behövt ligga i samma klass med if-satser och den hade blivit 
större för varje ny attacktyp. 

Jag har delat upp koden i fyra paket: model, repository, service och ui. Varje paket har ett eget ansvar, 
model håller dataklasserna, repository sparar och laddar filer, service har stridslogiken och ui hanterar 
menyer och inläsningen. Detta gör koden lättare att hitta i och att ändra eftersom varje del gör en sak. 
Main innehåller t.ex. ingen stridslogik. BattleService har reglerna för en enskild tur (träffsäkerhet, skada och logg), 
medan BattleMenu styr flödet och registrerar vinst eller förlust. 

## Stridssystem 

Striden går till så att spelaren börjar och sen turas spelaren och motståndaren (cpu) om. Varje tur väljer spelaren 
en attack från menyn och motståndarens attack väljs slumpmässigt. Träffsäkerheten avgör om attacken träffar eller inte 
och om attacken träffar minskar HP för den som blev attackerad. Striden fortsätter tills ena sidan har 0 HP kvar. 

### Skadeformel 

skada = basskada * slumpfaktor / 100

Slumpfaktorn är ett tal mellan 85 och 100 och slumpas fram med hjälp av `random.nextInt(16) + 85`. Sen tar jag 
`Math.max(1, damage)` så att skadan alltid blir minst 1. 

Exempel: basskada 90 och slumpfaktor 90 ger 90 * 90 / 100 = 81 i skada. 

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
- Om en rad i pokemon.txt är trasig hoppas den raden över när filen laddas och raden försvinner helt nästa gång 
man sparar till fil. 
- Om man avbryter en strid med Ctrl+D sparas den skadade HP:n eftersom återställning till maxHP bara sker efter 
avslutad strid. 
- Det finns ingen gräns för hur långa ett namn får vara, så listan i "Visa alla Pokémoner" kan hamna snett.
- Avgörandet av vem som vann och registreringen av vinster och förluster ligger i BattleMenu (ui) istället för i 
BattleService (service). Själva stridsloopen ligger med rätta i BattleMenu (ui) eftersom den läser inmatningen 
från spelaren. 
- HealAttack (model) använder samma fält (baseDamage) för läkning som DamageAttack använder för skada vilket kan 
vara lite förvirrande. 

## VG-krav

Inga VG-krav är implementerade i den här versionen. Följande VG-krav är planerade att implementeras: 

- Typeffektivitetstabell (Fire->Grass 2x, Water->Fire 2x, Grass->Water 2x, Electric->Water 2x, Fire->Water 0.5x, 
 Water->Grass 0.5x, Grass->Fire 0.5x, Normal 1x). 
- Kritiska träffar (1/16 chans som dubblar skadan och syns i loggen). 
- JSON-persistens via Jackson. 
- Persistent statistik per Pokémon och mest använda attack. 

Det som redan finns är totala vinster och förluster som sparas mellan körningar och visas via menyval 9 
(Stridsresultat).

## Extrainfo 

Vid första körningen finns ingen sparad fil och programmet skapar 6 Pokémon som startdata. Efter att programmet har 
körts sparas datan i `pokemon.txt` i arbetsmappen och läses igen när man startar programmet på nytt. Efter varje 
strid sparas resultatet i `stats.txt` i samma mapp. Båda filerna är git-ignorerade via `.gitignore` och ska inte 
checkas in i repot. 

När en strid är avslutad läks spelarens Pokémon tillbaka till maxHp, därför visar listan full HP efter en avslutad
strid. 