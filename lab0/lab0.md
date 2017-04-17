#### Frågor lab0

## 1.2 Vilka typer bör variablerna vara?
Lämpligen är:
name - String
age - int

## 1.3 Vilka typer bör metoderna returnera?
toString() - returnerar en String
getAge() - returnerar en int
getName() - returnernar en String

## 1.4 Vilka fält/metoder bör vara private, och vilka bör vara public?
Variablerna bör vara private då vi inte vill tillåta direkt access till dessa. Främst för att vi vill ha koll på hur variablerna kan modifieras, om överhuvudtaget.

Dessa ska vara public, då vi vill kunna anropa dessa utifrån klassen
toString()
getAge()
getName()


## 2.3 Måste man anropa toString() explicit?
Nej när man printar ett objekt så ser compilern till att toString metoden invokeras.


