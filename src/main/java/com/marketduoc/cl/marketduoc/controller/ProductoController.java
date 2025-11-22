package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.marketduoc.cl.marketduoc.model.ProductoDTO;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.model.Categoria;
import java.util.Date;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Operaciones para publicar y gestionar productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos publicados")
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
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
    @Operation(summary = "Crear un nuevo producto desde Móvil", description = "Publica un producto recibiendo datos simples")
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoDTO dto) {
        Producto nuevo = new Producto();
        // Copiamos los datos simples que vienen del celular
        nuevo.setNombre(dto.getNombre());
        // Guardamos descripción y precio juntos en el contenido para no complicarnos
        nuevo.setContenido(dto.getContenido() + " (Precio: $" + dto.getPrecio() + ")");
        
        // --- CAMBIO CLAVE AQUÍ ---
        nuevo.setImagen(dto.getImagen());
        // -------------------------

        nuevo.setFechaCreacion(new Date());

        // TRUCO: Asignamos IDs fijos para cumplir con la base de datos
        // IMPORTANTE: Debes asegurarte de que existan el Usuario 1, Estado 1 y Categoría 1 en tu BD de Render después.

        Usuario usuario = new Usuario(); 
        usuario.setId(1L); // Asumimos que siempre lo crea el usuario con ID 1 (admin o genérico)
        nuevo.setUsuario(usuario);

        Estado estado = new Estado(); 
        estado.setId(1L); // Asumimos estado "Activo" o similar
        nuevo.setEstado(estado);

        Categoria categoria = new Categoria(); 
        categoria.setId(1L); // Asumimos una categoría por defecto
        nuevo.setCategoria(categoria);

        return ResponseEntity.status(201).body(productoService.save(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente", description = "Reemplaza completamente los datos de un producto dado su ID")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.save(producto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Modifica uno o varios campos de un producto existente")
    public ResponseEntity<Producto> actualizarParcialProducto(@PathVariable Long id, @RequestBody Producto parcialProducto) {
        Producto productoActualizado = productoService.patchProducto(id, parcialProducto);
        if (productoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Borra un producto del marketplace dado su ID")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}