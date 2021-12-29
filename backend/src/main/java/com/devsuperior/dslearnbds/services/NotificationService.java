package com.devsuperior.dslearnbds.services;

import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslearnbds.dto.NotificationDTO;
import com.devsuperior.dslearnbds.entities.Deliver;
import com.devsuperior.dslearnbds.entities.Notification;
import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.observers.DeliverRevisionObserver;
import com.devsuperior.dslearnbds.repositories.NotificationRepository;

@Service
public class NotificationService implements DeliverRevisionObserver {   //-- implements DeliverRevisionObserver na 05-29 Padrão de projetos observer, princípio SOLID OCP

	@Autowired
	private NotificationRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private DeliverService deliverService;
	
	@PostConstruct //-- faz o objeto ser executado logo apois o NotificationService se instanciado.
	private void initialize() {  //05-29 Padrão de projetos observer, princípio SOLID OCP
		deliverService.subscribeDeliverRevisionObserver(this);  //-- this é o NotificationService
	}
	
	@Transactional 									       	//boolean com "b" minusculo, tornasse obrigatório e será informado um valor default vindo da requisição
	public Page<NotificationDTO> notificationsForCurrentUser(boolean unreadOnly, Pageable pageable){ 
		User user = authService.authenticated(); 
		Page<Notification> page = repository.find(user, unreadOnly, pageable);
		return page.map(x -> new NotificationDTO(x)); //-- Convertendo Page de Notification entidade para Page de NotificationDTO
	}
	
	@Transactional
	public void saveDeliverNotification(Deliver deliver) { //Aula 05-28 Salvando notificação sem observer nem princípio SOLID OCP -- Salvando notificações
		
		Long sectionId = deliver.getLesson().getSection().getId();
		Long resourceId= deliver.getLesson().getSection().getResource().getId();
		Long offerId   = deliver.getLesson().getSection().getResource().getOffer().getId(); 
		
		String route = "/offer/" + offerId + "/resources/" + resourceId + "/sections/" + sectionId;
		String text = deliver.getFeedback();
		Instant moment = Instant.now();
		User user = deliver.getEnrollment().getStudent();
		
		Notification notification = new Notification(null, text, moment, false, route, user);
		repository.save(notification);
	}

	@Override
	public void onSaveRevision(Deliver deliver) {
		saveDeliverNotification(deliver);		
	}
}
