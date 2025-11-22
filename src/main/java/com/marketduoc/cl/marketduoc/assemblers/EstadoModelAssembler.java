package com.marketduoc.cl.marketduoc.assemblers;

import com.marketduoc.cl.marketduoc.controller.EstadoControllerV2;
import com.marketduoc.cl.marketduoc.model.Estado;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoModelAssembler implements RepresentationModelAssembler<Estado, EntityModel<Estado>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Estado> toModel(Estado estado) {
        return EntityModel.of(estado,
                linkTo(methodOn(EstadoControllerV2.class).getEstadoById(estado.getId().longValue())).withSelfRel(),
                linkTo(methodOn(EstadoControllerV2.class).getAllEstados()).withRel("estados"),
                linkTo(methodOn(EstadoControllerV2.class).updateEstado(estado.getId().longValue(), estado)).withRel("actualizar"),
                linkTo(methodOn(EstadoControllerV2.class).patchEstado(estado.getId().longValue(), estado)).withRel("actualizar-parcial"),
                linkTo(methodOn(EstadoControllerV2.class).deleteEstado(estado.getId().longValue())).withRel("eliminar"),
                linkTo(methodOn(EstadoControllerV2.class).getByNombre(estado.getNombre())).withRel("buscar-por-nombre")
        );
    }
}
