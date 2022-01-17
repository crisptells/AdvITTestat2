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

Auswertung...


### Implementation - Beispiel 2

Im zweiten Beispiel wird durch einen Tippfehler FAILED geworfen...


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

Auswertung...

### Implementation - Beispiel 3

drittes Beispiel...

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

Auswertung...


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

