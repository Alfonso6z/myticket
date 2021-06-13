package com.example.restservice.datos;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.restservice.negocio.modelo.Cliente;
import com.example.restservice.negocio.modelo.Negocio;

/**
 * Repositorio para clientes
 * @author Alfonso6z
 */
public interface ClienteRepository extends CrudRepository <Cliente, Long> {

	public Cliente findByEmail(String email);
	public Cliente findByNombre(String nombre);
	public Cliente findByNegocio(Negocio negocio);
	public List<Cliente> findAllByNegocio(Negocio negocio);
	
}