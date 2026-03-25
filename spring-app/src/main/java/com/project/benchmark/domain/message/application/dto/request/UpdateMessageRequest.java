package com.project.benchmark.domain.message.application.dto.request;

public record UpdateMessageRequest(
        String content,
        Boolean isRead
) {}
