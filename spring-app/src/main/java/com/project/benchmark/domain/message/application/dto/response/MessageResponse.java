package com.project.benchmark.domain.message.application.dto.response;

import com.project.benchmark.domain.message.domain.entity.ChatMessage;
import com.project.benchmark.domain.message.infra.document.ChatMessageDocument;
import com.project.benchmark.domain.message.infra.jsonb.JsonbChatMessage;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long roomId,
        Long senderId,
        String content,
        LocalDateTime createdAt,
        Boolean isRead
) {
    public static MessageResponse from(ChatMessage entity) {
        return new MessageResponse(
                entity.getId(),
                entity.getRoomId(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getIsRead()
        );
    }

    public static MessageResponse from(ChatMessageDocument document) {
        return new MessageResponse(
                document.getId(),
                document.getRoomId(),
                document.getSenderId(),
                document.getContent(),
                document.getCreatedAt(),
                document.getIsRead()
        );
    }

    public static MessageResponse from(JsonbChatMessage message) {
        return new MessageResponse(
                message.id(),
                message.roomId(),
                message.senderId(),
                message.content(),
                message.createdAt(),
                message.isRead()
        );
    }
}
