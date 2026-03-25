package com.project.benchmark.domain.message.application.usecase.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.infra.document.ChatMessageDocument;
import com.project.benchmark.domain.message.infra.service.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMongoMessagePageUseCase {

    private final ChatMessageMongoService chatMessageMongoService;

    public MessagePageResponse execute(int page, int size) {
        Page<ChatMessageDocument> docPage = chatMessageMongoService.findPage(page, size);
        List<MessageResponse> messages = docPage.getContent().stream()
                .map(MessageResponse::from)
                .toList();
        return new MessagePageResponse(messages, docPage.getTotalElements(), page, size);
    }
}
