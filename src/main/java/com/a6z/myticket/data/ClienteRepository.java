package com.a6z.myticket.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.a6z.myticket.negocio.models.Cliente;
import com.a6z.myticket.negocio.models.Negocio;

/**
 * Repositorio para clientesfd
 * @author Alfonso6z
 */
public interface ClienteRepository extends CrudRepository <Cliente, Long> {

	public Cliente findByEmail(String email);
	public Cliente findByNombre(String nombre);
	public Cliente findByNegocio(Negocio negocio);
	public List<Cliente> findAllByNegocio(Negocio negocio);
	
}