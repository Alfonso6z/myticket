package com.a6z.myticket.data.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.a6z.myticket.negocio.models.Negocio;
import com.a6z.myticket.negocio.models.Usuario;

import lombok.Data;

@Data
public class NegocioDto {
	private Integer id;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String servicio;

	@NotNull
	private Usuario usuario;
	/**
	 * Crea dto de negocio
	 * @param negocio
	 * @return
	 */
	public static NegocioDto creaDto(Negocio negocio) {
		NegocioDto dto = new NegocioDto();
		
		dto.setId(negocio.getId());
		dto.setNombre(negocio.getNombre());
		dto.setServicio(negocio.getServicio());
		dto.setUsuario(negocio.getUsuario());
		
		return dto;
	}
}
