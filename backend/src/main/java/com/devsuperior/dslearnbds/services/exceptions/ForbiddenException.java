package com.devsuperior.dslearnbds.services.exceptions;

public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ForbiddenException(String msg) {
		super(msg);		
	}
}

//-- Criada para retornar o erro 403 - Quando o usuário tem o token podem não tem acesso ao recurso, vai causar o erro.