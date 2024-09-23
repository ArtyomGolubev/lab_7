package common.requests;

import common.exceptions.WrongParameterException;
import common.model.Chapter;
import common.model.Coordinates;
import common.model.AstartesCategory;
import Client.Handlers.Checker;
import common.model.Weapon;

public class UpdateRequest extends AbstractRequest implements RequestWithParameters {
    private long id;
    private String name;
    private Coordinates coordinates;
    private float health;
    private int heartCount;
    private AstartesCategory astartesCategory;
    private Weapon weapon;
    private Chapter chapter;

    public UpdateRequest(String commandName) {
        super(commandName);
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

    @Override
    public void setParameters(String... parameters) throws WrongParameterException {
        if (!parameters[0].isEmpty()) {
            if (Checker.isCorrectNumber(parameters[0], Long.class)) {
                this.id = Long.parseLong(parameters[0]);
            } else {
                throw new WrongParameterException("Wrong id entered.");
            }
        } else {
            throw new WrongParameterException("Parameter is empty.");
        }
    }

    public long getId() {
        return id;
    }
}
