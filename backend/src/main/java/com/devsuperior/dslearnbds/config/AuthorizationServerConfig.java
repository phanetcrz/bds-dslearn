package com.devsuperior.dslearnbds.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	
	@Value("${jwt.durationRefreshToken}")
	private Integer durationRefreshToken;
	
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
	
	@Autowired
	private UserDetailsService userDetailsService;         // aula 05-26 Refresh Token
	
	//-- aula 04-23 -  ?? necess??rio sobreescrever 3 m??todos de configure: bot??o direito/source/override implements methods
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override //-- aula 04-23 -  Respons??vel pela configura????o das crendenciais da aplica????o e outros detalhes e quais ser??o os dados do cliente
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()                               // Para dizer que o processo vai ser feiot em mem??ria
		.withClient(clientId)           				 // nome da aplica????o o Id dela
		.secret(passwordEncoder.encode(clientSecret))    // Senha da aplica????o
		.scopes("read","write")                          // tipo de acesso se vai ser de leitura e escrita
		.authorizedGrantTypes("password", "refresh_token") // Grant Type  - refresh_token # aula 05-26 Refresh Token
		.accessTokenValiditySeconds(jwtDuration)        // Tempo de valida????o do token, que no exemplo representa 24hrs
		.refreshTokenValiditySeconds(durationRefreshToken);
		
	}

	@Override  //-- ?? aqui ?? que vai falar quem quem vai autorizar e qual o formato do token
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		
		//instanciando a variavel chain 
		TokenEnhancerChain chain =  new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer));
		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore) //objetos respons??veis por processar o token 
		.accessTokenConverter(accessTokenConverter)
		.tokenEnhancer(chain)
		.userDetailsService(userDetailsService); // # aula 05-26 Refresh Token
	}
	
	
}
