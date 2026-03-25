package com.project.benchmark.domain.message.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isRead;

    @Builder
    public ChatMessage(Long roomId, Long senderId, String content, LocalDateTime createdAt, Boolean isRead) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.isRead = isRead != null ? isRead : false;
    }

    public void update(String content, Boolean isRead) {
        if (content != null) this.content = content;
        if (isRead != null) this.isRead = isRead;
    }
}
