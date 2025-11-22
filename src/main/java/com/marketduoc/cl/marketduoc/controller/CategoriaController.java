package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "API para la administración de categorías de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Devuelve una lista con todas las categorías disponibles")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Recupera una categoría según su ID")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Registra una nueva categoría en el sistema")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Reemplaza completamente los datos de una categoría")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria actualizada = categoriaService.save(categoria);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente categoría", description = "Modifica algunos datos de una categoría")
    public ResponseEntity<Categoria> actualizarParcialCategoria(@PathVariable Long id, @RequestBody Categoria parcial) {
        Categoria existente = categoriaService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        if (parcial.getNombre() != null) {
            existente.setNombre(parcial.getNombre());
        }
        Categoria actualizada = categoriaService.save(existente);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        Categoria existente = categoriaService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
