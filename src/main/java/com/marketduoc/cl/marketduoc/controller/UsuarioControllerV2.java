package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.assemblers.UsuarioModelAssembler;
import com.marketduoc.cl.marketduoc.model.Usuario;
import com.marketduoc.cl.marketduoc.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios v2", description = "Operaciones con representación HATEOAS para usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar usuarios con HATEOAS", description = "Devuelve una colección HATEOAS de todos los usuarios registrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).getAllUsuarios()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener usuario por ID con HATEOAS", description = "Recupera un usuario específico usando su ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioByCodigo(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo usuario con HATEOAS", description = "Registra un nuevo usuario y devuelve su representación HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        Usuario newUsuario = usuarioService.save(usuario);
        return ResponseEntity
                .created(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).getUsuarioByCodigo(newUsuario.getId()))
                        .toUri())
                .body(assembler.toModel(newUsuario));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un usuario con HATEOAS", description = "Reemplaza completamente los datos de un usuario dado su ID y devuelve su nueva representación HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        Usuario updatedUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(assembler.toModel(updatedUsuario));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente un usuario con HATEOAS", description = "Modifica uno o más campos de un usuario existente y devuelve su representación actualizada HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> patchUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario updatedUsuario = usuarioService.patchUsuario(id, usuario);
        if (updatedUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updatedUsuario));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un usuario con HATEOAS", description = "Borra un usuario del sistema dado su ID")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/correo/{correo}")
    @Operation(summary = "Obtener un usuario por su correo con HATEOAS", description = "Recupera un usuario específico usando su correo con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioByCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.findByCorreo(correo);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    @GetMapping("/buscar/nombre-apellidos")
    @Operation(summary = "Obtener un usuario por su nombre y apellido con HATEOAS", description = "Recupera un usuario específico usando su nombre y apellido con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getUsuariosByNombreAndApellidos(
            @RequestParam String nombre, @RequestParam String apellidos) {
        List<EntityModel<Usuario>> usuarios = usuarioService.findByNombreAndApellidos(nombre, apellidos).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (usuarios.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(usuarios));
    }

    @GetMapping("/buscar/nombre-correo")
    @Operation(summary = "Obtener un usuario por su nombre y correo con HATEOAS", description = "Recupera un usuario específico usando su nombre y correo con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioByNombreAndCorreo(
            @RequestParam String nombre, @RequestParam String correo) {
        Usuario usuario = usuarioService.findByNombreAndCorreo(nombre, correo);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(assembler.toModel(usuario));
}

    @GetMapping("/buscar/tipo-categoria")
    @Operation(summary = "Obtener un usuario por su tipoUsuario y categoria con HATEOAS", description = "Recupera un usuario específico usando su tipoUsuario y categoria con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getUsuariosPorTipoYCategoria(
            @RequestParam String tipo, @RequestParam String categoria) {
        List<EntityModel<Usuario>> usuarios = usuarioService.buscarPorTipoYCategoriaDeProducto(tipo, categoria).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        if (usuarios.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(usuarios));
    }

}
