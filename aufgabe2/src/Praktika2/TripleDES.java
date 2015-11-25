package Praktika2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TripleDES {
	File inputFile;
	File outputFile;
	File keyFile;

	public TripleDES(File input, File output, File key) {
		this.inputFile = input;
		this.outputFile = output;
		this.keyFile = key;
	}

	public void encrypt() throws IOException {
		InputStream stream = new FileInputStream(this.inputFile);
		OutputStream output = new FileOutputStream(outputFile);
		// Read 3 des keys + one init vector from keyfile
		DESKey desKey = readKeys();
		byte[] buffer = new byte[8];
		byte[] encryptBuffer = new byte[8];
		// Init all three des algorithms
		DES des1 = new DES(desKey.keys[0]);
		DES des2 = new DES(desKey.keys[1]);
		DES des3 = new DES(desKey.keys[2]);
		// Initialize the encryption buffer with the input vector and triple des
		tripleEncrypt(des1, des2, des3, desKey.iv, encryptBuffer);
		int read;
		while ((read = stream.read(buffer)) > 0) {
			// Apply padding if read less than 8 bytes
			for(int i = read; i < 8; i++) {
				buffer[i] = 0;
			}

			for (int i = 0; i < 8; i++) {
				// XOR message with previous cypher
				buffer[i] = (byte) (buffer[i] ^ (encryptBuffer[i]));
			}

			// Encrypt current cypher result for next iteration
			tripleEncrypt(des1, des2, des3, buffer, encryptBuffer);
			output.write(buffer);
		}
		stream.close();
		output.close();
	}

	public void decrypt() throws IOException {
		InputStream stream = new FileInputStream(this.inputFile);
		OutputStream output = new FileOutputStream(outputFile);
		// Read 3 des keys + one init vector from keyfile
		DESKey desKey = readKeys();
		byte[] buffer = new byte[8];
		byte[] encryptBuffer = new byte[8];
		byte[] decryptBuffer = new byte[8];
		// Init all three des algorithms
		DES des1 = new DES(desKey.keys[0]);
		DES des2 = new DES(desKey.keys[1]);
		DES des3 = new DES(desKey.keys[2]);

		// Initialize the encryption buffer with the input vector and triple des
		tripleEncrypt(des1, des2, des3, desKey.iv, encryptBuffer);
		int read;
		while ((read = stream.read(buffer)) > 0) {
			for(int i = read; i < 8; i++){
				buffer[i] = 0;
			}

			for (int i = 0; i < 8; i++) {
				// XOR read cypher with previous cypher to get the message
				decryptBuffer[i] = (byte) (buffer[i] ^ (encryptBuffer[i]));
			}

			// encrypt current cypher for next iteration
			tripleEncrypt(des1, des2, des3, buffer, encryptBuffer);
			output.write(decryptBuffer);
		}
		stream.close();
		output.close();
	}

	private void tripleEncrypt(DES des1, DES des2, DES des3, byte[] input,
			byte[] output) {
		des1.encrypt(input, 0, output, 0);
		des2.decrypt(output, 0, output, 0);
		des3.encrypt(output, 0, output, 0);
	}

	private DESKey readKeys() throws IOException {
		InputStream streamKey = new FileInputStream(this.keyFile);
		DESKey desKey = new DESKey();
		byte[] buffer = new byte[8];
		// Read three times 8 bytes for 3 DES keys
		for (int i = 0; i < 3; i++) {
			streamKey.read(buffer);
			System.arraycopy(buffer, 0, desKey.keys[i], 0, 8);
		}

		// Read one more 8 bytes for init vector
		streamKey.read(buffer);
		System.arraycopy(buffer, 0, desKey.iv, 0, 8);
		streamKey.close();

		return desKey;
	}

	public static void main(String[] args) throws IOException {
		String action = args[3];
		File input = new File(args[0]);
		File inputKey = new File(args[1]);
		File output = new File(args[2]);
		TripleDES tripleDes = new TripleDES(input, output, inputKey);

		if (action.equals("encrypt")) {
			tripleDes.encrypt();
		} else if (action.equals("decrypt")) {
			tripleDes.decrypt();
		}
	}
}
