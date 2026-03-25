package com.project.benchmark.domain.message.domain.service;

import com.project.benchmark.domain.message.domain.entity.ChatMessage;
import com.project.benchmark.domain.message.domain.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> findAll() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChatMessage not found: " + id));
    }

    public List<ChatMessage> findBySenderId(Long senderId) {
        return chatMessageRepository.findBySenderId(senderId);
    }

    public Page<ChatMessage> findPage(int page, int size) {
        return chatMessageRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage update(Long id, String content, Boolean isRead) {
        ChatMessage chatMessage = findById(id);
        chatMessage.update(content, isRead);
        return chatMessageRepository.save(chatMessage);
    }

    public void delete(Long id) {
        chatMessageRepository.deleteById(id);
    }
}
