package com.project.benchmark.domain.message.ui.postgresql.spec;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "PostgreSQL Benchmark")
@RequestMapping("/api/postgresql/messages")
public interface LoadPostgresqlMessageByIdApiSpec {

    @Operation(summary = "PostgreSQL PK로 메시지 조회")
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<MessageResponse>> loadById(@PathVariable Long id);
}
