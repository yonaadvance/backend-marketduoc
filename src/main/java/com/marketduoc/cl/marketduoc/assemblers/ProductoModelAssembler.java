package com.marketduoc.cl.marketduoc.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.marketduoc.cl.marketduoc.controller.ProductoControllerV2;
import com.marketduoc.cl.marketduoc.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class).getProductoById(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).getAllProductos()).withRel("productos"),
                linkTo(methodOn(ProductoControllerV2.class).updateProducto(producto.getId(), producto)).withRel("actualizar"),
                linkTo(methodOn(ProductoControllerV2.class).patchProducto(producto.getId(), producto)).withRel("actualizar-parcial"),
                linkTo(methodOn(ProductoControllerV2.class).deleteProducto(producto.getId())).withRel("eliminar"),
                linkTo(methodOn(ProductoControllerV2.class).getProductosByNombre(producto.getNombre())).withRel("buscar-por-nombre"),
                linkTo(methodOn(ProductoControllerV2.class).getProductosByUsuarioId(producto.getUsuario() != null ? producto.getUsuario().getId().intValue() : null)).withRel("buscar-por-usuario")
        );
    }
}
