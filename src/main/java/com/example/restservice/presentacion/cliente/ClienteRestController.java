package com.example.restservice.presentacion.cliente;

import org.springframework.http.MediaType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import lombok.extern.slf4j.Slf4j;

import com.example.restservice.servicios.ServicioClienteDto;
import com.example.restservice.datos.dto.ClienteDto;

/** 
 * @author Alfonso6z
*/

@RestController
@RequestMapping("/v1")
@Slf4j
public class ClienteRestController {
    @Autowired
    private ServicioClienteDto servicioCliente;

    /**
     * Método que permite agregar un cliente
     *
     * @param nuevoCliente los datos del nuevo cliente a registrar
     * @return los datos del cliente recién creado, más el ID generado por el sistéma para este.
     */
    @ApiOperation(value = "Registra un cliente", notes="Registra un cliente con: nombre,correo y el id del negocio que lo esta registrando")
    @ApiResponses(value={
        @ApiResponse(code = 201,message = "Cliente creado exitosamente"),
        @ApiResponse(code = 400,message = "Error al registrar cliente: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al registrar cliente",response=List.class)
    })
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/clientes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteDto> creaCliente(@RequestBody ClienteDto nuevoCliente) {
        log.info("RestController: Agregando nuevo Cliente");
        try {
            ClienteDto usuarioDto = servicioCliente.agregaCliente(nuevoCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
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
     * Get recuperar todos los clientes
     * @return lista de clientesDto
     */
    @ApiOperation(value = "Recupera todos los clientes", notes="Recupera todos los clientes en un arreglo DTO")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperaron los clientes exitosamente"),
        @ApiResponse(code = 404,message = "Error al recuperar los clientes: no se encontraron clientes"),
        @ApiResponse(code = 500,message = "Error al recuperar los clientes",response=List.class)
    })
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/clientes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <List<ClienteDto>> todosClientes() {
        log.info("RestController: Recuperando Todos los clientes");
        try {
            List<ClienteDto> clienteDto = servicioCliente.recuperaClientes();
            return ResponseEntity.status(HttpStatus.OK).body(clienteDto);
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
    
    /*@CrossOrigin(origins = "*")
    @GetMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClienteDto>> listaTodosClientes(){
    	return ResponseEntity.status(HttpStatus.OK).body(servicioCliente.recuperaTodos());
    }*/

    /**
     * Get recupera un cliente con id
     * @param id del cliente que se quiere recuperar
     * @return clienteDto recuperado
     */
    @ApiOperation(value = "Recupera un cliente con id", notes="Recupera un cliente con su id")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperó el cliente exitosamente"),
        @ApiResponse(code = 404,message = "Error al recuperar el cliente: no se encontró el cliente que se quiere recuperar"),
        @ApiResponse(code = 500,message = "Error al recuperar cliente")
    })
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/clientes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <ClienteDto> clientesId(
        @ApiParam(name = "id", value = "id del cliente a recuperar",example = "0")
        @PathVariable("id") Long id) {
        log.info("RestController: Recuperando cliente con id: " + id);
        try {
            ClienteDto clienteDto = servicioCliente.recuperaClientePorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(clienteDto);
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
     * Actualiza un cliente con su id
     * @param id del cliente que se quiere actualizar
     * @param clienteActualizado los datos del cliente actualizados
     * @return
     */
    @ApiOperation(value = "Actualiza un cliente con id", notes="Actualiza un cliente con su id")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se actualizó el cliente exitosamente"),
        @ApiResponse(code = 404,message = "Error al actualizar cliente: No se encontró al cliente que se desea actualizar"),
        @ApiResponse(code = 500,message = "Error al actualizar cliente")
    })
    @CrossOrigin(origins = "*")
    @PutMapping(path = "/cliente/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
        @ApiParam(name = "id", value = "id del cliente a actualizar",example = "0")
        @PathVariable("id") Long id, 
        @RequestBody ClienteDto clienteActualizado) {
        log.info("RestController: Actualizando cliente id" + id);
        try {
            ClienteDto clienteDto = servicioCliente.actualizaCliente(id,clienteActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(clienteDto);
            
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
     * Elimina un cliente con su id
     * @param id del cliente que se quiere eliminar
     * @return
     */
    @ApiOperation(value = "Elimina un cliente con id", notes="Elimina un cliente con su id")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se eliminó el cliente exitosamente"),
        @ApiResponse(code = 404,message = "Error al eliminar cliente: No se encontró al cliente que se quiere eliminar"),
        @ApiResponse(code = 500,message = "Error al eliminar cliente")
    })
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/clientes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
        @ApiParam(name = "id", value = "id del cliente a eliminar",example = "0")    
        @PathVariable("id") Long id) {
        log.info("RestController: Eliminando cliente con id: " + id);
        try {
            String msj = servicioCliente.elimiinaClienteId(id);
            return ResponseEntity.status(HttpStatus.OK).body(msj);
        } catch (Exception e) {
            HttpStatus status;
            if (e instanceof IllegalArgumentException) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status,e.getMessage());
        }
    }

}