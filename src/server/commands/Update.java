package server.commands;

import common.exceptions.NotFoundException;
import common.exceptions.NotAnOwnerException;
import common.model.*;
import common.network.GuestUser;
import common.network.AbstractUser;
import common.requests.RequestDTO;
import common.requests.UpdateRequest;
import common.responses.ErrorResponse;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;

import java.sql.SQLException;

public class Update extends AbstractCommand {
    public Update(String consoleName) {
        super(consoleName, "(long id) Update marine by id", "Marine is updated!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        UpdateRequest request = (UpdateRequest) requestDTO.getRequest();
        AbstractUser abstractUser = request.getUser();
        String name;
        Coordinates coordinates;
        float health;
        int heartCount;
        AstartesCategory category;
        Weapon weapon;
        Chapter chapter;
        String login = abstractUser.getLogin();

        if (abstractUser instanceof GuestUser) {
            return new ErrorResponse("You must register to update marines.");
        }
        try {
            SpaceMarine outdated = collectionProcessor.getMarineById(request.getId());

            if (!request.getName().isEmpty()) {
                name = request.getName();
            } else {
                name = outdated.getName();
            }
            if (request.getCoordinates() != null) {
                coordinates = request.getCoordinates();
            } else {
                coordinates = outdated.getCoordinates();
            }
            if (request.getHealth() != 0) {
                health = request.getHealth();
            } else {
                health = outdated.getHealth();
            }
            if (request.getHeartCount() != 0) {
                heartCount = request.getHeartCount();
            } else {
                heartCount = outdated.getHeartCount();
            }
            if (request.getCategory() != null) {
                category = request.getCategory();
            } else {
                category = outdated.getCategory();
            }
            if (request.getWeapon() != null) {
                weapon = request.getWeapon();
            } else {
                weapon = outdated.getWeapon();
            }
            if (request.getChapter() != null) {
                chapter = request.getChapter();
            } else {
                chapter = outdated.getChapter();
            }

            SpaceMarine newSpaceMarine = new SpaceMarine(
                    outdated.getId(),
                    name,
                    coordinates,
                    outdated.getCreationDate(),
                    health,
                    heartCount,
                    category,
                    weapon,
                    chapter,
                    request.getUser().getLogin());
            if (this.collectionProcessor.getConnection().updateMarine(
                    outdated.getId(),
                    name,
                    coordinates,
                    health,
                    heartCount,
                    category,
                    weapon,
                    chapter,
                    request.getUser().getLogin())) {
                collectionProcessor.setMarineById(outdated.getId(), newSpaceMarine);
            }
            return new RegardsResponse(getConsoleName(), regards);
        } catch (NotFoundException e) {
            return new ErrorResponse(e.toString());
        } catch (SQLException e) {
            return new ErrorResponse("Can't update the element");
        } catch (NotAnOwnerException e) {
            return new ErrorResponse(e.getMessage());
        }
    }
}
