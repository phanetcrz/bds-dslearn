package com.devsuperior.dslearnbds.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer   // Essa anotacion vai processar por baixo dos panos a configuração para esse classe implemente a funcionalidade do Resouce Server do Oauth2
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
	//faz override de dois métodos configure
	
	//--ambiente de execução da aplicação
	@Autowired
	private Environment env;
	
	// para fazer as configurações será preciso injetar o tokenStore
	@Autowired
	private JwtTokenStore tokenStore; //Bean AppConfig.java
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" }; // Constante de vetor
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {   // É aonde configura o Token Store
		resources.tokenStore(tokenStore);
		//--Com isso, o Resource Server vai ser capaz de decodificar o token e analisar se o token está batendo com secret, se está expirado ou não. tendo condição de ver se o token é valido
	}

	@Override
	public void configure(HttpSecurity http) throws Exception { //--Método responsável pelas configurações das rotas e validação de acesso a elas com base nas credenciais
		
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){  //Se eu estiver executando meu profiles como test, libera o acesso ao banco H2
			http.headers().frameOptions().disable();   /// Libera os frames do sistema H2
		}
		
		http.authorizeRequests() //--define as autorizações
		.antMatchers(PUBLIC).permitAll() 
		.anyRequest().authenticated();//Exige quem for acessar qualquer outra rota, tem que estar logado não importando o perfil do usuário
		
		http.cors().configurationSource(corsConfigurationSource());// Chama o método cors abaixo
	}
	
	@Bean    //-- Liberaçao do CORS que é um recurso que os navegadores e aplicações tem, que na web não se consegue acessar um sistema que está em um Hots usando um aplicação que está em outro host.
	public CorsConfigurationSource corsConfigurationSource() {   //-- essa configuração é para liberar o acesso que é bloqueado pelo CORS      
		CorsConfiguration corsConfig = new CorsConfiguration();  
		corsConfig.setAllowedOriginPatterns(Arrays.asList("*")); //-- quem vai pode acessar (* = todos)
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH")); //-- quais metodos serão permitidos, no caso esses da lista 
		corsConfig.setAllowCredentials(true); //-- vai permitir credencial? no caso sim.
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); //-- Quais cabeçahos vão deixar? esses dois e podem ser utilizado outros

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);  //-- aqui significa que todos respeitarão as configurações acima realizada no corsConfig 
		return source;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() { //-- Serve para registrar a configuração de CORS
		FilterRegistrationBean<CorsFilter> bean 
			= new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Aqui diz que a ordem desse registro será HIGHEST_PRECEDENCE tem que estra no primeiro lugar,prioridade maxima
		return bean;
	}		
}
