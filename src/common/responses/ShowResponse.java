package common.responses;

import common.model.SpaceMarine;

import java.util.LinkedList;

public class ShowResponse extends AbstractResponse {
    private LinkedList<SpaceMarine> spaceMarines;

    public ShowResponse(String commandName, String message) {
        super(commandName, message);
    }

    public LinkedList<SpaceMarine> getMarines() {
        return spaceMarines;
    }

    public void setOrganizations(LinkedList<SpaceMarine> spaceMarines) {
        this.spaceMarines = spaceMarines;
        StringBuilder stringBuilder = new StringBuilder();
        for (SpaceMarine spaceMarine : this.spaceMarines) {
            stringBuilder.append(spaceMarine).append("\n");
        }
        this.message = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.message;
    }
}
