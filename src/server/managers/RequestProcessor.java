package server.managers;

import common.network.Serializer;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import server.commands.AbstractCommand;

import java.nio.ByteBuffer;

public class RequestProcessor {
    private final CommandProcessor commandProcessor;

    public RequestProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public <T extends AbstractResponse> T handleRequest(ByteBuffer buffer) {

        T response;
        RequestDTO requestDTO;
        try {
            requestDTO = Serializer.deserializeObject(buffer);
            AbstractCommand command = commandProcessor.getCommands().get(requestDTO.getRequest().getCommandName());
            System.out.println("RECEIVED: " + requestDTO.getRequest());
            response = (T) command.execute(requestDTO);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
