package com.marketduoc.cl.marketduoc.model;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id; // <--- ESTE ES EL CAMBIO CLAVE (El carnet de identidad del producto)
    private String nombre;
    private String contenido;
    private Integer precio;
    private String emailUsuario;
    private String imagen;
    private Long categoriaId;
}