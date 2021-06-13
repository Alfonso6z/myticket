package com.example.restservice.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.restservice.datos.NegocioRepository;
import com.example.restservice.datos.ProductoRepository;
import com.example.restservice.datos.dto.ProductoDto;
import com.example.restservice.negocio.modelo.Negocio;
import com.example.restservice.negocio.modelo.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Sevicio Productos
 * @author Alfonso6z
 */
@Service
public class ServicioProductoDto {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    /**
     * agrega un producto
     * @param productoDto
     * @return productoDto
     */

    public ProductoDto agregarProducto(ProductoDto productoDto,int idNegocio){

        System.out.println(productoDto.getPrecio());
        Producto productoDb = productoRepository.findByNombre(productoDto.getNombre());
        if(productoDb != null){
            throw new IllegalArgumentException("Este producto ya esta registrado");
        }
        Optional <Negocio> optNegocio = negocioRepository.findById((int) idNegocio);
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del negocio");
		}
        Negocio negocio = optNegocio.get();
        Producto producto = new Producto();
        producto.setNegocio(negocio);
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());

        productoRepository.save(producto);
        productoDto.setId(producto.getId());
        productoDto.setNegocio(negocio.getId());

        return productoDto;
    }

    public List<ProductoDto> recuperaProductosNegocio(Integer id) {
        List<ProductoDto> productoDtos = new ArrayList<>();
        for(Producto producto: productoRepository.findAll()){
            if(producto.getNegocio().getId() == id){
                productoDtos.add(ProductoDto.creaDto(producto));
            }
        }
        if(productoDtos.size()>0){
            return productoDtos;
        }else{
            throw new IllegalArgumentException("No se encontraron productos en ese negocio");
        }
    }

    public ProductoDto actualizaProducto(Integer idN, Long idP, ProductoDto productoActualizado) {

        Optional <Negocio> optNegocio = negocioRepository.findById(idN);
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontro el cliente con ese id");
		}
		Optional <Producto> optProducto = productoRepository.findById(idP);
		if (optProducto.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el negocio");
		}
		Negocio negocio = optNegocio.get();
        Producto producto = optProducto.get();

        if(negocio == producto.getNegocio()){
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setNegocio(negocio);
            productoRepository.save(producto);

            productoActualizado.setId(producto.getId());
            productoActualizado.setNegocio(negocio.getId());
            return productoActualizado;
        }else{
            throw new IllegalArgumentException("No se encontró el producto del negocio");
        }
		
    }

    public String eliminaProducto(Integer id, Long idP) {
        // Busca el usuario por id
		Optional <Negocio> optNegocio = negocioRepository.findById(id);
		if(optNegocio.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Optional <Producto> optProducto = productoRepository.findById(idP);
		if(optProducto.isEmpty()){
			throw new IllegalArgumentException("No se encontró el id del usuario");
		}
		Negocio negocio = optNegocio.get();
		Producto producto = optProducto.get();
		// Elimina el negocio 
		if(negocio == producto.getNegocio()){
			productoRepository.delete(producto);
			return "Se elimino el producto con el id : " + idP + " Negocio con id: " +id;
		}else{
			throw new IllegalArgumentException("No se encontró coincidenci entre negocio y producto");
		}
    }
}
