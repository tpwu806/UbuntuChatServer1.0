package uc.common.domain;

import java.io.Serializable;

public class ResultObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Object ResponseObject;
	public int ErrorCode;
	public String ErrorString;

	public ResultObject() {
		this.ResponseObject = null;
		this.ErrorCode = 0;
		this.ErrorString = null;
	}

	public ResultObject(Object RO) {
		this.ResponseObject = RO;
		this.ErrorCode = 0;
		this.ErrorString = null;
	}

	public ResultObject(Object RO, int EC) {
		this.ResponseObject = RO;
		this.ErrorCode = EC;
		this.ErrorString = null;
	}

	public ResultObject(Object RO, int EC, String ES) {
		this.ResponseObject = RO;
		this.ErrorCode = EC;
		this.ErrorString = ES;
	}

	public int GetErrorCode() {
		return this.ErrorCode;
	}

}