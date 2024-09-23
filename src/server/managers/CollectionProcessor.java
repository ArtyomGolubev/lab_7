package server.managers;

import Client.Handlers.Checker;
import common.exceptions.NotFoundException;
import common.network.AbstractUser;
import server.db.DBConnection;
import server.interfaces.FileHandler;
import common.model.*;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;


public class CollectionProcessor {
    private ConcurrentLinkedDeque<SpaceMarine> collection;
    private String data;
    private LocalDate lastUpdateDate;
    private DBConnection connection;

    public CollectionProcessor(FileHandler fileHandler) {
        lastUpdateDate = LocalDate.now();
    }

    public void loadCollectionFromDB() throws SQLException {
        this.collection = this.connection.getAllMarines();
        updateData();
    }

    private void updateData() {
        data = "Collection type: " + LinkedList.class.getName() + "\n"
                + "Objects type: " + SpaceMarine.class.getName() + "\n"
                + "Collection size: " + collection.size() + "\n"
                + "Last update date: " + lastUpdateDate;
    }

    public void addNewMarine(SpaceMarine spaceMarine) {
        try {
            long id = connection.addMarine(spaceMarine.getName(),
                    spaceMarine.getCoordinates(),
                    spaceMarine.getCreationDate(),
                    spaceMarine.getHealth(),
                    spaceMarine.getHeartCount(),
                    spaceMarine.getCategory(),
                    spaceMarine.getWeapon(),
                    spaceMarine.getChapter(),
                    spaceMarine.getOwnerLogin());
            spaceMarine.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        collection.add(spaceMarine);
        this.lastUpdateDate = LocalDate.now();
        updateData();
    }

    public void removeById(long id, AbstractUser abstractUser) throws NotFoundException, SQLException {
        boolean result = collection.removeIf(org -> (org.getId() == id && abstractUser.getLogin().equals(org.getOwnerLogin())));
        if (result) {
            this.connection.removeById(id, abstractUser);
            lastUpdateDate = LocalDate.now();
        } else {
            throw new NotFoundException("Either marine with such Id doesn't exist, or you're not his owner.");
        }
    }

    public ConcurrentLinkedDeque<SpaceMarine> getCollection() {
        return collection;
    }
    public void clearCollection(AbstractUser abstractUser) throws SQLException {
        this.connection.clearCollectionForUser(abstractUser);
        this.collection.removeIf((SpaceMarine m) -> m.getOwnerLogin().equals(abstractUser.getLogin()));
    }

    public SpaceMarine getMarineById(long id) throws NotFoundException {
        for (SpaceMarine spaceMarine : collection) {
            if (spaceMarine.getId() == id) return spaceMarine;
        }
        throw new NotFoundException("No marine with such id");
    }

    public LinkedList<SpaceMarine> getMarinesByName(String substring) {
        return collection.stream()
                .filter(marine -> Checker.isSubstring(substring, marine.getName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }


    public SpaceMarine[] getMarinesLessThanCertainHealth(float health) {
        List<SpaceMarine> marines = new ArrayList<>();
        for (SpaceMarine spaceMarine : collection) {
            if (spaceMarine.getHealth() < health) {
                marines.add(spaceMarine);
            }
        }
        return marines.toArray(new SpaceMarine[0]);
    }

    public void shuffleCollection() {
        shuffleDeque(collection);
        lastUpdateDate = LocalDate.now();
    }

    private <T> void shuffleDeque(ConcurrentLinkedDeque<T> deque) {
        synchronized (deque) {
            List<T> list = new ArrayList<>(deque);
            deque.clear();
            Collections.shuffle(list);
            deque.addAll(list);
        }
    }

    public SpaceMarine[] getMarinesByHeartCount(int heartCount) {
        List<SpaceMarine> marines = new ArrayList<>();
        for (SpaceMarine spaceMarine : collection) {
            if (spaceMarine.getHealth() == heartCount) {
                marines.add(spaceMarine);
            }
        }
        return marines.toArray(new SpaceMarine[0]);
    }

    public void setMarineById(long id, SpaceMarine marine) {
        for (int i = 0; i < collection.size(); i++) {
            if (getMarineAtIndex(i).getId() == id) {
                setMarineAtIndex(i, marine);
                break;
            }
        }
    }

    public SpaceMarine getMarineAtIndex(int index) {
        int currentIndex = 0;
        for (SpaceMarine marine : collection) {
            if (currentIndex == index) {
                return marine;
            }
            currentIndex++;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + currentIndex);
    }

    public void setMarineAtIndex(int index, SpaceMarine newMarine) {
        int currentIndex = 0;
        Iterator<SpaceMarine> iterator = this.collection.iterator();
        while (iterator.hasNext()) {
            SpaceMarine element = iterator.next();
            if (currentIndex == index) {
                synchronized (collection) {
                    if (collection.contains(element)) {
                        iterator.remove();
                        collection.addFirst(newMarine);
                        for (int i = 0; i < currentIndex; i++) {
                            collection.addLast(collection.pollFirst());
                        }
                    }
                }
                return;
            }
            currentIndex++;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + currentIndex);
    }

    public DBConnection getConnection() {
        return connection;
    }

    public void setConnection(DBConnection connection) {
        this.connection = connection;
    }

    public String getData() {
        return data;
    }
}
