package com.marketduoc.cl.marketduoc.model;

import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String contenido;

    @Column(nullable = true) // Nuevo campo para el precio real
    private Integer precio;

    @Column(columnDefinition = "TEXT") 
    private String imagen;

    @Column(nullable = false)
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_usu", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_est", nullable = false)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "id_cat", nullable = false)
    private Categoria categoria;
}