package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadAllMongoMessagesUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public List<MessageResponse> execute() {
        return chatMessageMongoService.findAll().stream()
                .map(MessageResponse::from)
                .toList();
    }
}
