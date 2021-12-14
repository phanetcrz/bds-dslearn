package com.devsuperior.dslearnbds.resources.exceptions;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthCustomError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String error;
	
	//-- Annotation do jackson
	@JsonProperty("error_description") //-- Aula 05-21 Autorização customizada em nível de serviço PARTE 1  - java camelcase enviando ao JSON convertido como snake case   
	private String errorDescription; 
	
	public OAuthCustomError() {		
	}

	public OAuthCustomError(String error, String errorDescription) {
		super();
		this.error = error;
		this.errorDescription = errorDescription;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
}
