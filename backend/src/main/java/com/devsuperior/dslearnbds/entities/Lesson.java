package com.devsuperior.dslearnbds.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_lesson")
@Inheritance(strategy = InheritanceType.JOINED)        //-- Como é uma superclasse e vai ter herança, usa-se a anotation @Inheritance  
public abstract class Lesson implements Serializable { // -- abstract significa que a classe não pode ser instanciada = Aula 05-12 Lesson, Content, Task
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		
	private Long id;
	private String title;
	private Integer position;
	
	@ManyToOne   //-- Muitos para um
	@JoinColumn(name = "section_id")	
	private Section section;
	
	@ManyToMany
	@JoinTable(name = "tb_lessons_done",   ///joinColumns Pega da propria classe e o inverseJoinColumns pega o tipo que estiver na coleção
		joinColumns = @JoinColumn(name = "lesson_id"),        // nome da chave estrangeira da tabela que estou relacionando 
		inverseJoinColumns = {
				@JoinColumn(name = "user_id"),   //-- Quando a chave da ourta tabela é composta, usa-se desta forma de mapear Aula 05-12 Lesson, Content, Task 
				@JoinColumn(name = "offer_id")
				}
	)
	private Set<Enrollment> enrollmentsDone = new HashSet<>();  //-- relacimento Muitos para muitos usa-se "Set" de coleção
	
	public Lesson() {		
	}
    //-- Aula 05-12 Lesson, Content, Task 
	//OBS: Nunca se coloca coleçao em construtor 
	public Lesson(Long id, String title, Integer position, Section section) {
		super();
		this.id = id;
		this.title = title;
		this.position = position;
		this.section = section;
	}
	
	//-- Get and Seters não coloca o Set na coleção, somente o Get	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public Set<Enrollment> getEnrollmentDone() {
		return enrollmentsDone;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lesson other = (Lesson) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
