package praktikum3;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.*;

/**
 * Created by tim on 01.12.15.
 */
public class RSF {

    private final File inputFile;
    private final PrivateKey myKey;
    private final PublicKey hisKey;

    public RSF(File inputFile, String myPrivateKey, String hisPubKey) throws Exception {
        this.inputFile = inputFile;
        this.myKey = SSF.getPrivateKey(myPrivateKey);
        this.hisKey = SSF.getPublicKey(hisPubKey);
    }

    public void decrypt(File outputFile) throws Exception {
        DataInputStream input = new DataInputStream(new FileInputStream(this.inputFile));
        int aesLength = input.readInt();
        byte[] encryptedAES = new byte[aesLength];
        input.readFully(encryptedAES);

        int signatureLength = input.readInt();
        System.out.println(signatureLength);
        byte[] signature = new byte[signatureLength];
        input.readFully(signature);

        int algorithmParamsLength = input.readInt();
        byte[] algorithmParamsBytes = new byte[algorithmParamsLength];
        input.readFully(algorithmParamsBytes);
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
        algorithmParameters.init(algorithmParamsBytes);

        byte[] aesKey = this.decryptKey(encryptedAES);
        if(!this.checkSignature(aesKey, signature)) {
            System.out.println("Signature was invalid.");
            input.close();
            return;
        }

        this.decryptFile(input, outputFile, aesKey, algorithmParameters);
    }

    private void decryptFile(InputStream inputStream, File outputFile, byte[] aesKey, AlgorithmParameters algorithmParams) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, algorithmParams);

        FileOutputStream output = new FileOutputStream(outputFile);

        int read;
        byte[] buffer = new byte[512];
        while((read = inputStream.read(buffer)) > 0) {
            if(read < 512 ) {
                output.write(cipher.update(buffer, 0, read));
            } else {
                output.write(cipher.update(buffer));
            }
        }

        output.write(cipher.doFinal());

        output.close();
    }

    public byte[] decryptKey(byte[] aesKey) throws Exception {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, this.myKey);
        return rsaCipher.doFinal(aesKey);
    }

    public boolean checkSignature(byte[] aesKey, byte[] signature) throws Exception {
        Signature sha256sign = Signature.getInstance("SHA256withRSA");
        sha256sign.initVerify(this.hisKey);
        sha256sign.update(aesKey);
        return sha256sign.verify(signature);
    }

    public static void main(String[] args) throws Exception {
        String privateKey = args[0];
        String pubKey = args[1];
        String encryptedFile = args[2];
        String outputFile = args[3];

        RSF rsf = new RSF(new File(encryptedFile), privateKey, pubKey);
        rsf.decrypt(new File(outputFile));
    }
}
