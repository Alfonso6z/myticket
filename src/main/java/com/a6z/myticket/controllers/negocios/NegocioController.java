package com.a6z.myticket.controllers.negocios;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.a6z.myticket.data.dto.NegocioDto;
import com.a6z.myticket.data.dto.UsuarioDto;
import com.a6z.myticket.services.ServicioNegocioDto;
import com.a6z.myticket.services.ServicioUsuarioDto;

/**
 * 
 * @author L. Manuel Arrieta
 *
 */
@RestController
@RequestMapping("/v1")
@Slf4j
public class NegocioController {
	
	@Autowired
	private ServicioNegocioDto servicioNegocio;
	
	@Autowired
	private ServicioUsuarioDto servicioUsuario;
	
	/**
	 * Recuera todos los negocios que un usuario tiene registrados en el sistema
	 * 
	 * @param id el ID del usuario que ha registrado los negocios
	 * @return una lista JSON cons los atributos de los negocios registrados por el usuario
	 */
	@ApiOperation(value = "Recupera todos los negocios", notes="Recupera todos los negocios de un usuario")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Negocios recuperados exitosamente"),
        @ApiResponse(code = 400,message = "Error al recuperar negocios: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
    })
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/usuarios/{id}/negocios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> recuperaNegocios(
		@ApiParam(name="id",value = "identificador de usuario",required = true,example = "0")
		@PathVariable Integer id){
		
		UsuarioDto usuarioAsociado = servicioUsuario.recuperaUsuarioPorId(id);

		if(usuarioAsociado != null) {
			JSONArray negociosJson = new JSONArray();
			
			for (NegocioDto negocio:servicioNegocio.negociosDelUsuario(usuarioAsociado)) {
				
				JSONObject negocioJson = new JSONObject();
				
				negocioJson.put("id", negocio.getId());
				negocioJson.put("nombre", negocio.getNombre());
				negocioJson.put("servicio", negocio.getServicio());
				
				negociosJson.put(negocioJson);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(negociosJson.toString());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	/**
	 * Recupera negocio que coinceda con usuario
	 * @param id del usuario 
	 * @param idN del negocio
	 * @return NegocioDto
	 */
	@ApiOperation(value = "Recupera negocio con id", notes="Recupera un negocio con id usuario y id negocio")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Negocio recuperado exitosamente"),
        @ApiResponse(code = 404,message = "Error al recuperar negocio: el negocio no concide con el usuario o viceversa"),
        @ApiResponse(code = 500,message = "Error al recuperar negocio"),
    })
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/usuarios/{id}/negocios/{idN}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NegocioDto> recuperaNegocioId(
		@ApiParam(name="id",value = "identificador de usuario",required = true,example = "0")
		@PathVariable Integer id,
		@ApiParam(name="idN",value = "identificador del negocio",required = true,example = "0")
		@PathVariable("idN") Integer idN){
			log.info("RestController: Recuperando negocio con id: " + idN);
			try {
				NegocioDto negocioDto = servicioNegocio.recuperaNegocioIds(id, idN);
				return ResponseEntity.status(HttpStatus.OK).body(negocioDto);
			} catch (Exception ex) { 
				HttpStatus status;
				if (ex instanceof IllegalArgumentException) {
					status = HttpStatus.NOT_FOUND;
				} else {
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
				throw new ResponseStatusException(status, ex.getMessage());
			}
		
	}
	/**
	 * Registra un negocio con id de usuario
	 * @param nuevoNegocio Dto del nuevo negocio que se va a registrar
	 * @param id del usuario que registra el negocio
	 * @return
	 */
	@ApiOperation(value = "Registra un nuevo negocio", notes="Registra un nuevo negocio con id cliente")
    @ApiResponses(value={
        @ApiResponse(code = 201,message = "Negocio registrado exitosamente"),
        @ApiResponse(code = 400,message = "Error al registrar negocio: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
		@ApiResponse(code = 500,message = "Error al registrar negocio")
	})
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/usuarios/{id}/negocios", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NegocioDto> agregaNegocio(@RequestBody NegocioDto nuevoNegocio, @PathVariable("id") Long id) {
        log.info("RestController: Agregando nuevo negocio");
        try {
            NegocioDto negocioDto = servicioNegocio.registraNegocioIdUsuario(id, nuevoNegocio);
            return ResponseEntity.status(HttpStatus.CREATED).body(negocioDto);
        } catch (Exception ex) { 
            HttpStatus status;
            if (ex instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, ex.getMessage());
        }
    }

	/**
	 * Actualiza negocio con id de usuario y id de negocio
	 * @param id del usuario 
	 * @param idN del negocio
	 * @param negocioA Dto con los compos actualizados
	 * @return negocio Dto Actualizado
	 */
	@ApiOperation(value = "Actualiza un negocio con id", notes="Actualiza un negocio con su id usuario que coincida")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se actualizó el negocio exitosamente"),
        @ApiResponse(code = 404,message = "Error al actualizar negocio: No se encontró el negocio"),
        @ApiResponse(code = 500,message = "Error al actualizar negocio")
    })
    @CrossOrigin(origins = "*")
    @PutMapping(path = "/usuarios/{id}/negocios/{idN}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NegocioDto> update(
		@ApiParam(name="id",value = "identificador de usuario",required = true,example = "0")
		@PathVariable Integer id,
		@ApiParam(name="idN",value = "identificador del negocio",required = true,example = "0")
		@PathVariable("idN") Integer idN,
		@RequestBody NegocioDto negocioA) {
        log.info("RestController: Actualizando negocio id" + idN);
        try {
            NegocioDto negocioDto = servicioNegocio.actualizaNegocio(id, idN, negocioA);
            return ResponseEntity.status(HttpStatus.OK).body(negocioDto);
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

	/**
	 * Elimina un negocio con su id y id usuario
	 * @param id del usuario
	 * @param idN del negocio
	 * @return un mensaje con el id y idN confirmando que se elimino el negocio
	 */
	@ApiOperation(value = "Elimina un negocio con id", notes="Elimina un negocio con su id y id usuario")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se eliminó el negocio exitosamente"),
        @ApiResponse(code = 404,message = "Error al eliminar negocio: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al eliminar negocio")
    })
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/usuarios/{id}/negocios/{idN}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(
		@ApiParam(name="id",value = "identificador de usuario",required = true,example = "0")
		@PathVariable Integer id,
		@ApiParam(name="idN",value = "identificador del negocio",required = true,example = "0")
		@PathVariable("idN") Integer idN) {
        log.info("RestController: Eliminando negocio con id: " + idN);
        try {
            String msj = servicioNegocio.elimiinaNegocioId(id,idN);
            return ResponseEntity.status(HttpStatus.OK).body(msj);
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
