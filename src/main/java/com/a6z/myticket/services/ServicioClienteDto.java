package com.a6z.myticket.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

import com.a6z.myticket.data.ClienteRepository;
import com.a6z.myticket.data.NegocioRepository;
import com.a6z.myticket.data.TicketRepository;
import com.a6z.myticket.data.dto.ClienteDto;
import com.a6z.myticket.data.dto.NegocioDto;
import com.a6z.myticket.negocio.models.Cliente;
import com.a6z.myticket.negocio.models.Negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 
 * Servicio de cliente
 * @author Alfonso6z
*/

@Service
public class ServicioClienteDto {
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private NegocioRepository negocioRepository;

	@Autowired
	private TicketRepository ticketRepository;
	 
	/**
	 * 
	 * Permite agregar un cliente
	 * @param clienteDto
	 * @return
	 */
	public ClienteDto agregaCliente(ClienteDto clienteDto) {
        /* Regla de negocio no se permite agregar 2 veces un cliente con el mismo correo */
		Cliente cliente = clienteRepository.findByEmail(clienteDto.getEmail());

		if (cliente != null) {
			throw new IllegalArgumentException("Ese cliente ya existe");
		}

		Optional <Negocio> optNegocio = negocioRepository.findById((int) clienteDto.getNegocio());
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del negocio");
		}

		Negocio negocio = optNegocio.get();
		// crea el cliente y guarda sus valores del dto
		cliente = new Cliente();
		cliente.setNombre(clienteDto.getNombre());
		cliente.setEmail(clienteDto.getEmail());
		cliente.setContadorPromocion(clienteDto.getContadorPromocion());
		cliente.setNegocio(negocio);
		
		// guarda en la base de datos
		clienteRepository.save(cliente);

		clienteDto.setId(cliente.getId());
		
		return clienteDto;

	}

	/**
	 * Recupera un cliente por su ID de la base de datos
	 * 
	 * @param id el ID del cliente que se desea recuperae
	 * @return un DTO de cliente con su información. Nulo si no se encontró.
	 */
	public ClienteDto recuperaClientePorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isEmpty()) {
			throw new IllegalArgumentException("No se encontró el cliente");
		}
		else {
			return ClienteDto.creaDto(cliente.get());
		}
	}
	
	/**
	 * Recupera los clientes
	 * @return lista de clientesDto
	 */

  public List<ClienteDto> recuperaClientes() {
	  
		List <ClienteDto> clientes = new ArrayList<>();

		for(Cliente cliente:clienteRepository.findAll()) {
			System.out.println(cliente.getId());
			clientes.add(ClienteDto.creaDto(cliente));
		}

		return clientes;
	}
	

	 /* Obtener una lista con los clientes pertenecientes a un negocio
	 * 
	 * @param negocioDto un DTO de negocio respresentando el negocio
	 * @return una lista de DTO de cliente que pertenecen al negocio
	 */
  
	public List<ClienteDto> clientesDelNegocio(NegocioDto negocioDto){
		
		List<ClienteDto> listaClientes = new ArrayList<ClienteDto>();
		Negocio negocio = negocioRepository.findByUsuario(negocioDto.getUsuario());
		
		for (Cliente cliente:clienteRepository.findAll()) {
			if (cliente.getNegocio() == negocio) {
				listaClientes.add(ClienteDto.creaDto(cliente));
			}
		}
		
		return listaClientes;
	}
	
	/**
	 * Recupera todos los clientes almacenados en la BD
	 * 
	 * @return una lista de DTO de cliente con todos los clientes
	 */
	public List<ClienteDto> recuperaTodos(){
		List<ClienteDto> clientes = new ArrayList<ClienteDto>();
		clienteRepository.findAll().forEach(cliente -> clientes.add(ClienteDto.creaDto(cliente)));
		return clientes;
	}

	public ClienteDto actualizaCliente(long id,ClienteDto clienteDto){

		Optional <Cliente> optCliente = clienteRepository.findById(id);
		if(optCliente.isEmpty()){
			throw new IllegalArgumentException("No se encontro el cliente con ese id");
		}
		Cliente cliente = optCliente.get();

		Optional <Negocio> optNegocio = negocioRepository.findById((int) clienteDto.getNegocio());
		if (optNegocio.isEmpty()) {
			throw new IllegalArgumentException("No se encontró el negocio");
		}
		Negocio negocio = optNegocio.get();

		cliente.setNombre(clienteDto.getNombre());
		cliente.setContadorPromocion(clienteDto.getContadorPromocion());
		cliente.setEmail(clienteDto.getEmail());
		cliente.setNegocio(negocio);
		
		clienteRepository.save(cliente);

		clienteDto.setId(cliente.getId());
		
		return clienteDto;
	}

	public String elimiinaClienteId(Long id){
		// Busca el usuario por id
		Optional <Cliente> optCliente = clienteRepository.findById(id);
		if(optCliente.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Cliente cliente = optCliente.get();
		// Elimina sus tickets
		ticketRepository.deleteAll(ticketRepository.findByClienteId(cliente.getId()));
		// Elimina el cliente
		cliente.setNegocio(null);
		clienteRepository.save(cliente);
		clienteRepository.delete(cliente);
		
		return "Se elimino el cliente con el id : " + id;
	}

    
}
