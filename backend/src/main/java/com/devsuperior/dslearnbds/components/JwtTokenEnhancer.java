package com.devsuperior.dslearnbds.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer { //-- responsável por adicionar mais informações ao token "RESPONSÁVEL PELA PERSONALIZAÇÃO DO TOKEN"

	@Autowired
	private UserRepository userRepository;
	
	// ele vai receber esses dois objetos do parametro, e se vc implementar esse método ele vai entrar no ciclo de vida do Token e na hora de gera o token ele
	// vai acrescentar os objetos que vc passar 
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {  // Interface do TokenEnhancer 
		
		//Buscando e Acrescentando o usuário(email) na preparação ao token
		User user = userRepository.findByEmail(authentication.getName()); // O authentication.getName() busca o email lá no "authentication"
		
		// Para acrescentar objeto token tem que colocar os objetos em forma de MAP(Pares de Chave e Valor)
		Map<String, Object> map = new HashMap<>();
		map.put("userName", user.getName());
		map.put("userId", user.getId());
		
		//Adicionando os valors ao token
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken; // Casting convertendo o accessToken para DefaultOAuth2AccessToken, que contem o método de adição
		token.setAdditionalInformation(map);
		
		return accessToken;		
	}		
}
