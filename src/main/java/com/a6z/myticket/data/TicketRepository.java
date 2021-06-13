package com.a6z.myticket.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.a6z.myticket.negocio.models.Ticket;


/**
 * Repositorio para tickets
 * @author Alfonso6z
 */

public interface TicketRepository extends CrudRepository <Ticket, Long> {

    public List <Ticket> findByClienteId(Long cliente_id);
    public List <Ticket> findAlldByNegocioId(Integer negocio_id);
    
}