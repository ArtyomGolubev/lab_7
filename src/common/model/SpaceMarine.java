package common.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final float health; //Значение поля должно быть больше 0
    private final Integer heartCount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 3
    private final AstartesCategory category; //Поле не может быть null
    private final Weapon weapon; //Поле не может быть null
    private final Chapter chapter; //Поле может быть null
    private String ownerLogin;

    public SpaceMarine(Long id, String name, Coordinates coordinates, LocalDate creationDate, float health, Integer heartCount, AstartesCategory category, Weapon weapon, Chapter chapter) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.weapon = weapon;
        this.chapter = chapter;
    }

    public SpaceMarine(Long id, String name, Coordinates coordinates, LocalDate creationDate, float health, Integer heartCount, AstartesCategory category, Weapon weapon, Chapter chapter, String ownerLogin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.category = category;
        this.weapon = weapon;
        this.chapter = chapter;
        this.ownerLogin = ownerLogin;
    }

    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Float getHealth() {
        return health;
    }

    public Integer getHeartCount() {
        return heartCount;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SpaceMarine that = (SpaceMarine) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(creationDate, that.creationDate) && Objects.equals(health, that.health) && Objects.equals(heartCount, that.heartCount) && category == that.category && weapon == that.weapon && Objects.equals(chapter, that.chapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, health, heartCount, category, weapon, chapter);
    }

    @Override
    public int compareTo(SpaceMarine marine) {
        int compareById = this.id.compareTo(marine.getId());
        return compareById;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    @Override
    public String toString() {
        return "Marine: {" +
                "Id:" + id +
                ", Name:" + name +
                ", Coordinates:" + coordinates +
                ", CreationDate:" + creationDate +
                ", Amount of HP:" + health +
                ", Amount of hearts: " + heartCount +
                ", Astartes Category:" + category +
                ", Weapon:" + weapon +
                ", Chapter:" + chapter +
                ", ownerLogin=" + ownerLogin +
                '}';
    }

}
