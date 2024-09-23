package server.interfaces;

public interface Encoder {
    byte[] getSalt();
    byte[] hash(byte[] password, byte[] salt);
}
