package com.BinarySeint.vsCatalog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BinarySeint.vsCatalog.classes.Box;
import com.BinarySeint.vsCatalog.classes.Cupo;
import com.BinarySeint.vsCatalog.classes.Prestacion;
import com.BinarySeint.vsCatalog.service.CatalogService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/services")
    public ResponseEntity<List<Prestacion>> getServices() {
        return ResponseEntity.ok(catalogService.obtenerTodasLasPrestaciones());
    }

    @PostMapping("/services")
    public ResponseEntity<Prestacion> createService(@RequestBody Prestacion prestacion) {
        return ResponseEntity.ok(catalogService.crearPrestacion(prestacion));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<Prestacion> updateServicePrice(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        Double nuevoPrecio = body.get("precio");
        return ResponseEntity.ok(catalogService.actualizarPrecioPrestacion(id, nuevoPrecio));
    }

    @GetMapping("/cupos")
    public ResponseEntity<List<Cupo>> getCupos(@RequestParam(required = false) Boolean disponible) {
        return ResponseEntity.ok(catalogService.obtenerCupos(disponible));
    }

    @PostMapping("/cupos")
    public ResponseEntity<Cupo> createCupo(@RequestBody Cupo cupo) {
        return ResponseEntity.ok(catalogService.crearCupo(cupo));
    }

    @PutMapping("/cupos/{cupoId}/consumir")
    public ResponseEntity<Cupo> consumeCupo(@PathVariable Long cupoId) {
        return ResponseEntity.ok(catalogService.consumirCupo(cupoId));
    }

    @GetMapping("/boxes")
    public ResponseEntity<List<Box>> getBoxes() {
        return ResponseEntity.ok(catalogService.obtenerTodosLosBoxes());
    }

    @PostMapping("/boxes")
    public ResponseEntity<Box> createBox(@RequestBody Box box) {
        return ResponseEntity.ok(catalogService.crearBox(box));
    }
}