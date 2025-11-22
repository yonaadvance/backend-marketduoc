package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.service.TipoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipousuarios")
@Tag(name = "Tipos de Usuario", description = "API para la gestión de los tipos de usuario")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @GetMapping
    @Operation(summary = "Listar tipos de usuario", description = "Obtiene todos los tipos de usuario disponibles")
    public ResponseEntity<List<TipoUsuario>> listarTipoUsuario() {
        List<TipoUsuario> tipos = tipoUsuarioService.findAll();
        if (tipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de usuario por ID", description = "Recupera un tipo de usuario dado su identificador")
    public ResponseEntity<TipoUsuario> obtenerTipoUsuarioPorId(@PathVariable Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioService.findById(id);
        if (tipoUsuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tipoUsuario);
    }

    @PostMapping
    @Operation(summary = "Crear tipo de usuario", description = "Registra un nuevo tipo de usuario en el sistema")
    public ResponseEntity<TipoUsuario> crearTipoUsuario(@RequestBody TipoUsuario tipoUsuario) {
        TipoUsuario nuevo = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de usuario", description = "Reemplaza completamente un tipo de usuario por su ID")
    public ResponseEntity<TipoUsuario> actualizarTipoUsuario(@PathVariable Long id, @RequestBody TipoUsuario tipoUsuario) {
        tipoUsuario.setId(id);
        TipoUsuario actualizado = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente tipo de usuario", description = "Modifica campos específicos de un tipo de usuario")
    public ResponseEntity<TipoUsuario> patchTipoUsuario(@PathVariable Long id, @RequestBody TipoUsuario parcial) {
        TipoUsuario actualizado = tipoUsuarioService.patchTipoUsuario(id, parcial);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de usuario", description = "Elimina un tipo de usuario dado su ID")
    public ResponseEntity<Void> eliminarTipoUsuario(@PathVariable Long id) {
        tipoUsuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
