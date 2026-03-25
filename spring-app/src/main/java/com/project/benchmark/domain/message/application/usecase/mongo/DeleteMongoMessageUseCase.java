package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMongoMessageUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public void execute(Long id) {
        chatMessageMongoService.delete(id);
    }
}
