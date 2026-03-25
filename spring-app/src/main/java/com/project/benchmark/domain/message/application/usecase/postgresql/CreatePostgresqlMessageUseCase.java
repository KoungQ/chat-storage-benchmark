package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.domain.message.domain.entity.ChatMessage;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePostgresqlMessageUseCase {

    private final ChatMessageService chatMessageService;

    public Long execute(CreateMessageRequest request) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(request.roomId())
                .senderId(request.senderId())
                .content(request.content())
                .build();
        return chatMessageService.save(chatMessage).getId();
    }
}
