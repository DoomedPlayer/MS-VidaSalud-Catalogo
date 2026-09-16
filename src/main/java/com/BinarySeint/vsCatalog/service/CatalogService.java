package com.BinarySeint.vsCatalog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BinarySeint.vsCatalog.classes.Box;
import com.BinarySeint.vsCatalog.classes.Cupo;
import com.BinarySeint.vsCatalog.classes.Prestacion;
import com.BinarySeint.vsCatalog.repository.BoxRepository;
import com.BinarySeint.vsCatalog.repository.CupoRepository;
import com.BinarySeint.vsCatalog.repository.PrestacionRepository;

import java.util.List;

@Service
public class CatalogService {

    private final PrestacionRepository prestacionRepository;
    private final CupoRepository cupoRepository;
    private final BoxRepository boxRepository;

    public CatalogService(PrestacionRepository prestacionRepository, 
                          CupoRepository cupoRepository, 
                          BoxRepository boxRepository) {
        this.prestacionRepository = prestacionRepository;
        this.cupoRepository = cupoRepository;
        this.boxRepository = boxRepository;
    }

    public List<Prestacion> obtenerTodasLasPrestaciones() {
        return prestacionRepository.findAll();
    }

    @Transactional
    public Prestacion crearPrestacion(Prestacion prestacion) {
        return prestacionRepository.save(prestacion);
    }

    @Transactional
    public Prestacion actualizarPrecioPrestacion(Long id, Double nuevoPrecio) {
        Prestacion prestacion = prestacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestación no encontrada"));
        prestacion.setPrecio(nuevoPrecio);
        return prestacionRepository.save(prestacion);
    }

    public List<Cupo> obtenerCupos(Boolean disponible) {
        if (disponible != null) {
            return cupoRepository.findByDisponible(disponible);
        }
        return cupoRepository.findAll();
    }

    @Transactional
    public Cupo crearCupo(Cupo cupo) {
        cupo.setDisponible(true);
        return cupoRepository.save(cupo);
    }

    @Transactional
    public Cupo consumirCupo(Long cupoId) {
        Cupo cupo = cupoRepository.findById(cupoId)
                .orElseThrow(() -> new RuntimeException("Cupo no encontrado"));
        
        if (!cupo.getDisponible()) {
            throw new RuntimeException("El cupo ya no está disponible");
        }
        
        cupo.setDisponible(false); 
        return cupoRepository.save(cupo);
    }

    public List<Box> obtenerTodosLosBoxes() {
        return boxRepository.findAll();
    }

    @Transactional
    public Box crearBox(Box box) {
        return boxRepository.save(box);
    }
}