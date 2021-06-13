package com.a6z.myticket.data.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

import com.a6z.myticket.negocio.models.Cliente;

/** 
 * @author Alfonso6z
*/

@Data
public class ClienteDto {
        
    private long id;
    
    @NotEmpty
    private String nombre;
    
    @NotEmpty 
    private String email;
    
    private int contadorPromocion;

    @NotEmpty
    private long negocio;

    /**
     * Este método permite generar un DTO a partir de la entidad nota: es un método
     * de clase y no se necesita un objeto para invocarlo. Se invoca como
     * ClienteDto.crea(param)
     * 
     * @param cliente 
     * @return dto obtenido a partir de la entidad
     */
    public static ClienteDto creaDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setContadorPromocion(cliente.getContadorPromocion());
        dto.setNegocio(cliente.getNegocio().getId());
        return dto;
    }



}
