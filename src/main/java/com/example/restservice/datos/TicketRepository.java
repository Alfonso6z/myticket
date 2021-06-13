package com.example.restservice.datos;
import java.util.List;

import com.example.restservice.negocio.modelo.Ticket;
import org.springframework.data.repository.CrudRepository;


/**
 * Repositorio para tickets
 * @author Alfonso6z
 */

public interface TicketRepository extends CrudRepository <Ticket, Long> {

    public List <Ticket> findByClienteId(Long cliente_id);
    public List <Ticket> findAlldByNegocioId(Integer negocio_id);
    
}