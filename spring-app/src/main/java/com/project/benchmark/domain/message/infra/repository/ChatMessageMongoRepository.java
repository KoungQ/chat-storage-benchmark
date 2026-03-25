package com.project.benchmark.domain.message.infra.repository;

import com.project.benchmark.domain.message.infra.document.ChatMessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, Long> {

    List<ChatMessageDocument> findBySenderId(Long senderId);

    Page<ChatMessageDocument> findAll(Pageable pageable);

    @Query(value = "{}", sort = "{'_id': -1}")
    List<ChatMessageDocument> findTopByOrderByIdDesc(Pageable pageable);
}
