package com.marketduoc.cl.marketduoc.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    // --- NUEVO CAMPO PARA FOTO ---
    // Usamos TEXT porque el código Base64 es muy largo
    @Column(columnDefinition = "TEXT") 
    private String imagen;
    // -----------------------------

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