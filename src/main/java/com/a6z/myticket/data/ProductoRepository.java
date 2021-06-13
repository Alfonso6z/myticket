package com.a6z.myticket.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.a6z.myticket.negocio.models.Negocio;
import com.a6z.myticket.negocio.models.Producto;

/**
 * Repositorio para productos
 * @author Alfonso6z
 * 
 */
public interface ProductoRepository extends CrudRepository <Producto, Long>{
    public Producto findByNombre(String nombre);
    public List<Producto> findAllByNegocio(Negocio negocio);
}
