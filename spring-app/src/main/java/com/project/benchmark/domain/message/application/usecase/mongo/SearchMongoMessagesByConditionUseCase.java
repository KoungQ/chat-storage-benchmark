package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchMongoMessagesByConditionUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public List<MessageResponse> execute(Long senderId) {
        return chatMessageMongoService.findBySenderId(senderId).stream()
                .map(MessageResponse::from)
                .toList();
    }
}
