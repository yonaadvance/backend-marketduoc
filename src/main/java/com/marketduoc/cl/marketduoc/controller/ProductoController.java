package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.*;
import com.marketduoc.cl.marketduoc.repository.*;
import com.marketduoc.cl.marketduoc.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Operaciones para publicar y gestionar productos")
public class ProductoController {

    @Autowired private ProductoService productoService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private EstadoRepository estadoRepository;
    
    // Usamos el nombre correcto de tu repositorio
    @Autowired private TipoUsuarioRepository tipoUsuarioRepository;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        List<ProductoDTO> dtos = productos.stream().map(p -> {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(p.getId());
            dto.setNombre(p.getNombre());
            dto.setContenido(p.getContenido());
            dto.setPrecio(p.getPrecio() != null ? p.getPrecio() : 0);
            dto.setImagen(p.getImagen());
            dto.setEmailUsuario(p.getUsuario() != null ? p.getUsuario().getCorreo() : "Anónimo");
            dto.setCategoriaId(p.getCategoria() != null ? p.getCategoria().getId() : 1L);
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoDTO dto) {
        Producto nuevo = new Producto();
        nuevo.setNombre(dto.getNombre());
        nuevo.setContenido(dto.getContenido());
        nuevo.setPrecio(dto.getPrecio());
        nuevo.setImagen(dto.getImagen());
        nuevo.setFechaCreacion(new Date());

        // --- LÓGICA DE USUARIOS ---
        String email = dto.getEmailUsuario();
        if (email == null || email.isEmpty()) email = "anonimo@marketduoc.cl";

        Usuario usuario = usuarioRepository.findByCorreo(email);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setCorreo(email);
            usuario.setNombre("Nuevo");
            usuario.setApellidos("Usuario");
            usuario.setContraseña("123456");

            // Buscamos el TipoUsuario (Estudiante)
            // Nota: Usamos 1L porque en tu repositorio extiendes de JpaRepository<TipoUsuario, Long>
            TipoUsuario tipo = tipoUsuarioRepository.findById(1L).orElse(null);
            
            if (tipo == null) {
                tipo = new TipoUsuario();
                tipo.setNombre("Estudiante");
                tipo = tipoUsuarioRepository.save(tipo);
            }
            
            // Aquí usamos el setter correcto basado en tu modelo Usuario.java
            usuario.setTipoUsuario(tipo);
            
            usuario = usuarioRepository.save(usuario);
        }
        nuevo.setUsuario(usuario);

        // --- LÓGICA DE CATEGORÍAS ---
        Long catId = dto.getCategoriaId() != null ? dto.getCategoriaId() : 1L;
        Categoria categoria = categoriaRepository.findById(catId).orElse(null);
        
        if (categoria == null) {
            // Si no existe, intentamos buscar la 1 por defecto
            categoria = categoriaRepository.findById(1L).orElse(null); 
            if (categoria == null) {
                // Solo si NO hay nada en la base de datos, creamos la 1
                categoria = new Categoria();
                categoria.setNombre("Tecnología");
                categoria = categoriaRepository.save(categoria);
            }
        }
        nuevo.setCategoria(categoria);

        // Estado
        Estado estado = estadoRepository.findById(1L).orElse(null);
        if (estado == null) {
             estado = new Estado();
             estado.setNombre("Disponible");
             estado = estadoRepository.save(estado);
        }
        nuevo.setEstado(estado);

        return ResponseEntity.status(201).body(productoService.save(nuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}