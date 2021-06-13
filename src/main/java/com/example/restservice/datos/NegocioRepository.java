package com.example.restservice.datos;

import com.example.restservice.negocio.modelo.*;

import org.springframework.data.repository.CrudRepository;
/**
 * Repositorio de negocio
 */
public interface NegocioRepository extends CrudRepository<Negocio, Integer> {
	public Negocio findByNombre(String nombre);
	public Negocio findByServicio(String servicio);
	public Negocio findByUsuario(Usuario usuario);
}
