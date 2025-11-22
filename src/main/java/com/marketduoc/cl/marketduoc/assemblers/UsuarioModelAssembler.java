package com.marketduoc.cl.marketduoc.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.marketduoc.cl.marketduoc.controller.UsuarioControllerV2;
import com.marketduoc.cl.marketduoc.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class).getUsuarioByCodigo(usuario.getId().longValue())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioControllerV2.class).updateUsuario(usuario.getId().longValue(), usuario)).withRel("actualizar"),
                linkTo(methodOn(UsuarioControllerV2.class).patchUsuario(usuario.getId(), usuario)).withRel("actualizar-parcial"),
                linkTo(methodOn(UsuarioControllerV2.class).deleteUsuario(usuario.getId())).withRel("eliminar"),
                linkTo(methodOn(UsuarioControllerV2.class).getUsuarioByCorreo(usuario.getCorreo())).withRel("buscar-por-correo"),
                linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByNombreAndApellidos(usuario.getNombre(), usuario.getApellidos())).withRel("buscar-por-nombre-y-apellidos"),
                linkTo(methodOn(UsuarioControllerV2.class).getUsuarioByNombreAndCorreo(usuario.getNombre(), usuario.getCorreo())).withRel("buscar-por-nombre-y-correo"),
                linkTo(methodOn(UsuarioControllerV2.class).getUsuariosPorTipoYCategoria(usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNombre() : "tipo","categoria")).withRel("buscar-por-tipo-y-categoria")
        );
    }
}
