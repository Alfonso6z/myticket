package com.a6z.myticket.controllers.login;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.a6z.myticket.data.dto.UsuarioDto;
import com.a6z.myticket.negocio.models.Usuario;
import com.a6z.myticket.services.ServicioUsuarioDto;
/**
 * identificar si existe el usuario
 * @author Alfonso6z 
 */
@RestController
@RequestMapping("/v1")
public class LoginRestController {
    
    @Autowired
    private ServicioUsuarioDto servicioUsuarioDto;

    /**
     * Método encuentra el email de un usuario en la base de datos
     *
     * @param auth datos del usuario
     * @return
     */
    @ApiOperation(value = "Login", notes="Login")
    @ApiResponses(value={
        @ApiResponse(code = 200,message = "Usuario recuperado exitosamente"),
        @ApiResponse(code = 404,message = "Error al recuperar usuario : No se encontró el usuario"),
        @ApiResponse(code = 500,message = "Error al recuperar usuario")
    })
    @CrossOrigin(origins = "*")
    @PostMapping(path = {"/","/login","usuarios/auth"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Usuario auth){
        try {
            UsuarioDto uauarioDto = servicioUsuarioDto.recuperaUsuarioPorEmail(auth.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(uauarioDto);
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