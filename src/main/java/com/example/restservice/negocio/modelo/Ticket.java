package com.example.restservice.negocio.modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
/** Entidad Ticket
 * @author Alfonso6z
 * 
 */
@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Date fecha;
	
	private String observacion;

	private double total;

	@ManyToOne
	private Negocio negocio;

	@ManyToOne
	private Cliente cliente;

	// relacion bidireccional
	@ManyToMany
	private List<Producto> productos;

	// agrega los productos a los tiket y genera la relacion en la tabla ticker_productos
	public void addProducto(Producto producto){
		if(this.productos == null){
			this.productos = new ArrayList<>();
		}
		this.productos.add(producto);
	}

}
