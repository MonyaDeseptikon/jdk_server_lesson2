package server.server.storage;

public interface Repository {

    void save(String text);

    String read();
}
