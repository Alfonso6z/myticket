package com.example.restservice.negocio.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
/**
 * Entidad de productos
 * @author Alfonso6z
 */
@Entity
@Table(name="productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    
    private String nombre;

    private double precio;

    @ManyToOne
    private Negocio negocio;

    // relacion bidireccional
    @ManyToMany(mappedBy = "productos")
    private List<Ticket> ticket;

}
