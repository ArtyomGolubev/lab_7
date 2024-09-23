package server.interfaces;

public interface Decoder {
    String bytesToString(byte[] bytes);
    boolean passwordCheck(byte[] password, byte[] salt, byte[] expectedHash);
}
