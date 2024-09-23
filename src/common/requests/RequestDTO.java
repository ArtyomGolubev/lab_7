package common.requests;

import java.io.Serializable;

public class RequestDTO implements Serializable {
    private AbstractRequest abstractRequest;

    public <T extends AbstractRequest> RequestDTO(T request) {
        this.abstractRequest = request;
    }

    public AbstractRequest getRequest() {
        return abstractRequest;
    }
}
