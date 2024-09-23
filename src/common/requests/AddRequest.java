package common.requests;

import common.model.AstartesCategory;
import common.model.Chapter;
import common.model.Coordinates;
import common.model.Weapon;

public class AddRequest extends AbstractRequest {
    private String name;
    private Coordinates coordinates;
    private float health;
    private int heartCount;
    private AstartesCategory astartesCategory;
    private Weapon weapon;
    private Chapter chapter;

    public AddRequest(String commandName) {
        super(commandName);
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getHealth() {
        return health;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public AstartesCategory getCategory() {
        return astartesCategory;
    }
    public Weapon getWeapon() {
        return weapon;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setCategory(AstartesCategory astartesCategory) {
        this.astartesCategory = astartesCategory;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
