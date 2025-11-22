package com.marketduoc.cl.marketduoc.assemblers;

import com.marketduoc.cl.marketduoc.controller.CategoriaControllerV2;
import com.marketduoc.cl.marketduoc.model.Categoria;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerV2.class).getCategoriaById(categoria.getId().longValue())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).getAllCategorias()).withRel("categorias"),
                linkTo(methodOn(CategoriaControllerV2.class).updateCategoria(categoria.getId().longValue(), categoria)).withRel("actualizar"),
                linkTo(methodOn(CategoriaControllerV2.class).patchCategoria(categoria.getId().longValue(), categoria)).withRel("actualizar-parcial"),
                linkTo(methodOn(CategoriaControllerV2.class).deleteCategoria(categoria.getId().longValue())).withRel("eliminar"),
                linkTo(methodOn(CategoriaControllerV2.class).getCategoriasByNombre(categoria.getNombre())).withRel("buscar-por-nombre")
        );
    }
}
