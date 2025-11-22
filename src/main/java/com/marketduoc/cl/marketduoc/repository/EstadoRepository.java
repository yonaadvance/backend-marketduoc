package com.marketduoc.cl.marketduoc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.marketduoc.cl.marketduoc.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{

    List<Estado> findByNombre(String nombre);

    Estado findById(Integer id);

    @Query("SELECT e FROM Estado e ORDER BY e.nombre ASC")
    List<Estado> listaOrdenada();
}
