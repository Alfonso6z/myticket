package com.example.restservice.presentacion.ticket;

import com.example.restservice.servicios.ServicioTicketDto;
import com.example.restservice.datos.dto.TicketDto;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.websocket.server.PathParam;

import lombok.extern.slf4j.Slf4j;
/**
 * CRUD de ticket
 * @author Alfonso6z
 */
@RestController
@RequestMapping("/v1")
@Slf4j
public class TicketController {

    @Autowired
    private ServicioTicketDto servicioTicketDto;

    /**
     * Crea el ticket para un usuario.
     * 
     * @param nuevoTicket json del ticket que se quiere generar {observació:String,"total":dooble,"negocio":String,"cliete":string}
     * @param idU id de usuario que lo emite
     * @param idN id del negocio del usuario que lo emite
     * @return ticketDto con la fecha y el id del ticket que se genero
     */
    @ApiOperation(value = "Registra un ticket", notes="Genera un ticket a partir de id usuario y id negocio")
    @ApiResponses(value={
        @ApiResponse(code = 201,message = "Ticket creado exitosamente"),
        @ApiResponse(code = 400,message = "Error al registrar Ticket:El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al registrar Ticket")
    })
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/usuarios/{idU}/negocios/{idN}/tickets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketDto> crearTicket(
        @RequestBody TicketDto nuevoTicket,
        @ApiParam(name = "idU", value = "Identificador de usuario", required = true,example = "0")
        @PathParam("idU") Long idU,
        @ApiParam(name = "idN", value = "Identificador de negocio", required = true,example = "0")
        @PathParam("idN") Long idN){
        log.info("RestController: Agregando nuevo Ticket" + nuevoTicket.getProductos());
        try {
            TicketDto ticketDto = servicioTicketDto.agregarTicket(nuevoTicket);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketDto);
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
     * Recuper todos los tickets de un cliente
     * 
     * @param id id del cliente
     * @return  lista con los tickets del cliente
     */
    @ApiOperation(value = "Recupera tickets", notes="Recupera todos los tickets de un cliente")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperaron todos los tickets del cliente exitosamente "),
        @ApiResponse(code = 404,message = "Error al recuperar los Ticket: No se encontró el id del cliente"),
        @ApiResponse(code = 500,message = "Error al recuperar los Tickets")
    })
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/clientes/{id}/tickets",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <List<TicketDto>> ticketsCliente(
        @ApiParam(name = "id", value = "Identificador del cliente", required = true,example = "0")
        @PathVariable("id") Long id){
        log.info("Recuperando los tickets del cliente con id: " + id);
        try {
            List<TicketDto> ticketDtos = servicioTicketDto.ticketsCliente(id);
            return ResponseEntity.status(HttpStatus.OK).body(ticketDtos);
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
     * Recupera un ticket de clietne
     * @param id del cliente 
     * @param idT del ticket
     * @return
     */

    @ApiOperation(value = "Recupera ticket id", notes="Recupera el ticket de un cliente")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperó el ticket del cliente exitosamente "),
        @ApiResponse(code = 404,message = "Error al recuperar el Ticket: No se encuentra el ticket o el cliente"),
        @ApiResponse(code = 500,message = "Error al recuperar el Ticket")
    })
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/clientes/{id}/tickets/{idT}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <TicketDto> ticketIdCliente(
        @ApiParam(name = "id", value = "Identificador del cliente", required = true,example = "0")
        @PathVariable("id") Long id,
        @ApiParam(name = "idT", value = "Identificador del Ticket", required = true,example = "0")
        @PathVariable("idT") Long idT){
        log.info("Recuperando el ticket "+idT+ " del cliente con id: " + id);
        try {
            TicketDto ticketDtos = servicioTicketDto.ticketId(id,idT);
            return ResponseEntity.status(HttpStatus.OK).body(ticketDtos);
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
     * Elmina un ticket con idTicket, idNegocio e idCliente
     * @param idC id del cliente
     * @param idN id del negocio
     * @param idT id del Ticket
     * @return string verificando la eliminación 
     */
    @ApiOperation(value = "Elimina un ticket con id", notes="Elimina un ticket con su id, id negocio y id cliente")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se eliminó el ticket exitosamente"),
        @ApiResponse(code = 404,message = "Error al eliminar ticket: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al eliminar ticket")
    })
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/cliente/{idC}/negocios/{idN}/tickets/{idT}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
        @ApiParam(name = "idC", value = "id del cliente",example = "0")    
        @PathVariable("idC") Long idC,
        @ApiParam(name = "idN", value = "id del negocio",example = "0")    
        @PathVariable("idN") Integer idN,
        @ApiParam(name = "idT", value = "id del ticket que se quiere eliminar",example = "0")    
        @PathVariable("idT") Long idT
        ) {
        log.info("RestController: Eliminando el ticket con id: " + idT);
        try {
            String msj = servicioTicketDto.eliminaTicketUsNe(idC,idN,idT);
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
