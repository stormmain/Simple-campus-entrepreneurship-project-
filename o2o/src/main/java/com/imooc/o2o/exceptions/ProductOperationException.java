package com.imooc.o2o.exceptions;

public class ProductOperationException extends RuntimeException{

	private static final long serialVersionUID = -51321897981893245L;

	public ProductOperationException(String msg) {
		super(msg);
	}
}
