# Testat
	
Luis Maier - Matrikelnummer 7096964 |
Christian Reitmeier - Matrikelnummer 2923922

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


## Aufgabe 1a

Implementieren Sie eine Java-L�sung f�r die enter- und exit-Methoden als Erzeuger/Verbraucher-Problem.

### Implememtation - Semaphorenmanagement

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

## Aufgabe 1b

Implementieren Sie eine Java-L�sung f�r die enter- und exit-Methoden mit privaten Semaphoren.

### Implememtation - Semaphorenmanagement

``` java
	void enterLok0() throws InterruptedException {
		mutex.acquire();
		if(next == 0) {
			priv[0].release();
		} else {
			state[0] = WAITING;
		}
		mutex.release();
		priv[0].acquire();
	}
	
	void exitLok0() throws InterruptedException {
		mutex.acquire();
		next = 1;
		if(state[1] == WAITING) {
			state[1] = DRIVING;
			priv[1].release();
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

### Implementation - Beispiel 1

Wie in Aufgabe 1a) wird hier Lok0 etwas schneller gemacht als Lok1. Sobald Lok0 den Abschnitt befahren will, kann diese ihren eigenen privaten Semaphor (priv[0]) freigeben, um ihn danach zu aquiren. Dies liegt daran, dass der Indikator f�r die n�chste Durchfahrt auf 0 gestellt ist, das hei�t, dass Lok1 nicht im Abschnitt ist und Lok0 mit dem Durchfahren dran ist. Wartet dann in der Zwischenzeit Lok1, muss diese dank des Infikators noch warten. Der Wartezustand muss dann durch Lok0 beim Verlassen aufgehoben werden, damit Lok1 fahren kann. 

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

Die Ausgabe ist gleich mit der Ausgabe aus Aufgabe 1a). Die privaten Semaphoren und die Zust�nde sorgen f�r einen Ablauf, der mit dem des E/V-Problems �bereinstimmt. Das Aufwachen der wartenden Lok funktioniert also wie geplant, wenn eine andere Lok den Abschnitt verl�sst.

### Implementation - Beispiel 2

Im zweiten Beispiel ist Lok1 schneller und startet fr�her. Hier muss der Indikator f�r die n�chste Durchfahrt daf�r sorgen, dass trotzdem erst Lok0 f�hrt.

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

Auch wenn Lok1 schneller und fr�her an der Weiche ist, wird Lok0 zuerst durchgelassen. Der Indikator, der die n�chste Lok zeigt, die durchfahren darf, funktioniert. Zu Beginn steht diese auf 0, was Lok0 erm�glicht, den privaten Semaphor f�r die Durchfahrt freizugeben. Lok1 kann das nicht, solange Lok0 nicht den gemeinsamen Abschnitt durchfahren hat und den Indikator auf = 1 gesetzt hat.

### Implementation - Beispiel 3

Wie bereits in Aufgabe 1a) muss getestet werden, ob die Z�ge auch streng abwechselnd fahren, da dies gegeben sein soll - auch wenn ein Zug von der Geschwindigkeit ein Vielfaches schneller ist.

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

Wie in Aufgabe 1a) ist auch hier gegeben, dass die Z�ge abwechselnd fahren. Lok0 muss nur �fter mal etwas l�nger warten. Daf�r sorgt der Indikator, wenn auch wenn Lok0 nach der Durchfaht schnell ankommt und den Abschnitt befahren will, muss Lok0 trotzdem erst auf eine Durchfahrt von Lok1 warten. 
