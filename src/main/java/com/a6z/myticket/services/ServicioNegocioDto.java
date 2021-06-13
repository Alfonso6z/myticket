package com.a6z.myticket.services;

import com.a6z.myticket.data.ClienteRepository;
import com.a6z.myticket.data.NegocioRepository;
import com.a6z.myticket.data.ProductoRepository;
import com.a6z.myticket.data.TicketRepository;
import com.a6z.myticket.data.UsuarioRepository;
import com.a6z.myticket.data.dto.NegocioDto;
import com.a6z.myticket.data.dto.UsuarioDto;
import com.a6z.myticket.negocio.models.Negocio;
import com.a6z.myticket.negocio.models.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ServicioNegocioDto {
	@Autowired
	NegocioRepository negocioRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	ProductoRepository productoRepository;

	
	/**
	 * Registra (guarda) unnuevo negocio en la BD
	 * 
	 * @param nuevoNegocioDto un DTO de negocio con los datos pertinentes
	 * @return el negocio nuevo con su ID generada por el sistema. Nulo si hubo un error.
	 */
	public NegocioDto registrarNegocio(NegocioDto nuevoNegocioDto) {
		//REGLA DE NEGOCIO: No pueden haber dos negocios que se llamen igual con el mismo usuario.
		Negocio negocioPorUsuario = negocioRepository.findByUsuario(nuevoNegocioDto.getUsuario());
		Negocio negocioPorNombre = negocioRepository.findByNombre(nuevoNegocioDto.getNombre());
		
		if (negocioPorUsuario != null && negocioPorNombre != null && negocioPorUsuario == negocioPorNombre) {
			return null;
		}
		else {
			Negocio nuevoNegocio = new Negocio();
			
			nuevoNegocio.setNombre(nuevoNegocioDto.getNombre());
			nuevoNegocio.setServicio(nuevoNegocioDto.getServicio());
			nuevoNegocio.setUsuario(nuevoNegocioDto.getUsuario());
			
			Negocio negocioNuevo = negocioRepository.save(nuevoNegocio);
			
			return NegocioDto.creaDto(negocioNuevo);
		}
	}
	
	/**
	 * Obtener una lista con los negocios pertenecientes a un usuario
	 * 
	 * @param usuarioDto un DTO de usuario respresentando el dueño de los negocios
	 * @return una lista de DTO de negocio que pertenecen al usuario
	 */
	public List<NegocioDto> negociosDelUsuario(UsuarioDto usuarioDto){
		
		List<NegocioDto> listaNegocios = new ArrayList<NegocioDto>();
		Usuario usuario = usuarioRepository.findById(usuarioDto.getId()).get();
		
		for(Negocio negocio:negocioRepository.findAll()) {
			if(negocio.getUsuario() == usuario) {
				listaNegocios.add(NegocioDto.creaDto(negocio));
			}
		}
		
		return listaNegocios;
	}
	
	/**
	 * recupera un negocio de la BD dado su ID
	 * 
	 * @param id el ID del negocio
	 * @return el negocio si fue encontrado, nulo si no.
	 */
	public NegocioDto recuperaPorId(Integer id) {
		Optional<Negocio> negocio = negocioRepository.findById(id);
		if(negocio.isEmpty()) {
			return null;
		}
		else {
			return NegocioDto.creaDto(negocio.get());
		}
	}

	public NegocioDto recuperaNegocioIds(Integer idU, Integer idN) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(idU);
		if(optUsuario.isEmpty()) {
			throw new IllegalArgumentException("No se encontró el negocio");
		}
		Optional<Negocio> optNegocio = negocioRepository.findById(idN);
		if(optNegocio.isEmpty()) {
			throw new IllegalArgumentException("No se encontró el negocio");
		}
		Negocio negocio = optNegocio.get();
		Usuario usuario = optUsuario.get();
		if(negocio.getUsuario() == usuario){
			NegocioDto negocioDto = NegocioDto.creaDto(negocio);
			negocioDto.setUsuario(usuario);
			return negocioDto;
		}else{
			throw new IllegalArgumentException("No se encontró el negocio");
		}

	}


	public NegocioDto registraNegocioIdUsuario(long id, NegocioDto negocioDto){

		Optional <Usuario> optUsuario = usuarioRepository.findById((int) id);
		if(optUsuario.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Negocio negocioDB = negocioRepository.findByNombre(negocioDto.getNombre());
		if(negocioDB != null){
			throw new IllegalArgumentException("No se puede registrar dos negocios con el mismo nombre");
		}
		Usuario usuario = optUsuario.get();

		Negocio negocio = new Negocio();
		negocio.setNombre(negocioDto.getNombre());
		negocio.setServicio(negocioDto.getServicio());
		negocio.setUsuario(usuario);

		negocioRepository.save(negocio);

		negocioDto.setId(negocio.getId());
		negocioDto.setUsuario(usuario);
		return negocioDto;
	}

	public NegocioDto actualizaNegocio( Integer idU,Integer idN, NegocioDto negocioA){

			Optional <Usuario> optUsuario = usuarioRepository.findById(idU);
			if(optUsuario.isEmpty()){
				throw new IllegalArgumentException("No se encontro el usuario con ese id");
			}
			Optional <Negocio> optNegocio = negocioRepository.findById(idN);
			if (optNegocio.isEmpty()) {
				throw new IllegalArgumentException("No se encontró el negocio");
			}
			
			Usuario usuario = optUsuario.get();
			Negocio negocio = optNegocio.get();
			if(usuario == negocio.getUsuario()){
				negocio.setNombre(negocioA.getNombre());
				negocio.setServicio(negocioA.getServicio());
				negocio.setUsuario(usuario);
				negocioRepository.save(negocio);

				negocioA.setUsuario(usuario);
				negocioA.setId(negocio.getId());

				return negocioA;
			}else{
				throw new IllegalArgumentException("No se encontró coincidenci entre usuario y negocio");
			}
		
	}


	public String elimiinaNegocioId(Integer idU,Integer idN){
		// Busca el usuario por id
		Optional <Negocio> optNegocio = negocioRepository.findById(idN);
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Optional <Usuario> optUsuario = usuarioRepository.findById(idU);
		if(optUsuario.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Negocio negocio = optNegocio.get();
		Usuario usuario = optUsuario.get();
		// Elimina el negocio 
		if(usuario == negocio.getUsuario()){
			ticketRepository.deleteAll(ticketRepository.findAlldByNegocioId(negocio.getId()));
			productoRepository.deleteAll(productoRepository.findAllByNegocio(negocio));
			clienteRepository.deleteAll(clienteRepository.findAllByNegocio(negocio));

			negocioRepository.delete(negocio);
			return "Se elimino el negocio con el id : " + idN + "  Usuario con id: " +idU;
		}else{
			throw new IllegalArgumentException("No se encontró coincidenci entre negocio y usuario");
		}
		
	}
}
