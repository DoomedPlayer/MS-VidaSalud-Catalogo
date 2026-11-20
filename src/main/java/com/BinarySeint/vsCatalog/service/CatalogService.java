package com.BinarySeint.vsCatalog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BinarySeint.vsCatalog.classes.Box;
import com.BinarySeint.vsCatalog.classes.Cupo;
import com.BinarySeint.vsCatalog.classes.Prestacion;
import com.BinarySeint.vsCatalog.dto.AuditTimelineEventDTO;
import com.BinarySeint.vsCatalog.repository.BoxRepository;
import com.BinarySeint.vsCatalog.repository.CupoRepository;
import com.BinarySeint.vsCatalog.repository.PrestacionRepository;

@Service
public class CatalogService {

    private final PrestacionRepository prestacionRepository;
    private final CupoRepository cupoRepository;
    private final BoxRepository boxRepository;
    private final CatalogKafkaProducerService kafkaProducer;

    public CatalogService(PrestacionRepository prestacionRepository, 
                          CupoRepository cupoRepository, 
                          BoxRepository boxRepository,
                          CatalogKafkaProducerService kafkaProducer) {
        this.prestacionRepository = prestacionRepository;
        this.cupoRepository = cupoRepository;
        this.boxRepository = boxRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<Prestacion> obtenerTodasLasPrestaciones() {
        return prestacionRepository.findAll();
    }

    @Transactional
    public Prestacion crearPrestacion(Prestacion prestacion) {
        Prestacion guardada = prestacionRepository.save(prestacion);

        kafkaProducer.publicarAuditoria(new AuditTimelineEventDTO(
                UUID.randomUUID().toString(),
                "PRESTACION-" + guardada.getId(),
                "Administrador",
                "CREACION_PRESTACION",
                LocalDateTime.now(),
                "127.0.0.1",
                "ms-vidasalud-catalog",
                "Nueva prestación creada con ID " + guardada.getId() + " | Precio: $" + guardada.getPrecio()
        ));

        return guardada;
    }

    @Transactional
    public Prestacion actualizarPrecioPrestacion(Long id, Double nuevoPrecio) {
        Prestacion prestacion = prestacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestación no encontrada"));
        Double precioAnterior = prestacion.getPrecio();
        prestacion.setPrecio(nuevoPrecio);
        Prestacion actualizada = prestacionRepository.save(prestacion);

        kafkaProducer.publicarAuditoria(new AuditTimelineEventDTO(
                UUID.randomUUID().toString(),
                "PRESTACION-" + actualizada.getId(),
                "Administrador",
                "ACTUALIZACION_PRECIO_PRESTACION",
                LocalDateTime.now(),
                "127.0.0.1",
                "ms-vidasalud-catalog",
                "Prestación ID " + actualizada.getId() + " cambió precio de $" + precioAnterior + " a $" + nuevoPrecio
        ));

        return actualizada;
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