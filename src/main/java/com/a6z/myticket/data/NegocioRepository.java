package com.a6z.myticket.data;

import org.springframework.data.repository.CrudRepository;

import com.a6z.myticket.negocio.models.Negocio;
import com.a6z.myticket.negocio.models.Usuario;
/**
 * Repositorio de negocio
 */
public interface NegocioRepository extends CrudRepository<Negocio, Integer> {
	public Negocio findByNombre(String nombre);
	public Negocio findByServicio(String servicio);
	public Negocio findByUsuario(Usuario usuario);
}
