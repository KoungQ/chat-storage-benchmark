package com.project.benchmark.domain.message.ui.mysql.spec;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "MySQL Benchmark")
@RequestMapping("/api/mysql/messages")
public interface UpdateMysqlMessageApiSpec {

    @Operation(summary = "MySQL 메시지 수정")
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse<MessageResponse>> update(@PathVariable Long id, @RequestBody UpdateMessageRequest request);
}
