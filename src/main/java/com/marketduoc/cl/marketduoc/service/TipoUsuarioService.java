package com.marketduoc.cl.marketduoc.service;

import com.marketduoc.cl.marketduoc.model.TipoUsuario;
import com.marketduoc.cl.marketduoc.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipousuarioRepository;

    public List<TipoUsuario> findAll() {
        return tipousuarioRepository.findAll();
    }

    public TipoUsuario findById(Long id) {
        return tipousuarioRepository.findById(id).orElse(null);
    }

    public TipoUsuario save(TipoUsuario tipousuario) {
        return tipousuarioRepository.save(tipousuario);
    }

    public void delete(Long id) {
        tipousuarioRepository.deleteById(id);
    }

    public TipoUsuario patchTipoUsuario(Long id, TipoUsuario parcial) {
        Optional<TipoUsuario> tipoOptional = tipousuarioRepository.findById(id);
        if (tipoOptional.isPresent()) {
            TipoUsuario tipoToUpdate = tipoOptional.get();
            if (parcial.getNombre() != null) {
                tipoToUpdate.setNombre(parcial.getNombre());
            }
            return tipousuarioRepository.save(tipoToUpdate);
        } else {
            return null;
        }
    }

    public List<TipoUsuario> findByNombre(String nombre) {
        return tipousuarioRepository.findByNombre(nombre);
    }
}
