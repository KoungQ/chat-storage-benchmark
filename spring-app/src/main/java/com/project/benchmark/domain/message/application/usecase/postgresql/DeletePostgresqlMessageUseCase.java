package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePostgresqlMessageUseCase {

    private final ChatMessageService chatMessageService;

    public void execute(Long id) {
        chatMessageService.delete(id);
    }
}
