package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadAllPostgresqlMessagesUseCase {

    private final ChatMessageService chatMessageService;

    public List<MessageResponse> execute() {
        return chatMessageService.findAll().stream()
                .map(MessageResponse::from)
                .toList();
    }
}
