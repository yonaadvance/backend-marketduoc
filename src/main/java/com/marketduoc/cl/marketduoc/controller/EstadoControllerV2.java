package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.assemblers.EstadoModelAssembler;
import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.service.EstadoService;
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
@RequestMapping("/api/v2/estados")
@Tag(name = "Estados v2", description = "Operaciones con representación HATEOAS para estados de productos")
public class EstadoControllerV2 {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private EstadoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar estados con HATEOAS", description = "Devuelve una colección HATEOAS de todos los estados")
    public ResponseEntity<CollectionModel<EntityModel<Estado>>> getAllEstados() {
        List<EntityModel<Estado>> estados = estadoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                estados,
                linkTo(methodOn(EstadoControllerV2.class).getAllEstados()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener estado por ID con HATEOAS", description = "Recupera un estado específico usando su ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Estado>> getEstadoById(@PathVariable Long id) {
        Estado estado = estadoService.findById(id);
        if (estado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(estado));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo estado con HATEOAS", description = "Crea un nuevo estado y devuelve su representación HATEOAS")
    public ResponseEntity<EntityModel<Estado>> createEstado(@RequestBody Estado estado) {
        Estado nuevoEstado = estadoService.save(estado);
        return ResponseEntity
                .created(linkTo(methodOn(EstadoControllerV2.class).getEstadoById(nuevoEstado.getId().longValue()))
                        .toUri())
                .body(assembler.toModel(nuevoEstado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar estado con HATEOAS", description = "Reemplaza completamente los datos de un estado y retorna su versión actualizada")
    public ResponseEntity<EntityModel<Estado>> updateEstado(@PathVariable Long id, @RequestBody Estado estado) {
        estado.setId(id);
        Estado actualizado = estadoService.save(estado);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente estado con HATEOAS", description = "Actualiza uno o más campos de un estado existente con HATEOAS")
    public ResponseEntity<EntityModel<Estado>> patchEstado(@PathVariable Long id, @RequestBody Estado parcial) {
        Estado actual = estadoService.findById(id);
        if (actual == null) {
            return ResponseEntity.notFound().build();
        }
        if (parcial.getNombre() != null) {
            actual.setNombre(parcial.getNombre());
        }
        Estado actualizado = estadoService.save(actual);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar estado con HATEOAS", description = "Elimina un estado del sistema con representación HATEOAS")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        Estado estado = estadoService.findById(id);
        if (estado == null) {
            return ResponseEntity.notFound().build();
        }
        estadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar estados por nombre")
    public ResponseEntity<CollectionModel<EntityModel<Estado>>> getByNombre(@RequestParam String nombre) {
        List<Estado> resultados = estadoService.findByNombre(nombre);
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Estado>> modelos = resultados.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(modelos));
    }

    @GetMapping(value = "/ordenados", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar estados ordenados alfabéticamente")
    public ResponseEntity<CollectionModel<EntityModel<Estado>>> getEstadosOrdenados() {
        List<EntityModel<Estado>> estados = estadoService.listaOrdenada().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(estados));
    }
}
