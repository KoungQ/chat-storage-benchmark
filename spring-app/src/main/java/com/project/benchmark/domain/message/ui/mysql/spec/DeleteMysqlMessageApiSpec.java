package com.project.benchmark.domain.message.ui.mysql.spec;

import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "MySQL Benchmark")
@RequestMapping("/api/mysql/messages")
public interface DeleteMysqlMessageApiSpec {

    @Operation(summary = "MySQL 메시지 삭제")
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id);
}
