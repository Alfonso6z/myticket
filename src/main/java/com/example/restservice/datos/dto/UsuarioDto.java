package com.example.restservice.datos.dto;
import javax.validation.constraints.NotEmpty;

import com.example.restservice.negocio.modelo.Usuario;

import lombok.Data;

@Data
public class UsuarioDto {
    private Integer id;

    @NotEmpty
    private String nombre;
    
    @NotEmpty
    private String email;

    public static UsuarioDto creaDto(Usuario user) {
        UsuarioDto dto = new UsuarioDto();

		dto.setId(user.getId());
		dto.setNombre(user.getNombre());
		dto.setEmail(user.getEmail());

		return dto;
    }
}
