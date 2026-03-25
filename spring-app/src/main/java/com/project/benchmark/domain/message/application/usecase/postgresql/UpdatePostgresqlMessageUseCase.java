package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePostgresqlMessageUseCase {

    private final ChatMessageService chatMessageService;

    public MessageResponse execute(Long id, UpdateMessageRequest request) {
        return MessageResponse.from(chatMessageService.update(id, request.content(), request.isRead()));
    }
}
