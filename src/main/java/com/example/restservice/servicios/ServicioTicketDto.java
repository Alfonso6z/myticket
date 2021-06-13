package com.example.restservice.servicios;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.restservice.datos.ClienteRepository;
import com.example.restservice.datos.NegocioRepository;
import com.example.restservice.datos.ProductoRepository;
import com.example.restservice.datos.TicketRepository;
import com.example.restservice.datos.dto.ProductoDto;
import com.example.restservice.datos.dto.TicketDto;
import com.example.restservice.negocio.modelo.Cliente;
import com.example.restservice.negocio.modelo.Negocio;
import com.example.restservice.negocio.modelo.Producto;
import com.example.restservice.negocio.modelo.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Servicio ticket
 * @author Alfonso6z
 */
@Service
public class ServicioTicketDto {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * agrega un ticket
     * @param ticketDto
     * @return ticketDto
     */
    public TicketDto agregarTicket(TicketDto ticketDto){
        // busca negocio y cliente
        Optional <Negocio> optNegocio = negocioRepository.findById((int) ticketDto.getNegocio());
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del negocio");
		}
        Optional <Cliente> optCliente = clienteRepository.findById(ticketDto.getCliente());
		if(optCliente.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del cliente");
		}

        // Recupera negocio y cliente
        Negocio negocio = optNegocio.get();
        Cliente cliente = optCliente.get();

        // crea fecha de la hora que se genero el ticket
        java.util.Date d = new java.util.Date();
        Date fecha = new Date(d.getTime());

        // genera un ticket con sus atributos
        Ticket ticket = new Ticket();
        ticket.setFecha(fecha);
        ticket.setObservacion(ticketDto.getObservacion());
        ticket.setTotal(ticketDto.getTotal());
        ticket.setNegocio(negocio);
        ticket.setCliente(cliente);
        
        //busca los productos en la base de datos si existen los recupera, de lo contrario crea nuevos 
        for(ProductoDto productoFor: ticketDto.getProductos()){ 
            Producto productoI = productoRepository.findByNombre(productoFor.getNombre());
            if(productoI == null){
                Producto nuevoProducto = new Producto();
                nuevoProducto.setNegocio(negocio);
                nuevoProducto.setNombre(productoFor.getNombre());
                nuevoProducto.setPrecio(productoFor.getPrecio());
                // guarda el nuevo producto
                productoRepository.save(nuevoProducto);
                // coloca id en productoDto
                productoFor.setId(nuevoProducto.getId());
                // agrega el producto a ticket
                ticket.addProducto(nuevoProducto);
            }else{
                productoFor.setId(productoI.getId());
                ticket.addProducto(productoI);
            }
        }
        
        ticketRepository.save(ticket);

        ticketDto.setId(ticket.getId());
        ticketDto.setFecha(fecha);

        return ticketDto;
    }
    /**
     * 
     * @param id id del cliete
     * @return lista de tickets de el cliente con id
     */
    public List<TicketDto> ticketsCliente(Long id){
        Optional <Cliente> optCliente = clienteRepository.findById(id);
        if(optCliente.isEmpty()){
            throw new IllegalArgumentException("No se encontró el cliente");
        }
        List<TicketDto> ticketsDto = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findByClienteId(id)){
            ticketsDto.add(TicketDto.creaDto(ticket));
        }
        return ticketsDto;
    }

    public TicketDto ticketId(Long id, Long idT) {
        Optional <Cliente> optClietne = clienteRepository.findById(id);
        if(optClietne.isEmpty()){
            throw new IllegalArgumentException("No se encontro el client ese id");
        }
        Optional <Ticket> optTicket = ticketRepository.findById(idT);
        if (optTicket.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el ticket");
        }

        Cliente cliente = optClietne.get();
        Ticket ticket = optTicket.get();

        if(cliente == ticket.getCliente()){
            TicketDto ticktDto = TicketDto.creaDto(ticket);
            ticktDto.setId(ticket.getId());
            return ticktDto;
        }else{
            throw new IllegalArgumentException("No se encontró el ticket");
        }
    }
    public String eliminaTicketUsNe(Long idC, Integer idN, Long idT) {
        // Busca cliente

        Optional <Cliente> optCliente = clienteRepository.findById(idC);
        if(optCliente.isEmpty()){
            throw new IllegalArgumentException("No se encontro el Cliente con ese id");
        }

        Optional <Negocio> optNegocio = negocioRepository.findById(idN);
        if(optNegocio.isEmpty()){
            throw new IllegalArgumentException("No se encontro el negocio con ese id");
        }

        Optional <Ticket> optTicket = ticketRepository.findById(idT);
        if(optTicket.isEmpty()){
            throw new IllegalArgumentException("No se encontro el Ticket con ese id");
        }
        Cliente cliente = optCliente.get();
        Negocio negocio = optNegocio.get();
        Ticket ticket = optTicket.get();

        if(ticket.getCliente() == cliente && ticket.getNegocio() == negocio){
            ticketRepository.delete(ticket);
            return "Se elimino el ticket con el id : " + idT + " Negocio con id: " + idN + "  Cliente con id : " + idC;
        }else{
            throw new IllegalArgumentException("No se encontraron coicidencias entre client,negocio y ticket");
        }

        




    }

}
