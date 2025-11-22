package com.marketduoc.cl.marketduoc.controller;

import com.marketduoc.cl.marketduoc.assemblers.ProductoModelAssembler;
import com.marketduoc.cl.marketduoc.model.Producto;
import com.marketduoc.cl.marketduoc.service.ProductoService;

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
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos v2", description = "Operaciones con representación HATEOAS para productos")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar productos con HATEOAS", description = "Devuelve una colección HATEOAS de todos los productos")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getAllProductos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getAllProductos())
                        .withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener producto por ID con HATEOAS", description = "Recupera un producto específico usando su ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Producto>> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo producto con HATEOAS", description = "Publica un nuevo producto y devuelve su representación HATEOAS")
    public ResponseEntity<EntityModel<Producto>> createProducto(@RequestBody Producto producto) {
        Producto newProducto = productoService.save(producto);
        return ResponseEntity
                .created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class)
                        .getProductoById(newProducto.getId())).toUri())
                .body(assembler.toModel(newProducto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un producto con HATEOAS", description = "Reemplaza completamente un producto por su ID y retorna la nueva representación HATEOAS")
    public ResponseEntity<EntityModel<Producto>> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        Producto updatedProducto = productoService.save(producto);
        return ResponseEntity.ok(assembler.toModel(updatedProducto));
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente un producto con HATEOAS", description = "Modifica campos específicos de un producto existente usando su ID y retorna su versión actualizada")
    public ResponseEntity<EntityModel<Producto>> patchProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updatedProducto = productoService.patchProducto(id, producto);
        if (updatedProducto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updatedProducto));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un producto con HATEOAS", description = "Elimina un producto existente del sistema usando su ID")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre/{nombre}")
    @Operation(summary = "Buscar productos por nombre", description = "Devuelve productos que coinciden con el nombre")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductosByNombre(@PathVariable String nombre) {
        List<EntityModel<Producto>> productos = productoService.findByNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductosByNombre(nombre))
                        .withSelfRel()));
    }

    @GetMapping("/buscar/usuario/{usuarioId}")
    @Operation(summary = "Buscar productos por usuario", description = "Devuelve productos asociados a un usuario por ID")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductosByUsuarioId(
            @PathVariable Integer usuarioId) {
        List<EntityModel<Producto>> productos = productoService.findByUsuarioId(usuarioId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductosByUsuarioId(usuarioId))
                        .withSelfRel()));
    }

    @GetMapping("/contar")
    @Operation(summary = "Contar productos agrupados por estado y categoría", description = "Devuelve conteo de productos agrupados")
    public ResponseEntity<List<Object[]>> contarProductosPorEstadoYCategoria() {
        List<Object[]> resultados = productoService.contarProductosPorEstadoYCategoria();
        if (resultados.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscar/tipousuario/{tipoNombre}")
    @Operation(summary = "Buscar productos por tipo de usuario y estado disponible", description = "Devuelve productos filtrados por tipo usuario y estado disponible")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarPorTipoDeUsuarioYEstadoDisponible(
            @PathVariable String tipoNombre) {
        List<EntityModel<Producto>> productos = productoService.buscarPorTipoDeUsuarioYEstadoDisponible(tipoNombre)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class)
                        .buscarPorTipoDeUsuarioYEstadoDisponible(tipoNombre)).withSelfRel()));
    }

    @GetMapping("/buscar/filtro")
    @Operation(summary = "Buscar productos por categoría, estado y usuario", description = "Devuelve productos filtrados por categoría, estado y nombre de usuario")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarPorCategoriaEstadoYUsuario(
            @RequestParam String categoria,
            @RequestParam String estado,
            @RequestParam String usuario) {
        List<EntityModel<Producto>> productos = productoService
                .buscarPorCategoriaEstadoYUsuario(categoria, estado, usuario).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                productos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class)
                        .buscarPorCategoriaEstadoYUsuario(categoria, estado, usuario)).withSelfRel()));
    }
}
