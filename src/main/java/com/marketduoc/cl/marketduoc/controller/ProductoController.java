package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.model.ProductoDTO;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.service.ProductoService;
import com.marketduoc.cl.marketduoc.repository.UsuarioRepository;
import com.marketduoc.cl.marketduoc.repository.CategoriaRepository;
import com.marketduoc.cl.marketduoc.repository.EstadoRepository;
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

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de productos formateada para la App")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        
        // Convertimos la lista de la base de datos a DTOs para la App
        List<ProductoDTO> dtos = productos.stream().map(p -> {
            ProductoDTO dto = new ProductoDTO();
            dto.setNombre(p.getNombre());
            dto.setContenido(p.getContenido());
            dto.setPrecio(p.getPrecio() != null ? p.getPrecio() : 0);
            dto.setImagen(p.getImagen());
            
            // Si tiene usuario, mandamos su correo, si no, "Anónimo"
            dto.setEmailUsuario(p.getUsuario() != null ? p.getUsuario().getCorreo() : "Anónimo");
            
            // Si tiene categoría, mandamos su ID, si no, 1 (Tecnología)
            dto.setCategoriaId(p.getCategoria() != null ? p.getCategoria().getId() : 1L);
            
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Recupera un producto dado su identificador")
    public ResponseEntity<Producto> buscarProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Publica un producto vinculándolo al usuario y categoría real")
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoDTO dto) {
        Producto nuevo = new Producto();
        nuevo.setNombre(dto.getNombre());
        nuevo.setContenido(dto.getContenido());
        nuevo.setPrecio(dto.getPrecio());
        nuevo.setImagen(dto.getImagen());
        nuevo.setFechaCreacion(new Date());

        // 1. Buscamos al usuario por su correo
        Usuario usuario = usuarioRepository.findByCorreo(dto.getEmailUsuario());
        if (usuario == null) {
            // Si no existe, asignamos el usuario 1 por seguridad
            usuario = new Usuario();
            usuario.setId(1L);
        }
        nuevo.setUsuario(usuario);

        // 2. Buscamos la categoría
        Long catId = dto.getCategoriaId() != null ? dto.getCategoriaId() : 1L;
        Categoria categoria = categoriaRepository.findById(catId).orElse(null);
        if (categoria == null) {
            categoria = new Categoria();
            categoria.setId(1L);
        }
        nuevo.setCategoria(categoria);

        // 3. Asignamos estado "Disponible" (ID 1)
        Estado estado = estadoRepository.findById(1L).orElse(null);
        if (estado == null) {
             estado = new Estado(); 
             estado.setId(1L);
        }
        nuevo.setEstado(estado);

        return ResponseEntity.status(201).body(productoService.save(nuevo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Borra un producto del marketplace dado su ID")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}