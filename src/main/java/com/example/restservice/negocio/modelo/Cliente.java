package com.example.restservice.negocio.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * Entidad de negocio Cliente
 * 
 * @author Alfonso6z
 *
 */
@Data
@Table(name = "clientes")
@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String email;
    
    private int contadorPromocion;
    
    @ManyToOne
    private Negocio negocio;

}
