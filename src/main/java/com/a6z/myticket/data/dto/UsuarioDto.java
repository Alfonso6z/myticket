package com.a6z.myticket.data.dto;
import javax.validation.constraints.NotEmpty;

import com.a6z.myticket.negocio.models.Usuario;

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
