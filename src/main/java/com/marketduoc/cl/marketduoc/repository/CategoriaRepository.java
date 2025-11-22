package com.marketduoc.cl.marketduoc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.marketduoc.cl.marketduoc.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

    List<Categoria> findByNombre(String nombre);

    Categoria findById(Integer id);

    @Query("SELECT c FROM Categoria c ORDER BY c.nombre ASC")
    List<Categoria> listaOrdenada();
}
