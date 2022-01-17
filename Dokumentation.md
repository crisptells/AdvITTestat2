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


### Implememtation - Server

In der Aufgabe soll ein Non-Persistent Server genutzt werden und der Client dazu angepasst werden...

In der Aufgabe werden zwei Semaphoren benutzt, einen, der mit 1 initialisiert ist (sharedTrack) und einen, der mit 0 initialisiert ist (lock). Der Semaphor, der noch eine Ressource frei hat, wird f�r Lok0 zum Betreten des gemeinsamen Abschnittes genutzt, der andere f�r Lok1. Dies verhindert, dass Lok1 beginnt, bevor Lok0 den ABschnitt befahren konnte.

``` java
	void enterLok0() throws InterruptedException {
		sharedTrack.acquire();
	}
	void enterLok1() throws InterruptedException {
		lock.acquire();
	}
	void exitLok0() {
		lock.release();
	}
	void exitLok1() {
		sharedTrack.release();
	}

```

### Implementation - Beispiel 1

Im ersten Beispiel wird durch das Keyword SAVE ein Text eingegeben...

Im ersten Beispiel ist Lok0 schneller, weshalb Lok0 auch zuerst den gemeinsamen Abschnitt befahren will und bef�hrt. Da in der L�sung aber gefordert ist, dass Lok0 immer zuerst den gemeinsamen Abschnitt bef�hrt, muss der LokThread dies ber�cksichtigen. Daher gibt es einen Semaphore(1, true), den Lok0 aquired, wenn diese den Abschnitt befahren will. Lok1 hat einen anderen Semaphore(0, true), der von Anfang an "voll" ist und der erst durch das Durchfahren von Lok0 freigeschaltet wird. 

``` java
	public static void start() {
		
		Lok l = new Lok();	

		LokThread lok0 = new LokThread(0, l, 1.1D);
		LokThread lok1 = new LokThread(1, l, 1.0D);
		
		lok0.start();
		lok1.start();
		
	}
```

### Ausgabe - Beispiel 1

``` java
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok1 will den geteilten Abschnitt befahren!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 will den geteilten Abschnitt befahren!
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 will den geteilten Abschnitt befahren!
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
```

### Auswertung - Beispiel 1

Lok0 startet zuerst und betritt den gemeinsamen Gleisabschnitt auch zuerst - genau das, was in der Aufgabe auch gefordert war. Da Lok0 etwas schneller ist als Lok1, welche nach Lok0 den gemeinsamen Abschnitt betritt, kommt Lok0 in der Regel zu Beginn immer vor Lok1 an der Weiche an und kann den Abschnitt ohne Probleme betreten. 
Wenn Lok0 den gemeinsamen Gleisabschnitt bef�hrt, kann Lok1 wegen dem "lock"-Semaphor, der mit 0 initialisiert ist, nicht betreten. Erst beim Ausfahren von Lok0 wird der Counter vom "lock"-Semaphor auf 1 gesetzt, was Lok1 das Einfahren in den gemeinsamen Abschnitt erm�glicht. Dabei wird der Counter von "lock" wieder auf 0 gesetzt. Lok0 kann den gemeinsamen Gleisabschnitt zu Beginn betreten, da dieser nicht mit dem "lock"-Semaphor beim Einfahren arbeitet, sondern den "sharedTrack"-Semaphor, der mit 1 initialisiert wird. Lok1 setzt nach dem Durchfahren des gemeinsamen Abschnitts den Counter von diesem wieder auf 1, damit Lok0 wieder einfahren kann. 

### Implementation - Beispiel 2

Im zweiten Beispiel wird durch einen Tippfehler FAILED geworfen...

Im zweiten Beispiel wird Lok1 schneller gemacht und der Thread wird zuerst gestartet. Trotzdem soll gegeben sein, dass Lok0 zuerst den gemeinsamen Abschnitt betritt.

``` java
	public static void start() {
		
		Lok l = new Lok();	

		LokThread lok0 = new LokThread(0, l, 1.0D);
		LokThread lok1 = new LokThread(1, l, 1.1D);

		lok1.start();
		lok0.start();
		
	}
```

### Ausgabe - Beispiel 2

``` java
Lok1 will den geteilten Abschnitt befahren!
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok1 will den geteilten Abschnitt befahren!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok1 will den geteilten Abschnitt befahren!
Lok0 verl�sst den geteilten Abschnitt...
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
```

### Auswertung - Beispiel 2

Im zweiten Beispiel wird sichtbar, wie die Semaphoren "sharedTrack" und "lock" wirklich arbeiten. Lok1 f�hrt schneller und erreicht die Weiche schneller, muss jedoch Lok0 vor lassen. Da Lok1 zum Einfahren den "lock"-Semaphor benutzt, der mit 0 initialisiert wurde, kann Lok1 nicht einfahren, bis Lok0 beim Verlassen den "lock"-Semaphor-Counter auf 1 setzt. Dies hindert Lok1 daran, zu Beginn als erstes den gemeinsamen Gleisabschnitt zu befahren. Danach m�ssen die Loks nur warten, bis der Abschnitt frei ist, damit sie einfahren k�nnen.

### Implementation - Beispiel 3

Im dritten Beispiel ist Lok0 um ein Vielfaches schneller, welches dazu f�hren k�nnte, dass Lok0 mehrfach den gemeinsamen Abschnitt bef�hrt. Dies soll jedoch verhindert werden, da es hei�t, die Strecke soll abwechselnd befahren werden. Ob hier jetzt Lok0 oder Lok1 startet ist egal, Lok0 startet immer als Erste (siehe Beispiele 1 und 2).

``` java
	
	public static void start() {
	
		Lok l = new Lok();	

		LokThread lok0 = new LokThread(0, l, 10.0D);
		LokThread lok1 = new LokThread(1, l, 1.0D);

		lok0.start();
		lok1.start();
		
	}

```

### Ausgabe - Beispiel 3

``` java
Lok0 will den geteilten Abschnitt befahren!
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok1 will den geteilten Abschnitt befahren!
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok1 will den geteilten Abschnitt befahren!
Lok1 f�hrt ein! Choo choo!
Lok1 verl�sst den geteilten Abschnitt...
Lok0 f�hrt ein! Choo choo!
Lok0 verl�sst den geteilten Abschnitt...
Lok0 will den geteilten Abschnitt befahren!
Lok1 will den geteilten Abschnitt befahren!
Lok1 f�hrt ein! Choo choo!

```

### Auswertung - Beispiel 3

Durch die beiden getrennten Semaphoren, k�nnen die Z�ge nur streng abwechselnd den Abschnitt befahren. 


		}
		mutex.release();
	}
	
	void enterLok1() throws InterruptedException {
		mutex.acquire();
		if(next == 1) {
			priv[1].release();
		} else {
			state[1] = WAITING;
		}
		mutex.release();
		priv[1].acquire();
	}
	
	void exitLok1() throws InterruptedException {
		mutex.acquire();
		next = 0;
		if(state[0] == WAITING) {
			state[0] = DRIVING;
			priv[0].release();
		}
		mutex.release();
	}
```

