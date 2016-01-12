package kerberos;

/* Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 /* Client-Klasse
 */

import java.util.*;

public class Client extends Object {

	private KDC myKDC; // Konstruktor-Parameter

	private String currentUser; // Speicherung bei Login n�tig
	private Ticket tgsTicket = null; // Speicherung bei Login n�tig
	private long tgsSessionKey; // K(C,TGS) // Speicherung bei Login n�tig

	// Konstruktor
	public Client(KDC kdc) {
		myKDC = kdc;
	}

	public boolean login(String userName, char[] password) {
		long nounce = this.generateNonce();
		TicketResponse response = this.myKDC.requestTGSTicket(userName, "myTGS", nounce);
		if(response == null) {
			System.out.println("Invalid login.");
			return false;
		}
		this.currentUser = userName;
		long myKey = this.generateSimpleKeyFromPassword(password);
		if(!response.decrypt(myKey)) {
			return false;
		}

		if(nounce != response.getNonce()) {
			System.out.println("Invalid noune.");
			return false;
		}

		this.tgsTicket = response.getResponseTicket();
		this.tgsSessionKey = response.getSessionKey();

		return true;
	}

	public boolean showFile(Server fileServer, String filePath) {
		long nounce = this.generateNonce();
		Auth auth = new Auth(this.currentUser, System.currentTimeMillis());
		auth.encrypt(this.tgsSessionKey);
		TicketResponse response = this.myKDC.requestServerTicket(this.tgsTicket, auth, fileServer.getName(), nounce);
		if(response == null) {
			System.out.println("Invalid login.");
			return false;
		}
		if(!response.decrypt(this.tgsSessionKey)) {
			return false;
		}

		if(response.getNonce() != nounce) {
			System.out.println("Invalid nounce");
			return false;
		}

		System.out.println("Got server ticket");
		Auth serverAuth = new Auth(this.currentUser, System.currentTimeMillis());
		serverAuth.encrypt(response.getSessionKey());
		return fileServer.requestService(response.getResponseTicket(), serverAuth, "showFile", filePath);
	}

	/* *********** Hilfsmethoden **************************** */

	private long generateSimpleKeyFromPassword(char[] passwd) {
		// Liefert einen eindeutig aus dem Passwort abgeleiteten Schl�ssel
		// zur�ck, hier simuliert als long-Wert
		long pwKey = 0;
		if (passwd != null) {
			for (int i = 0; i < passwd.length; i++) {
				pwKey = pwKey + passwd[i];
			}
		}
		return pwKey;
	}

	private long generateNonce() {
		// Liefert einen neuen Zufallswert
		long rand = (long) (100000000 * Math.random());
		return rand;
	}
}
