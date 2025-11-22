package com.marketduoc.cl.marketduoc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.marketduoc.cl.marketduoc.model.TipoUsuario;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long>{

    List<TipoUsuario> findByNombre(String nombre);

    TipoUsuario findById(Integer id);
}
