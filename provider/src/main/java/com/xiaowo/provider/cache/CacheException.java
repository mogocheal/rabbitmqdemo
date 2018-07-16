package com.xiaowo.provider.cache;

public class CacheException extends RuntimeException{

	private static final long serialVersionUID = -2133536898661941795L;
	
	private String message;

    private String errorCode = null;
	
	public CacheException(String errorCode, String message) {
		super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public CacheException(String errorCode, Throwable ex) {
    	super(ex);
        this.errorCode = errorCode;
    }

    public CacheException(String errorCode, String message, Throwable e) {
    	super(message, e);
        this.errorCode = errorCode;
        this.message = message;
    }

}
