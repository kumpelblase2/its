package Praktika2;

import java.io.*;

/**
 * Created by tim on 17.11.15.
 */
public class HC1 {

    private final LCG key;
    private final File cryptFile;

    public HC1(long key, File cryptFile) {
        this.key = new LCG(key);
        this.cryptFile = cryptFile;
    }

    public void crypt(File inOutput) throws IOException {
        InputStream stream = new FileInputStream(this.cryptFile);
        OutputStream output = new FileOutputStream(inOutput);
        byte[] buffer = new byte[8];
        int read = 0;
        while((read = stream.read(buffer)) > 0) {
            for(int i = read; i < 8; i++) {
                buffer[i] = 0;
            }

            long keyVal = this.key.nextValue();
            for(int i = 0; i < read; i++) {
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
