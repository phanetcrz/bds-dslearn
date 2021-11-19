package com.devsuperior.dslearnbds.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.devsuperior.dslearnbds.components.JwtTokenEnhancer;

@Configuration 
@EnableAuthorizationServer //aula 04-23 - Menciona que a classe abaixo representa o @EnableAuthorizationServer do OAuth2 
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	@Value("${jwt.duration}")
	private Integer jwtDuration;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter; //Beans da AppConfig.java
	
	@Autowired
	private JwtTokenStore tokenStore;                     //Beans da AppConfig.java
	
	@Autowired
	private AuthenticationManager authenticationManager;  //Beans da WebSecurityConfig.java 
	
	@Autowired
	private JwtTokenEnhancer tokenEnhancer;                //Component da JwtTokenEnhancer.java   
	
	//-- aula 04-23 -  é necessário sobreescrever 3 métodos de configure: botão direito/source/override implements methods
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override //-- aula 04-23 -  Responsável pela configuração das crendenciais da aplicação e outros detalhes e quais serão os dados do cliente
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()                               // Para dizer que o processo vai ser feiot em memória
		.withClient(clientId)           				 // nome da aplicação o Id dela
		.secret(passwordEncoder.encode(clientSecret))    // Senha da aplicação
		.scopes("read","write")                          // tipo de acesso se vai ser de leitura e escrita
		.authorizedGrantTypes("password")                // Grant Type
		.accessTokenValiditySeconds(jwtDuration);        // Tempo de validação do token, que no exemplo representa 24hrs
		
	}

	@Override  //-- é aqui é que vai falar quem quem vai autorizar e qual o formato do token
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		
		//instanciando a variavel chain 
		TokenEnhancerChain chain =  new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer));
		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore) //objetos responsáveis por processar o token 
		.accessTokenConverter(accessTokenConverter)
		.tokenEnhancer(chain);
	}
	
	
}
