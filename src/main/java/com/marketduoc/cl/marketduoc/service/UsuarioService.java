package com.marketduoc.cl.marketduoc.service;

import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.repository.UsuarioRepository;
import com.marketduoc.cl.marketduoc.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado"));
        
        // CORRECCIÓN: Pasamos 'id' directamente, ya que es Long. No usamos .intValue()
        List<Producto> productos = productoRepository.findByUsuario_id(id);
        
        for (Producto p : productos) {
            productoRepository.delete(p);
        }
        usuarioRepository.delete(usuario);
    }

    public Usuario patchUsuario(Long id, Usuario parcialUsuario) {
        return usuarioRepository.findById(id)
                .map(usuarioToUpdate -> {
                    if (parcialUsuario.getNombre() != null) {
                        usuarioToUpdate.setNombre(parcialUsuario.getNombre());
                    }
                    if (parcialUsuario.getApellidos() != null) {
                        usuarioToUpdate.setApellidos(parcialUsuario.getApellidos());
                    }
                    if (parcialUsuario.getCorreo() != null) {
                        usuarioToUpdate.setCorreo(parcialUsuario.getCorreo());
                    }
                    return usuarioRepository.save(usuarioToUpdate);
                })
                .orElse(null);
    }

    public List<Usuario> findByApellidos(String apellidos) {
        return usuarioRepository.findByApellidos(apellidos);
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<Usuario> findByNombreAndApellidos(String nombre, String apellidos) {
        return usuarioRepository.findByNombreAndApellidos(nombre, apellidos);
    }

    public Usuario findByNombreAndCorreo(String nombre, String correo) {
        return usuarioRepository.findByNombreAndCorreo(nombre, correo);
    }

    public List<Usuario> buscarPorTipoYCategoriaDeProducto(String tipoNombre, String categoriaNombre) {
        return usuarioRepository.buscarPorTipoYCategoriaDeProducto(tipoNombre, categoriaNombre);
    }
}