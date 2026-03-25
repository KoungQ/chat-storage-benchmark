package com.project.benchmark.domain.message.application.usecase.mysql;

import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteMysqlMessageUseCase {

    private final ChatMessageService chatMessageService;

    public void execute(Long id) {
        chatMessageService.delete(id);
    }
}
