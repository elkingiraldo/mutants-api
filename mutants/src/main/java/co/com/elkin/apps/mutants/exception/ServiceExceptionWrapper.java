package co.com.elkin.apps.mutants.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Wrapper for error messages
 * 
 * @author egiraldo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceExceptionWrapper {

	private String errorCode;
	private String errorMessage;
	private String errorDetail;
	private Integer httpStatus;
	private String errorOriginPath;
	private String errorOriginApp;

	public ServiceExceptionWrapper() {
	}

	public ServiceExceptionWrapper(final String errorCode, final String errorMessage, final String errorDetail,
			final Integer httpStatus, final String errorOrigin) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDetail;
		this.httpStatus = httpStatus;
		this.errorOriginPath = errorOrigin;
	}

	public ServiceExceptionWrapper(final String errorCode, final String errorMessage, final String errorDetail,
			final Integer httpStatus) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDetail;
		this.httpStatus = httpStatus;
	}

	public ServiceExceptionWrapper(final String errorCode, final String errorMessage, final String errorDetail) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(final String errorDetail) {
		this.errorDetail = errorDetail;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(final Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorOriginPath() {
		return errorOriginPath;
	}

	public void setErrorOriginPath(final String errorOriginPath) {
		this.errorOriginPath = errorOriginPath;
	}

	public String getErrorOriginApp() {
		return errorOriginApp;
	}

	public void setErrorOriginApp(final String errorOriginApp) {
		this.errorOriginApp = errorOriginApp;
	}

	@Override
	public String toString() {
		return "ServiceExceptionWrapper [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorDetail="
				+ errorDetail + ", httpStatus=" + httpStatus + ", errorOriginPath=" + errorOriginPath
				+ ", errorOriginApp=" + errorOriginApp + "]";
	}

}
