package server.interfaces;

import common.model.SpaceMarine;

import java.util.LinkedList;

public interface FileHandler {
    LinkedList<SpaceMarine> read(String filename);
    void write(LinkedList<SpaceMarine> collection, String filename);
}
