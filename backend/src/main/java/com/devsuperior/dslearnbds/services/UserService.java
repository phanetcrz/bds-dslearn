package com.devsuperior.dslearnbds.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslearnbds.dto.UserDTO;
import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.UserRepository;
import com.devsuperior.dslearnbds.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);  //-- Para fazer teste de processo no  console, trabalhando com log
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private AuthService authService;
		
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		
		authService.validateSelfOrAdmin(id); //Se esse usuário não for o que está logado e nem com role de Admin da exceção
		
		Optional<User> obj = repository.findById(id); // --Optional para evitar a trabalhar com valor nulo.
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found")); // -- O get do option recebe o objeto dentro do option que nesse caso é o User
		return new UserDTO(entity);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username); // -- Faz a busca do email, que é o username
		if (user == null) {
			logger.error("User not found " + username); // --Com isso vai ajudar a ver qual email tentou buscar e não
														// encontrou
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username); // nesse caso deu certo a busca do username
		return user;
	}
}
