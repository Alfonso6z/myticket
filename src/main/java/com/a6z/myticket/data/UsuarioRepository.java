package com.a6z.myticket.data;

import com.a6z.myticket.negocio.models.Usuario;

import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called usuarioRepository
// CRUD refers Create, Read, Update, Delete
/**
 * Repositorio de Usuarios
 */
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	public Usuario findByNombre(String name);
	public Usuario findByEmail(String email);
}