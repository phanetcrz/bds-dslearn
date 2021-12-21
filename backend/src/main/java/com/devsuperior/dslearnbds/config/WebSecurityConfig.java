package com.devsuperior.dslearnbds.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Faz com que o projeto aceite esse tipo de autorizaçao para os recursos -  05-27 Pré-autorizando métodos por perfil de usuário 16:00 min.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //injetado da classe "class AppConfig"
	
	@Autowired
	private UserDetailsService userDetailsService; 
	
	
	@Override // ele tem a função de configurar  qual é o algoritimo para verificar a senha(que é o Bcrypt) e configurar quem que é o meu UserDetailsServices
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		                     //-userDetailsService : busca o usuário por email  passwordEncoder:  ele vai ter que analisar a senha criptografada       
	}

	//--Configuração provisória para liberar todos os caminhos, endpoints sem precisar logar, da aplicação. 
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**"); 
		// Por ter adicionado na aplicação do Spring Claud OAuth, tem que colocar "/actuator/" é uma biblioteca que o Spring Claud OAuth usa para passar nas requisições
	}

	@Override
	@Bean  //aula 04-23  - o Bean faz com que o método seja um componente ou objeto que possa ser utilizado em outras partes do projeto 
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
