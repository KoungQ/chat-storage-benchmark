package com.project.benchmark.domain.message.application.usecase.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.domain.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadMysqlMessageByIdUseCase {

    private final ChatMessageService chatMessageService;

    public MessageResponse execute(Long id) {
        return MessageResponse.from(chatMessageService.findById(id));
    }
}
