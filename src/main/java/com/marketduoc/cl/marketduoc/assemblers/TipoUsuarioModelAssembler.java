package com.marketduoc.cl.marketduoc.assemblers;

import com.marketduoc.cl.marketduoc.controller.TipoUsuarioControllerV2;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoUsuarioModelAssembler implements RepresentationModelAssembler<TipoUsuario, EntityModel<TipoUsuario>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<TipoUsuario> toModel(TipoUsuario tipoUsuario) {
        return EntityModel.of(tipoUsuario,
                linkTo(methodOn(TipoUsuarioControllerV2.class).getTipoUsuarioById(tipoUsuario.getId().longValue())).withSelfRel(),
                linkTo(methodOn(TipoUsuarioControllerV2.class).getAllTipoUsuarios()).withRel("tipousuarios"),
                linkTo(methodOn(TipoUsuarioControllerV2.class).updateTipoUsuario(tipoUsuario.getId().longValue(), tipoUsuario)).withRel("actualizar"),
                linkTo(methodOn(TipoUsuarioControllerV2.class).patchTipoUsuario(tipoUsuario.getId().longValue(), tipoUsuario)).withRel("actualizar-parcial"),
                linkTo(methodOn(TipoUsuarioControllerV2.class).deleteTipoUsuario(tipoUsuario.getId().longValue())).withRel("eliminar"),
                linkTo(methodOn(TipoUsuarioControllerV2.class).getByNombre(tipoUsuario.getNombre())).withRel("buscar-por-nombre")
        );
    }
}
