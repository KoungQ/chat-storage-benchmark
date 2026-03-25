package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadPostgresqlMessageByIdUseCase {

    private final ChatMessageService chatMessageService;

    public MessageResponse execute(Long id) {
        return MessageResponse.from(chatMessageService.findById(id));
    }
}
