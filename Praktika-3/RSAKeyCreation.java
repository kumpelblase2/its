import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 
 * @author Vu-Nguyen Bui & Tim Hagemann <br>
 *         IT-Sicherheit Praktikum 3 - JAVA Kryptographie API <br>
 *         Aufgabe 1
 */
public class RSAKeyCreation {
	private static String ownerName;
	private static KeyPair keyPair = null;
	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	private static byte[] publicKeyBytes;
	private static byte[] privateKeyBytes;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		ownerName = "KMueller";

		generateKeyPair();

		writePubAndPrvFiles(ownerName);

		System.out.println(ownerName.length());
		System.out.println(ownerName.getBytes());

		System.out.println(publicKeyBytes.length);
		System.out.println(publicKey.getEncoded());

		System.out.println(privateKeyBytes.length);
		System.out.println(privateKey.getEncoded());

	}

	public static void generateKeyPair() throws NoSuchAlgorithmException {
		// Generate public/private key pair
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(2048);
		keyPair = gen.generateKeyPair();

		// Get the private and public key
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();

		// Get bytes of each of the keys
		privateKeyBytes = privateKey.getEncoded();
		publicKeyBytes = publicKey.getEncoded();
	}

	private static void writePubAndPrvFiles(String ownerName) throws IOException {
		// Create public and private file
		DataOutputStream pubFile = new DataOutputStream(new FileOutputStream(ownerName + ".pub"));
		DataOutputStream prvFile = new DataOutputStream(new FileOutputStream(ownerName + ".prv"));

		// Data for the public key file
		pubFile.writeInt(ownerName.length());
		pubFile.write(ownerName.getBytes());
		pubFile.writeInt(publicKeyBytes.length);
		pubFile.write(publicKeyBytes);

		// Data for the private key file
		prvFile.writeInt(ownerName.length());
		prvFile.write(ownerName.getBytes());
		prvFile.writeInt(privateKeyBytes.length);
		prvFile.write(privateKeyBytes);

		// Close output streams
		pubFile.close();
		prvFile.close();
	}

}
