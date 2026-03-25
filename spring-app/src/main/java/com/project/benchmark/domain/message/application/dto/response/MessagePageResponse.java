package com.project.benchmark.domain.message.application.dto.response;

import java.util.List;

public record MessagePageResponse(
        List<MessageResponse> messages,
        long total,
        int page,
        int size
) {}
