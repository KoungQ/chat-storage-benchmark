package com.project.benchmark.domain.message.ui.mongo.spec;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "MongoDB Benchmark")
@RequestMapping("/api/mongo/messages")
public interface SearchMongoMessagesApiSpec {

    @Operation(summary = "MongoDB 조건 조회 (senderId, 인덱스 없음)")
    @GetMapping("/search")
    ResponseEntity<BaseResponse<List<MessageResponse>>> search(@RequestParam Long senderId);
}
