package asw.agents.webService.responses.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User id  is required")
public class RequiredUserIDResponse extends ErrorResponse{


	private static final long serialVersionUID = 1L;

	@Override
	public String getMessageJSONFormat() {
		return "{\"reason\": \"Required user id\"}";
	}

	@Override
	public String getMessageStringFormat() {
		return "User id is required";
	}

}
