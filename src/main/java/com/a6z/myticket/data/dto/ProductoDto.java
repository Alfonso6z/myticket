package com.a6z.myticket.data.dto;

import javax.validation.constraints.NotEmpty;

import com.a6z.myticket.negocio.models.Producto;

import lombok.Data;

/**
 * 
 * Clase DTO de la entidad producto
 * @author Alfonso6z
 */

@Data
public class ProductoDto {
    
    private long id;
    
    @NotEmpty
    private String nombre;

    @NotEmpty
    private double precio;

    @NotEmpty
    private long negocio;

    /**
     * Crea dto de producto
     * @param producto de la entidad producto
     * @return dto de producto
     */

    public static ProductoDto creaDto(Producto producto){
        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setNegocio(producto.getNegocio().getId());
        return dto;
    }

}
