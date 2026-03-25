package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMongoMessageUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public MessageResponse execute(Long id, UpdateMessageRequest request) {
        return MessageResponse.from(chatMessageMongoService.update(id, request.content(), request.isRead()));
    }
}
