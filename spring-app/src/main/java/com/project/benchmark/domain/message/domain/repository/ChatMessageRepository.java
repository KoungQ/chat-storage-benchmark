package com.project.benchmark.domain.message.domain.repository;

import com.project.benchmark.domain.message.domain.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderId(Long senderId);

    Page<ChatMessage> findAll(Pageable pageable);
}
