package com.devsuperior.dslearnbds.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

 
@Entity
@Table(name = "tb_content")
public class Content extends Lesson{ //-- extends significa que essa classe é uma herança de Lesson Aula 05-12 Lesson, Content, Task
	private static final long serialVersionUID = 1L;

	private String TextContent;
	private String videoUri;
	
	public Content() {		
	}

	public Content(Long id, String title, Integer position, Section section, String textContent, String videoUri) {
		super(id, title, position, section);
		TextContent = textContent;
		this.videoUri = videoUri;
	}

	public String getTextContent() {
		return TextContent;
	}

	public void setTextContent(String textContent) {
		TextContent = textContent;
	}

	public String getVideoUri() {
		return videoUri;
	}

	public void setVideoUri(String videoUri) {
		this.videoUri = videoUri;
	}
}
