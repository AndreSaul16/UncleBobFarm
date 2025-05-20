package com.rancho.gestorGranja.service.impl;

import com.rancho.gestorGranja.entity.Inventario;
import com.rancho.gestorGranja.repository.InventarioRepository;
import com.rancho.gestorGranja.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioServiceImpl implements BaseService<Inventario, Long> {
    
    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    @Override
    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    @Override
    public Inventario save(Inventario inventario) {
        if (inventarioRepository.existsByNombre(inventario.getNombre())) {
            throw new RuntimeException("Ya existe un item con ese nombre en el inventario");
        }
        return inventarioRepository.save(inventario);
    }

    @Override
    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    public List<Inventario> findByTipo(Inventario.TipoItem tipo) {
        return inventarioRepository.findByTipo(tipo);
    }

    public List<Inventario> findNecesitanReposicion() {
        return inventarioRepository.findNecesitanReposicion();
    }

    @Transactional
    public Inventario actualizarCantidad(Long id, int cantidad) {
        Optional<Inventario> inventarioOpt = findById(id);
        if (inventarioOpt.isEmpty()) {
            throw new RuntimeException("Item no encontrado en el inventario");
        }

        Inventario inventario = inventarioOpt.get();
        int nuevaCantidad = inventario.getCantidad() + cantidad;
        if (nuevaCantidad < 0) {
            throw new RuntimeException("No hay suficiente cantidad en el inventario");
        }

        inventario.setCantidad(nuevaCantidad);
        return inventarioRepository.save(inventario);
    }
} 