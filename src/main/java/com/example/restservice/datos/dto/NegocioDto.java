package com.example.restservice.datos.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.restservice.negocio.modelo.Negocio;
import com.example.restservice.negocio.modelo.Usuario;

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
