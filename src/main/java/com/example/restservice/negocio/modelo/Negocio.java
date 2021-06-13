package com.example.restservice.negocio.modelo;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "negocios")
@Data
public class Negocio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	private String servicio;
	
	@ManyToOne
	private Usuario usuario;
}
