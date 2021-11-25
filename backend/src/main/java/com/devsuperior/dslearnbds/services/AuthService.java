package com.devsuperior.dslearnbds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.UserRepository;
import com.devsuperior.dslearnbds.services.exceptions.ForbiddenException;
import com.devsuperior.dslearnbds.services.exceptions.UnauthorizedException;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true) // em uma operção q é somente leitura, tem que dizer explicitamente, para que essa operação não trave o bando de dados . 
	public User authenticated() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName(); //--pega o usuário que já foi reconhecido pelo spring security
			return userRepository.findByEmail(username);
		}
		catch(Exception e) {
			throw new UnauthorizedException("Invalid user");
		}		
	}
	
	public void validateSelfOrAdmin(long userId) {
		User user = authenticated(); //-- Pega o usuário que já foi autenticado
		if (!user.getId().equals(userId) && !user.hasHole("ROLE_ADMIN")) {   //-- Se o usuário user não for igual ao userId(ele mesmo) e também não for o admin
			throw new ForbiddenException("Access denied");			
		}
	}
}
