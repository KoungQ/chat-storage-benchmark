package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMongoMessageByIdUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public MessageResponse execute(Long id) {
        return MessageResponse.from(chatMessageMongoService.findById(id));
    }
}
