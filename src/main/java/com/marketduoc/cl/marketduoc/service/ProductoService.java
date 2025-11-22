package com.marketduoc.cl.marketduoc.service;

import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {
@Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).get();
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto patchProducto(Long id, Producto parcialProducto){
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            
            Producto productoToUpdate = productoOptional.get();
            
            if (parcialProducto.getNombre() != null) {
                productoToUpdate.setNombre(parcialProducto.getNombre());   
            }

            if(parcialProducto.getContenido() != null) {
                productoToUpdate.setContenido(parcialProducto.getContenido());
            }

            if(parcialProducto.getFechaCreacion() != null) {
                productoToUpdate.setFechaCreacion(parcialProducto.getFechaCreacion());
            }

            return productoRepository.save(productoToUpdate);
        } else {
            return null;
        }
    }

    public List<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    public List<Producto> findByUsuarioId(Integer usuarioId) {
        return productoRepository.findByUsuario_id(usuarioId);
    }

    public Producto findByNombreAndFechaCreacion(String nombre, Date fechaCreacion) {
        return productoRepository.findByNombreAndFechaCreacion(nombre, fechaCreacion);
    }

    public List<Producto> findByUsuarioAndNombre(Usuario usuario, String nombre) {
        return productoRepository.findByUsuarioAndNombre(usuario, nombre);
    }

    public List<Object[]> contarProductosPorEstadoYCategoria() {
        return productoRepository.contarProductosPorEstadoYCategoria();
    }

    public List<Producto> buscarPorTipoDeUsuarioYEstadoDisponible(String tipoNombre) {
        return productoRepository.buscarPorTipoDeUsuarioYEstadoDisponible(tipoNombre);
    }

    public List<Producto> buscarPorCategoriaEstadoYUsuario(String categoriaNombre, String estadoNombre, String usuarioNombre) {
        return productoRepository.buscarPorCategoriaEstadoYUsuario(categoriaNombre, estadoNombre, usuarioNombre);
    }
}
