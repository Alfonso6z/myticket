package com.example.restservice.datos;

import java.util.List;

import com.example.restservice.negocio.modelo.Negocio;
import com.example.restservice.negocio.modelo.Producto;

import org.springframework.data.repository.CrudRepository;
/**
 * Repositorio para productos
 * @author Alfonso6z
 * 
 */
public interface ProductoRepository extends CrudRepository <Producto, Long>{
    public Producto findByNombre(String nombre);
    public List<Producto> findAllByNegocio(Negocio negocio);
}
