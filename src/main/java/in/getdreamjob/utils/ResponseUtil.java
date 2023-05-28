package in.getdreamjob.utils;

import in.getdreamjob.model.GeneralResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    private GeneralResponse response;

    public GeneralResponse createResponseObject(String message) {
        response = new GeneralResponse();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }
}
