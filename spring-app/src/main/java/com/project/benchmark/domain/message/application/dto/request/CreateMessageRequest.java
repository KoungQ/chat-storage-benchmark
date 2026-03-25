package com.project.benchmark.domain.message.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMessageRequest(
        @NotNull Long roomId,
        @NotNull Long senderId,
        @NotBlank String content
) {}
