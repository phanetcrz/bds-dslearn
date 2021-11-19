package com.devsuperior.dslearnbds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {
	
	@Value("${jwt.secret}") //aula 04-24 - Pega o valor da propriedade lá no applcation-teste.properties
	private String jwtSecret; //a variavel jwtSecret será instanciada com o valor vindo do @Value("${jwt.secret}") 

	@Bean   //--Bean é o componente do Spring que é um anotação de método 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean // aula 04-23 - Os JwtAccessTokenConverter e JwtTokenStore são objetos que serão capazes acessar um token JWT como: Ler, decodificar, criar um token codificando ele 
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter(); // instanciando o objeto
		tokenConverter.setSigningKey(jwtSecret); //Registra a chave do token 
		return tokenConverter; //e retorna
	}

	@Bean
	public JwtTokenStore tokenStore() {   //objetos responsáveis por processar o token
		return new JwtTokenStore(accessTokenConverter());
	}	
}
