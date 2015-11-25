package Praktika2;

/**
 * Created by tim on 25.11.15.
 */
public class DESKey {
    public byte[][] keys;
    public byte[] iv;

    public DESKey() {
        this.keys = new byte[3][8];
        this.iv = new byte[8];
    }
}
