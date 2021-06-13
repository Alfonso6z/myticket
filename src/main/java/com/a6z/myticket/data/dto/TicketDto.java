package com.a6z.myticket.data.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

import com.a6z.myticket.negocio.models.Producto;
import com.a6z.myticket.negocio.models.Ticket;

import lombok.Data;
/**
 * Clase DTO de la entidad Ticket
 * @author Alfonso6z
 */
@Data
public class TicketDto {

    private long id;

    @NotNull
	private Date fecha;

    @NotEmpty
	private String observacion;

    @NotEmpty
	private Double total;

    @NotEmpty
	private long negocio;

    @NotEmpty
	private long cliente;

    @NotNull
    private List<ProductoDto> productos;

    /**
     * Crea dto de ticket
     * @param ticket de la entidad ticket
     * @return dto optenido de la entidad
     */
    public static TicketDto creaDto(Ticket ticket){
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setFecha(ticket.getFecha());
        dto.setObservacion(ticket.getObservacion());
        dto.setTotal(ticket.getTotal());
        dto.setNegocio(ticket.getNegocio().getId());
        dto.setCliente(ticket.getCliente().getId());
        dto.setProductos(dameProductosDto(ticket.getProductos()));

        return dto;
    }
    /**
     * Crea una lista de productosDto
     * @param productoL lista de productos
     * @return
     */
    private static List<ProductoDto> dameProductosDto(List<Producto> productoL){
        List<ProductoDto> p = new ArrayList<>();
        for (Producto producto : productoL){
            p.add(ProductoDto.creaDto(producto));
        }
        return p;
    }
}
