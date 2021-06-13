package com.a6z.myticket.controllers.producto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.a6z.myticket.data.dto.ProductoDto;
import com.a6z.myticket.services.ServicioProductoDto;
/**
 * CRUD de producos
 * @author Alfonso6z
 */

@RestController
@RequestMapping("/v1")
@Slf4j
public class ProductoController {
    
    @Autowired
    private ServicioProductoDto servicioProductoDto;
    /**
     * Agrega un nuevo producto
     * @param nuevoProducto productoDto agregar
     * @param id del negocio que agrega el producto
     * @return
     */
    @ApiOperation(value = "Registra un producto", notes="Registra un producto con id de negocio")
    @ApiResponses(value={
        @ApiResponse(code = 201,message = "Producto creado exitosamente"),
        @ApiResponse(code = 400,message = "Error al registrar Producto: El servidor no puede o no procesará la petición debido a algo que es percibido como un error del cliente"),
        @ApiResponse(code = 500,message = "Error al registrar Producto")
    })
    @CrossOrigin(origins="*")
    @PostMapping(path = "/negocios/{id}/productos",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> creaProducto(
        @RequestBody ProductoDto nuevoProducto,
        @ApiParam(name="id",value = "identificador de negocio",required = true,example = "0")
        @PathVariable ("id") int id){
        log.info("RestController: Agregando nuevo Producto");
        try {
            ProductoDto productoDto = servicioProductoDto.agregarProducto(nuevoProducto,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoDto);
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
     * Recupera los productos de un negocio
     * @param id del negocio
     * @return productos de un negocio especifico
     */
    @ApiOperation(value = "Recupera todos los productos de un negocio", notes="Recupera todos los productos en un arreglo DTO")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se recuperaron los productos exitosamente"),
        @ApiResponse(code = 404,message = "Error al recuperar los producto: no se encontraron productos de ese negocio"),
        @ApiResponse(code = 500,message = "Error al recuperar los productos",response=List.class)
    })
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/negocios/{id}/productos",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <List<ProductoDto>> todosProductosNegocio(
        @ApiParam(name = "id", value = "id del negocio",example = "0")
        @PathVariable("id") Integer id) {
        log.info("RestController: Recuperando todos los productos");
        try {
            List<ProductoDto> clienteDto = servicioProductoDto.recuperaProductosNegocio(id);
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
     * Actualiza el producto de un negocio
     * @param id del negocio
     * @param idP del producto
     * @param productoActualizado productoDto con los nuevos valores
     * @return dto producto actualizado
     */
    @ApiOperation(value = "Actualiza un producto con id", notes="Actualiza un producto con su id y id negocio")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se actualizó el producto exitosamente"),
        @ApiResponse(code = 404,message = "Error al actualizar producto: No se encontró el producto que se quiere actualizar"),
        @ApiResponse(code = 500,message = "Error al actualizar producto")
    })
    @CrossOrigin(origins = "*")
    @PutMapping(path = "/negocios/{id}/productos/{idP}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> update(
        @ApiParam(name = "id", value = "id del negocio",example = "0")
        @PathVariable("id") Integer id,
        @ApiParam(name = "idP", value = "id del producto",example = "0")
        @PathVariable("idP") Long idP,
        @RequestBody ProductoDto productoActualizado) {
        log.info("RestController: Actualizando cliente id" + id);
        try {
            ProductoDto productoDto= servicioProductoDto.actualizaProducto(id,idP,productoActualizado);
            return ResponseEntity.status(HttpStatus.OK).body(productoDto);
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
     * Elimina un producto
     * @param id del negocio
     * @param idP del producto
     * @return string con la información del producto eliminado
     */
    @ApiOperation(value = "Elimina un producto con id", notes="Elimina un producto con su id, si no se ha usado para generar un ticket")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Se eliminó el producto exitosamente"),
        @ApiResponse(code = 404,message = "Error al eliminar producto: No se enctontró el producto que se desea eliminar"),
        @ApiResponse(code = 500,message = "Error al eliminar producto")
    })
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/negocios/{id}/productos/{idP}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
        @ApiParam(name = "id", value = "id del negocio",example = "0")
        @PathVariable("id") Integer id,
        @ApiParam(name = "idP", value = "id del producto",example = "0")
        @PathVariable("idP") Long idP) {
        log.info("RestController: Eliminando producto con id: " + id);
        try {
            String msj = servicioProductoDto.eliminaProducto(id,idP);
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