package com.project.benchmark.domain.message.application.usecase.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.entity.ChatMessage;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadPostgresqlMessagePageUseCase {

    private final ChatMessageService chatMessageService;

    public MessagePageResponse execute(int page, int size) {
        Page<ChatMessage> messagePage = chatMessageService.findPage(page, size);
        List<MessageResponse> messages = messagePage.getContent().stream()
                .map(MessageResponse::from)
                .toList();
        return new MessagePageResponse(messages, messagePage.getTotalElements(), page, size);
    }
}
