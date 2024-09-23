package common.responses;

import java.io.Serializable;

public class ResponseDTO implements Serializable {
    private final AbstractResponse abstractResponse;

    public <T extends AbstractResponse> ResponseDTO(T response) {
        this.abstractResponse = response;
    }

    public AbstractResponse getResponse() {
        return abstractResponse;
    }
}
