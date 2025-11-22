package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.assemblers.CategoriaModelAssembler;
import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/categorias")
@Tag(name = "Categorias v2", description = "Operaciones con representación HATEOAS para categorías")
public class CategoriaControllerV2 {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar categorías con HATEOAS", description = "Devuelve todas las categorías con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> getAllCategorias() {
        List<EntityModel<Categoria>> categorias = categoriaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaControllerV2.class).getAllCategorias()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría específica con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Categoria>> getCategoriaById(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una categoría", description = "Agrega una nueva categoría y devuelve su representación HATEOAS")
    public ResponseEntity<EntityModel<Categoria>> createCategoria(@RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.save(categoria);
        return ResponseEntity.created(
                linkTo(methodOn(CategoriaControllerV2.class).getCategoriaById(nueva.getId().longValue())).toUri())
                .body(assembler.toModel(nueva));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar categoría", description = "Reemplaza completamente los datos de una categoría")
    public ResponseEntity<EntityModel<Categoria>> updateCategoria(@PathVariable Long id,
            @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria actualizada = categoriaService.save(categoria);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente categoría", description = "Modifica ciertos campos de una categoría")
    public ResponseEntity<EntityModel<Categoria>> patchCategoria(@PathVariable Long id,
            @RequestBody Categoria parcial) {
        Categoria existente = categoriaService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        if (parcial.getNombre() != null) {
            existente.setNombre(parcial.getNombre());
        }
        Categoria actualizada = categoriaService.save(existente);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar categoría por nombre", description = "Busca una categoría por su nombre exacto")
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> getCategoriasByNombre(@RequestParam String nombre) {
        List<EntityModel<Categoria>> categorias = categoriaService.findByNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (categorias.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaControllerV2.class).getCategoriasByNombre(nombre)).withSelfRel()));
    }

    @GetMapping(value = "/ordenadas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar categorías ordenadas", description = "Devuelve todas las categorías ordenadas por nombre")
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> getCategoriasOrdenadas() {
        List<EntityModel<Categoria>> categorias = categoriaService.listaOrdenada().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (categorias.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaControllerV2.class).getCategoriasOrdenadas()).withSelfRel()));
    }
}
