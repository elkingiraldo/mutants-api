package co.com.elkin.apps.mutants.exception;

import org.springframework.http.HttpStatus;

/**
 * Enum with the errors the system handles
 * 
 * @author elkin.giraldo
 *
 */
public enum APIServiceErrorCodes implements IAPIServiceErrorMsg {

	GENERAL_EXCEPTION("general.exception", HttpStatus.INTERNAL_SERVER_ERROR),

	HUMAN_MATRIX_DNA_SIZE_EXCEPTION("human.matrix.dna.size.exception", HttpStatus.BAD_REQUEST),
	HUMAN_IS_NOT_MUTANT_EXCEPTION("human.is.not.mutant.exception", HttpStatus.FORBIDDEN);

	private String message;
	private HttpStatus httpStatus;
	private String errorDetail;

	private APIServiceErrorCodes(final String message, final HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
