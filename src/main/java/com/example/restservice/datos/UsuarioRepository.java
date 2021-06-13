package com.example.restservice.datos;

import com.example.restservice.negocio.modelo.Usuario;

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