package com.BinarySeint.vsCatalog.dto;

import java.time.LocalDateTime;

public record AuditTimelineEventDTO(
        String eventId,
        String entidadId,
        String usuarioId,
        String accion,
        LocalDateTime fechaHora,
        String origenIp,
        String servicioOrigen,
        String detalles
) {}