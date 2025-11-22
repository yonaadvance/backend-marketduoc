package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.model.Estado;
import com.marketduoc.cl.marketduoc.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estados")
@Tag(name = "Estados", description = "API para la gestión de estados de productos")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @GetMapping
    @Operation(summary = "Listar estados", description = "Devuelve una lista con todos los estados disponibles")
    public ResponseEntity<List<Estado>> listarEstados() {
        List<Estado> estados = estadoService.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID", description = "Recupera un estado según su ID")
    public ResponseEntity<Estado> obtenerEstadoPorId(@PathVariable Long id) {
        Estado estado = estadoService.findById(id);
        if (estado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estado);
    }

    @PostMapping
    @Operation(summary = "Crear estado", description = "Registra un nuevo estado en el sistema")
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado) {
        Estado nuevoEstado = estadoService.save(estado);
        return ResponseEntity.status(201).body(nuevoEstado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado", description = "Reemplaza completamente los datos de un estado")
    public ResponseEntity<Estado> actualizarEstado(@PathVariable Long id, @RequestBody Estado estado) {
        estado.setId(id);
        Estado actualizado = estadoService.save(estado);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un estado", description = "Modifica uno o más campos del estado existente")
    public ResponseEntity<Estado> actualizarParcialEstado(@PathVariable Long id, @RequestBody Estado parcial) {
        Estado actual = estadoService.findById(id);
        if (actual == null) {
            return ResponseEntity.notFound().build();
        }
        if (parcial.getNombre() != null) {
            actual.setNombre(parcial.getNombre());
        }
        Estado actualizado = estadoService.save(actual);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado", description = "Elimina un estado del sistema")
    public ResponseEntity<Void> eliminarEstado(@PathVariable Long id) {
        Estado estado = estadoService.findById(id);
        if (estado == null) {
            return ResponseEntity.notFound().build();
        }
        estadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
