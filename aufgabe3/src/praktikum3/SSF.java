package praktikum3;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * 
 * @author Vu-Nguyen Bui & Tim Hagemann <br>
 *         IT-Sicherheit Praktikum 3 - JAVA Kryptographie API <br>
 *         Aufgabe 2
 */
public class SSF {
	private PrivateKey prvKey;
	private PublicKey pubKey;
	private SecretKey secretKey;
	private byte[] signatureBytes = new byte[0];
	private AlgorithmParameters parameters;

	public SSF(PrivateKey privateKey, PublicKey publicKey) {
		this.prvKey = privateKey;
        this.pubKey = publicKey;
	}

	public static void main(String[] args) throws Exception {
        SSF ssf = new SSF(getPrivateKey(args[0]), getPublicKey(args[1]));
        ssf.generateAESKey();
		ssf.signatur();
		byte[] result = ssf.encryptFile(args[2]);
		ssf.writeFile(args[3], result);
	}

	private void writeFile(String output, byte[] inputdata) throws IOException {
		byte[] secretKeyEncrypted = encryptKey(secretKey, pubKey);

		DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(output)));
		System.out.println(secretKeyEncrypted.length);
		outputStream.writeInt(secretKeyEncrypted.length);
		outputStream.write(secretKeyEncrypted);
		System.out.println(signatureBytes.length);
		outputStream.writeInt(signatureBytes.length);
		outputStream.write(signatureBytes);
		byte[] algorithmParameterBytes = parameters.getEncoded();
		System.out.println(algorithmParameterBytes.length);
		outputStream.writeInt(algorithmParameterBytes.length);
		outputStream.write(algorithmParameterBytes);
		outputStream.write(inputdata);
		outputStream.close();
	}

	public void signatur() {
		// TODO
		Signature rsaSignature = null;
		try {
			// als Erstes erzeugen wir das Signatur-Objekt
			rsaSignature = Signature.getInstance("SHA256withRSA");
			// zum Signieren benoetigen wir den privaten Schluessel (hier: RSA)
			rsaSignature.initSign(prvKey);
			// Daten fuer die kryptographische Hashfunktion (hier: SHA-256)
			// liefern
			rsaSignature.update(secretKey.getEncoded());
			// Signaturbytes durch Verschluesselung des Hashwerts (mit privatem
			// RSA-Schluessel) erzeugen
			signatureBytes = rsaSignature.sign();
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Keine Implementierung fuer SHA256withRSA!");
		} catch (InvalidKeyException ex) {
			System.out.println("Falscher Schluessel!");
		} catch (SignatureException ex) {
			System.out.println("Fehler beim Signieren der Nachricht!");
		}
	}

	public byte[] encryptKey(SecretKey secretKey, PublicKey key) {
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] first = cipher.update(secretKey.getEncoded());
			return concat(first, cipher.doFinal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public byte[] encryptFile(String inputFile) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
		FileInputStream inputStream = new FileInputStream(new File(inputFile));
		int read;
		byte[] buffer = new byte[512];
		byte[] result = new byte[0];
		while((read = inputStream.read(buffer)) > 0) {
			if(read < 512) {
				result = concat(result, cipher.update(buffer, 0, read));
			} else {
				result = concat(result, cipher.update(buffer));
			}
		}

		result = concat(result, cipher.doFinal());

		parameters = cipher.getParameters();
		return result;
	}

	public void generateAESKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128); // for example
		secretKey = keyGen.generateKey();
	}

	public static PrivateKey getPrivateKey(String fileName) throws Exception {
		DataInputStream is = new DataInputStream(new FileInputStream(fileName));

		int ownerLength = is.readInt();
		is.skipBytes(ownerLength);

		byte[] prvKeyBytes = new byte[is.readInt()];
		is.readFully(prvKeyBytes);
		is.close();

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(prvKeyBytes);
		KeyFactory keyFac = KeyFactory.getInstance("RSA");
		return keyFac.generatePrivate(pkcs8KeySpec);
	}

	public static PublicKey getPublicKey(String fileName) throws Exception {
		DataInputStream is = new DataInputStream(new FileInputStream(fileName));

		int ownerLength = is.readInt();
		is.skipBytes(ownerLength);

		byte[] pubKeyBytes = new byte[is.readInt()];
		is.readFully(pubKeyBytes);
		is.close();

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(x509KeySpec);
	}

	private static byte[] concat(byte[] first, byte[] second) {
		byte[] newArray = new byte[first.length + second.length];
		System.arraycopy(first, 0, newArray, 0, first.length);
		System.arraycopy(second, 0, newArray, first.length, second.length);

		return newArray;
	}
}
