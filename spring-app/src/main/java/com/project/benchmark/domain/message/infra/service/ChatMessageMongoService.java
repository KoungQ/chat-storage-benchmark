package com.project.benchmark.domain.message.infra.service;

import com.project.benchmark.domain.message.infra.document.ChatMessageDocument;
import com.project.benchmark.domain.message.infra.repository.ChatMessageMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageMongoService {

    private final ChatMessageMongoRepository chatMessageMongoRepository;

    public List<ChatMessageDocument> findAll() {
        return chatMessageMongoRepository.findAll();
    }

    public ChatMessageDocument findById(Long id) {
        return chatMessageMongoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChatMessageDocument not found: " + id));
    }

    public List<ChatMessageDocument> findBySenderId(Long senderId) {
        return chatMessageMongoRepository.findBySenderId(senderId);
    }

    public Page<ChatMessageDocument> findPage(int page, int size) {
        return chatMessageMongoRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public ChatMessageDocument save(ChatMessageDocument document) {
        return chatMessageMongoRepository.save(document);
    }

    public Long getNextId() {
        List<ChatMessageDocument> docs = chatMessageMongoRepository
                .findTopByOrderByIdDesc(PageRequest.of(0, 1));
        if (docs.isEmpty()) return 1L;
        return docs.get(0).getId() + 1;
    }

    public ChatMessageDocument update(Long id, String content, Boolean isRead) {
        ChatMessageDocument document = findById(id);
        document.update(content, isRead);
        return chatMessageMongoRepository.save(document);
    }

    public void delete(Long id) {
        chatMessageMongoRepository.deleteById(id);
    }
}
