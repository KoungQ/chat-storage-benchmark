package com.project.benchmark.domain.message.application.usecase.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchMysqlMessagesByConditionUseCase {

    private final ChatMessageService chatMessageService;

    public List<MessageResponse> execute(Long senderId) {
        return chatMessageService.findBySenderId(senderId).stream()
                .map(MessageResponse::from)
                .toList();
    }
}
