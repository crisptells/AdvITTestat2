# Testat - 2
	
Luis Maier - Matrikelnummer 7096964 |
Christian Reitmeier - Matrikelnummer 2923922

## Aufgabe

Entwerfen Sie mit TCP einen Server, der Nachrichten speichert und zur Abfrage  uber das Netz bereit hält.
Zum Ablegen einer Nachricht auf dem Server sendet ein Client einen String mit dem folgenden Format an
den Server:

	SAVE beliebig langer String mit abschlie endem Zeilenende

Der Server generiert nach dem Empfang einen neuen geeigneten eindeutigen Schlüssel (als String) und
speichert die Nachricht in einer Datei, wobei der Schl ussel als Dateiname verwendet wird. Danach sendet
der Server den Schlüssel zurück an den Client:

	KEY schluessel

Alle Dateien sollen auf dem Server auf dem Desktop im Verzeichnis ”Messages/”abgespeichert werden, das
Sie vorher schon anlegen sollten.

Zum Abrufen einer Nachricht sendet ein Client einen String:

	GET schluessel

an den Server, der daraufhin  uberpr uft, ob eine entsprechende Datei existiert.
Falls ja, sendet er den Inhalt der Datei an den Client:

	OK dateiinhalt

Anderenfalls sendet er:

	FAILED

Implementieren Sie den Server auf Port 7777 sowie einen Client zum Testen.


## Tipps 

- Implementieren Sie den Server als Non-Persistent Server und passen Sie den Client entsprechend an.
- Verwenden Sie die Filter-Streams PrintWriter und BufferedReader.
- Verwenden Sie split aus der Klasse String zum Zerlegen der Nachrichten.
- Verwenden Sie die Klassen FileReader und FileWriter zum Zugriff auf die Dateien:
- new BufferedReader(new FileReader(filename))
- new PrintWriter(new FileWriter(filename))
- Erzeugen Sie den eindeutigen Schl ussel mithilfe eines Zufallszahlengenerators.


### Beispiel 1

Im ersten Beispiel wird mit SAVE der Text "Hello World!" gespeichert. Daraufhin sollte der Server einen zufälligen 16-Stelligen Schlüssel generieren. Den Text "Hello World!" speichert er in einer Datei im "Messages/" Verzeichniss auf dem Desktop. Der generierte Schlüssel stellt den Dateinamen dar. Ist dieser Vorgang abgeschlossen, schickt der Server dem Client den zuvor generierten Schlüssel zu.


``` 
SAVE Hello World!
KEY 6230216163552060
```
![image](https://user-images.githubusercontent.com/53625452/149752387-e3f518aa-3071-4f95-8e28-2adac162e563.png)

Auf diese Datei kann der Client nun mit dem GET Befehl und dem Schlüssel zugreifen. Der Server sucht im "Messages/" Verzeichniss, ob eine Datei gefunden wird, deren Dateiname mit dem Schlüssel übereinstimmt. Ist das der Fall, wird diese Datei ausgelesen und der Inhalt mit einem Vorangestellten "OK" an den Client zurückgesendet.

``` 
GET 6230216163552060
OK Hello World!
```

### Auswertung - Beispiel 1

Die SAVE-Anfrage des Clients wird vom Server erfolgreich erkannt und bearbeitet. Er generiert einen zufälligen 16-Stelligen Schlüssel und benennt die Datei, die den Text beinhaltet, nach diesem Schlüssel. Diese Datei wird im Ordner "Messages" auf dem Desktop abgespeichert. abgespeichert. Beim Aufruf des Clients durch den GET-Befehl wird die Datei anhand ihres Namens und des Schlüssels, den der Client mitgegeben hat, gefunden. Danach wird der Inhalt ausgelesen und dem Client zurückgeschickt.


### Beispiel 2

Im zweiten Beispiel wird durch eine Fehlerhafte Eingabe der Befehle "SAVE" und "GET" die Message "FAILED - Befehl wurde nicht erkannt" geworfen. Hierbei erkennt der Server den jeweiligen Befehl nicht und führt deswegen auch keinen weiteren Code zum erstellen einer Datei oder eines Schlüssels aus. In den ersten zwei Fällen werden jeweils einmal der SAVE-Befehl und der GET-Befehl falsch geschrieben. Da der Server bei den Befehlen case sensitive ist, erkennt er die Befehle nicht und gibt die Fehlermeldung "FAILED - Befehl wurde nicht erkannt" an den Client zurück. Im dritten Fall wird kein Leerzeichen zwischen dem Befehl und dem Text gesetzt. Hier wird die Fehlermeldung "FAILED - fehlerhafte Struktur" vom Server ausgegeben.


``` 
SaVE Hello World!
FAILED - Befehl wurde nicht erkannt
GeT 6230216163552060
FAILED - Befehl wurde nicht erkannt
SAVEHelloWorld!
FAILED - fehlerhafte Struktur
```

### Auswertung - Beispiel 2

Der Server reagiert auch auf falsche Eingaben. Zum einen wirft er die Fehlermeldung "FAILED - Befehl wurde nicht erkannt", wenn der Befehl falsch geschrieben ist. Zum anderen wird die Fehlermeldung "FAILED - fehlerhafte Struktur" ausgelöst, wenn die Anfrage des Clients nicht den syntaktischen Anforderungen entspricht.


### Beispiel 3

Im dritten Beispiel wird ein Fehlerhafter Schlüssel vom Client mitgegeben, zu dem der Server keine zugehörige Datei findet. Hierbei wird die "FileNotFoundException" ausgelöst und der Server schickt die Antwort "FAILED" an den Client zurück.

```
GET 6230216163552061
FAILED
```

### Auswertung - Beispiel 3

Der Client sendet einen falschen Schlüssel, den es nicht im "Message/" Verzeichniss gibt. Infolgedessen kann der Server auch keine Datei mit dem Namen finden und die "FileNotFoundException" ausgelöst. Daraufhin gibt der Server "FAILED" an den Client zurück.
