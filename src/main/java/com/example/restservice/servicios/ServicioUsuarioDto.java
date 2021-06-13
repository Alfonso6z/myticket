package com.example.restservice.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.restservice.datos.UsuarioRepository;
import com.example.restservice.datos.dto.UsuarioDto;
import com.example.restservice.negocio.modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuarioDto {
    @Autowired UsuarioRepository usuarioRepository;

    /**
	 * Recupera todos los usuarios existentes
	 * 
	 * @return Una lista con los usuarios (o lista vacía)
	 */
	public List <UsuarioDto> recuperaUsuarios() {
		
		List <UsuarioDto> usuarios = new ArrayList<>();
		
		for(Usuario usuario:usuarioRepository.findAll()) {
			usuarios.add(UsuarioDto.creaDto(usuario));
		}
				
		return usuarios;
	}
	
	/**
	 * Recupera un usuario de la BD por su identificador
	 * 
	 * @param id el identificador del usuario a buscar
	 * @return un DTO de usuario con el ucuario correspondiente al id provisto, nulo si no se encontró.
	 */
	public UsuarioDto recuperaUsuarioPorId(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isEmpty()) {
			return null;
		}
		else {
			return UsuarioDto.creaDto(usuario.get());
		}
	}
	
	/**
	 * Recupera un usuario de la BD por su correo electrónico
	 * 
	 * @param email el correo del usuario que se va a buscar
	 * @return un DTO de usuario con el correo encontrado.
	 */
	public UsuarioDto recuperaUsuarioPorEmail(String email) {
		return UsuarioDto.creaDto(usuarioRepository.findByEmail(email));
	}
    
	/**
	 * Registra (guarda) un usuario nuevo en la BD
	 * 
	 * @param nuevoUsuarioDto un DTO de usuario con la información del usuario a registrar
	 * @return un DTO de usuario con la info del usuario creado y su ID generada por el sistema, o null si ya hay un usuario gestrado con ese correo.
	 */
	public UsuarioDto registrarUsuario(UsuarioDto nuevoUsuarioDto){
		//REGLA DE NEGOCIO: No puede haber dos usuarios con el mismo correo
		Usuario usuario = usuarioRepository.findByEmail(nuevoUsuarioDto.getEmail());
		if(usuario != null) {
			return null;
		}
		else {
			Usuario usuarioNuevo = new Usuario();
			
			usuarioNuevo.setNombre(nuevoUsuarioDto.getNombre());
			usuarioNuevo.setEmail(nuevoUsuarioDto.getEmail());
			
			Usuario nuevoUsuario = usuarioRepository.save(usuarioNuevo);
			
			return UsuarioDto.creaDto(nuevoUsuario);
		}
	}

    public UsuarioDto actualizaUsuarioId(Integer id, UsuarioDto usuarioActualizado) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
		if(optUsuario.isEmpty()){
            throw new IllegalArgumentException("No se encontró el usuario");
		}
		Usuario usuario = optUsuario.get();
		usuario.setNombre(usuarioActualizado.getNombre());
		usuario.setEmail(usuarioActualizado.getEmail());
		usuarioRepository.save(usuario);
		usuarioActualizado.setId(usuario.getId());

		return usuarioActualizado;
    }



}
