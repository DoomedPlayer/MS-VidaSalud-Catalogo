package com.BinarySeint.vsCatalog.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.BinarySeint.vsCatalog.dto.AuditTimelineEventDTO;

@Service
public class CatalogKafkaProducerService {

    private static final String AUDIT_TOPIC = "audit.timeline";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CatalogKafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarAuditoria(AuditTimelineEventDTO evento) {
        kafkaTemplate.send(AUDIT_TOPIC, evento.entidadId(), evento);
    }
}