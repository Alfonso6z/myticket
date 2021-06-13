package com.a6z.myticket.negocio.models;

import javax.persistence.*;

import lombok.Data;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
@Data
public class Usuario {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	private String email;
}