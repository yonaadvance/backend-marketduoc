package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.assemblers.TipoUsuarioModelAssembler;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.service.TipoUsuarioService;
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
@RequestMapping("/api/v2/tipousuarios")
@Tag(name = "Tipos de Usuario v2", description = "Operaciones con representación HATEOAS para los tipos de usuario")
public class TipoUsuarioControllerV2 {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private TipoUsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar tipos de usuario con HATEOAS", description = "Devuelve una colección HATEOAS de todos los tipos de usuario")
    public ResponseEntity<CollectionModel<EntityModel<TipoUsuario>>> getAllTipoUsuarios() {
        List<EntityModel<TipoUsuario>> tipos = tipoUsuarioService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (tipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                tipos,
                linkTo(methodOn(TipoUsuarioControllerV2.class).getAllTipoUsuarios()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener tipo de usuario por ID con HATEOAS", description = "Recupera un tipo de usuario específico por su ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<TipoUsuario>> getTipoUsuarioById(@PathVariable Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioService.findById(id);
        if (tipoUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(tipoUsuario));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear tipo de usuario con HATEOAS", description = "Crea un nuevo tipo de usuario y devuelve su representación HATEOAS")
    public ResponseEntity<EntityModel<TipoUsuario>> createTipoUsuario(@RequestBody TipoUsuario tipoUsuario) {
        TipoUsuario nuevo = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity
                .created(linkTo(methodOn(TipoUsuarioControllerV2.class).getTipoUsuarioById(nuevo.getId().longValue()))
                        .toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar tipo de usuario con HATEOAS", description = "Actualiza completamente los datos de un tipo de usuario")
    public ResponseEntity<EntityModel<TipoUsuario>> updateTipoUsuario(@PathVariable Long id,
            @RequestBody TipoUsuario tipoUsuario) {
        tipoUsuario.setId(id);
        TipoUsuario actualizado = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente tipo de usuario con HATEOAS", description = "Modifica campos específicos del tipo de usuario")
    public ResponseEntity<EntityModel<TipoUsuario>> patchTipoUsuario(@PathVariable Long id,
            @RequestBody TipoUsuario parcial) {
        TipoUsuario actual = tipoUsuarioService.findById(id);
        if (actual == null) {
            return ResponseEntity.notFound().build();
        }
        if (parcial.getNombre() != null) {
            actual.setNombre(parcial.getNombre());
        }
        TipoUsuario actualizado = tipoUsuarioService.save(actual);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar tipo de usuario con HATEOAS", description = "Elimina un tipo de usuario del sistema")
    public ResponseEntity<Void> deleteTipoUsuario(@PathVariable Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioService.findById(id);
        if (tipoUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        tipoUsuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar tipos de usuario por nombre", description = "Busca y devuelve tipos de usuario que coincidan con el nombre dado")
    public ResponseEntity<CollectionModel<EntityModel<TipoUsuario>>> getByNombre(@RequestParam String nombre) {
        List<TipoUsuario> tipos = tipoUsuarioService.findByNombre(nombre);
        if (tipos.isEmpty())
            return ResponseEntity.noContent().build();

        List<EntityModel<TipoUsuario>> modelos = tipos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(modelos,
                        linkTo(methodOn(TipoUsuarioControllerV2.class).getByNombre(nombre)).withSelfRel()));
    }
}
