package Client.Handlers;

import Client.network.TCPClient;
import common.exceptions.ClosureFailedException;
import common.requests.AbstractRequest;
import common.requests.ExitRequest;
import common.requests.RequestDTO;
import common.responses.AbstractResponse;
import common.responses.ErrorResponse;
import common.responses.ExitResponse;
import common.responses.ResponseDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Sender {
    private final TCPClient client;

    public Sender(TCPClient client) {
        this.client = client;
    }


    public <T extends AbstractRequest> AbstractResponse sendRequest(T request) throws IOException {
        if (request instanceof ExitRequest) {
            try {
                client.close();
                return new ExitResponse("exit", "exit");
            } catch (ClosureFailedException e) {
                return new ErrorResponse(e.toString());
            }
        }
        sendObject(request);
        try {
            ResponseDTO responseDTO = (ResponseDTO) recieveObject();
            return responseDTO.getResponse();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends AbstractRequest> void sendObject(T request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        RequestDTO requestDTO = new RequestDTO(request);
        oos.writeObject(requestDTO);
        oos.close();

        sendData(baos.toByteArray());
    }

    private void sendData(byte[] bytes) {
        try {
            client.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object recieveObject() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(this.client.getInputStream());
        return objectInputStream.readObject();
    }
}
