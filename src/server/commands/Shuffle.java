package server.commands;

import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.RegardsResponse;
import server.interfaces.SimpleCommand;

public class Shuffle extends AbstractCommand implements SimpleCommand {
    public Shuffle(String consoleName) {
        super(consoleName, "(standart) Shuffles marines in the Imperium Army list", "Marines shuffled!");
    }

    @Override
    public AbstractResponse execute(RequestDTO requestDTO) {
        collectionProcessor.shuffleCollection();
        return new RegardsResponse(getConsoleName(), regards);
    }
}
