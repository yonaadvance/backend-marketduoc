package com.marketduoc.cl.marketduoc.model;

import lombok.Data;

@Data
public class ProductoDTO {
    private String nombre;
    private String contenido;
    private Integer precio;
    private String emailUsuario;
    private String imagen;
    private Long categoriaId; // Nuevo campo
}