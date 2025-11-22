package com.marketduoc.cl.marketduoc.service;

import com.marketduoc.cl.marketduoc.model.Categoria;
import com.marketduoc.cl.marketduoc.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria findById(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.orElse(null);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }

    public List<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public List<Categoria> listaOrdenada() {
        return categoriaRepository.listaOrdenada();
    }
}
