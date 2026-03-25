package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.domain.message.infra.document.ChatMessageDocument;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMongoMessageUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public Long execute(CreateMessageRequest request) {
        Long nextId = chatMessageMongoService.getNextId();
        ChatMessageDocument document = ChatMessageDocument.builder()
                .id(nextId)
                .roomId(request.roomId())
                .senderId(request.senderId())
                .content(request.content())
                .build();
        return chatMessageMongoService.save(document).getId();
    }
}
