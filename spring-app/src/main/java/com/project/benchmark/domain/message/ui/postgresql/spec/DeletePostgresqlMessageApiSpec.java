package com.project.benchmark.domain.message.ui.postgresql.spec;

import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "PostgreSQL Benchmark")
@RequestMapping("/api/postgresql/messages")
public interface DeletePostgresqlMessageApiSpec {

    @Operation(summary = "PostgreSQL 메시지 삭제")
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id);
}
