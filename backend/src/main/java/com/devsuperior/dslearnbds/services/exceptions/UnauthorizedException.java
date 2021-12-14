package com.devsuperior.dslearnbds.services.exceptions;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException(String msg) {
		super(msg);		
	}
}


//-- Retorna o erro 401 - quando o usuário não tem token ou ele não foi reconhecido.