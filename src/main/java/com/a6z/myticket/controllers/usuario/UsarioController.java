package com.a6z.myticket.controllers.usuario;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.a6z.myticket.services.ServicioNegocioDto;
import com.a6z.myticket.services.ServicioUsuarioDto;
import com.a6z.myticket.data.dto.UsuarioDto;
import com.a6z.myticket.data.dto.NegocioDto;
import com.a6z.myticket.negocio.models.Usuario;

/**
 * 
 * @author L. Manuel Arrieta.
 *
 */
@RestController
@RequestMapping("/v1")
public class UsarioController {

	@Autowired
	private ServicioUsuarioDto servicioUsuarios;
	
	@Autowired
	private ServicioNegocioDto servicioNegocio;

	/**
	 * Recupera todos los usuarios en el sistema
	 * 
	 * @return una lista de objetos JSON con la info de los usuarios.
	 */
	@ApiOperation(value = "Recupera todos los usuarios", notes="")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperaron los usuarios exitosamente"),
        @ApiResponse(code = 500,message = "Error al recuperar los usuarios",response=List.class)
    })
	@CrossOrigin(origins = "*")
    @GetMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <List<UsuarioDto>> recuperaTodosLosUsuarios() {
		List <UsuarioDto> usuarios =  servicioUsuarios.recuperaUsuarios();
		
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
	}

	/**
	 * Registra un usuario en el sistéma junto con su primer negocio
	 * 
	 * @param registro un objeto JSON compuesto de dos elementos: un DTO de Usuairo y un DTO de Negocio
	 * @return un JSON con la info del usuario recíen registrado (incluyendo su ID asignada por el sistema)
	 */
	@ApiOperation(value = "Registra un usuario", notes="Registra un usuario con un negocio")
    @ApiResponses(value={
        @ApiResponse(code = 201,message = "Usuario creado exitosamente"),
        @ApiResponse(code = 400,message = "Error al registrar usuario: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al registrar usuario")
    })
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsuarioDto> registrarNuevoUsuario(@RequestBody Registro registro){
		UsuarioDto nuevoUsuario = registro.getUsuario();
		NegocioDto nuevoNegocio = registro.getNegocio();
		
		UsuarioDto usuarioDto = servicioUsuarios.registrarUsuario(nuevoUsuario);
		
		if (usuarioDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		else {
			Usuario usuario = new Usuario();
			usuario.setId(usuarioDto.getId());
			usuario.setNombre(usuarioDto.getNombre());
			usuario.setEmail(usuarioDto.getEmail());
			
			nuevoNegocio.setUsuario(usuario);
			NegocioDto negocioDto = servicioNegocio.registrarNegocio(nuevoNegocio);
			
			if(negocioDto == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
		}
	}
	
	//clase para contener tanto la petición de un nuevo usuario como un nuevo negocio
	@Getter
	@Setter
	private static class Registro{
		UsuarioDto usuario;
		NegocioDto negocio;
	}

	/**
	 * Actualiza usuario
	 * @param id del usuario
	 * @param usuarioActualizado usuarioDto con valores actualizados
	 * @return usuarioDto
	 */
	@ApiOperation(value = "Actualiza un usuario con id", notes="Actualiza un usuario con su id")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se actualizó el usuario exitosamente"),
        @ApiResponse(code = 404,message = "Error al actualizar usuario: No se encontró al usuario que se desea actualizar"),
        @ApiResponse(code = 500,message = "Error al actualizar usuario")
    })
    @CrossOrigin(origins = "*")
    @PutMapping(path = "/usuarios/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> update(
        @ApiParam(name = "id", value = "id del usuario a actualizar",example = "0")
        @PathVariable("id") Integer id, 
        @RequestBody UsuarioDto usuarioActualizado) {
        try {
            UsuarioDto usuarioDto = servicioUsuarios.actualizaUsuarioId(id,usuarioActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
        } catch (Exception e) {
            HttpStatus status;
            if (e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
}
