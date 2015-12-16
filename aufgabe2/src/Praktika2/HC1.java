package Praktika2;

import java.io.*;

/**
 * Created by tim on 17.11.15.
 */
public class HC1 {

    private final LCG key;
    private final File cryptFile;

    public HC1(long key, File cryptFile) {
        //Init LCG with specific key to ensure same random numbers get generated for same key.
        this.key = new LCG(key);
        this.cryptFile = cryptFile;
    }

    public void crypt(File inOutput) throws IOException {
        InputStream stream = new FileInputStream(this.cryptFile);
        OutputStream output = new FileOutputStream(inOutput);
        byte[] buffer = new byte[4];
        int read = 0;
        while((read = stream.read(buffer)) > 0) {
            //Add padding if read less than 8 bytes.
            for(int i = read; i < 4; i++) {
                buffer[i] = 0;
            }

            // Generate next key for current bytes.
            long keyVal = this.key.nextValue();
            for(int i = 0; i < read; i++) {
                // Shift key to have one byte available for each byte that needs to be encrypted
                buffer[i] = (byte) (buffer[i] ^ (byte)(keyVal >> 8 * i));
            }
            output.write(buffer, 0, read);
        }

        stream.close();
        output.close();
    }

    public static void main(String[] args) throws IOException {
        File input = new File(args[0]);
        File output = new File(args[1]);
        long key = Long.parseLong(args[2]);
        HC1 hc1 = new HC1(key, input);
        hc1.crypt(output);
    }
}
