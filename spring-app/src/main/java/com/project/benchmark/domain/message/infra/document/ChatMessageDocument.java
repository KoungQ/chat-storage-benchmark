package com.project.benchmark.domain.message.infra.document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDocument {

    @Id
    private Long id;

    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;

    @Builder
    public ChatMessageDocument(Long id, Long roomId, Long senderId, String content,
                                LocalDateTime createdAt, Boolean isRead) {
        this.id = id;
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
