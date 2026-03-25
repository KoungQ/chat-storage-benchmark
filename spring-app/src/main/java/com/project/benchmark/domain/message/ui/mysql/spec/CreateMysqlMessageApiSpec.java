package com.project.benchmark.domain.message.ui.mysql.spec;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "MySQL Benchmark")
@RequestMapping("/api/mysql/messages")
public interface CreateMysqlMessageApiSpec {

    @Operation(summary = "MySQL 메시지 생성")
    @PostMapping
    ResponseEntity<BaseResponse<Long>> create(@RequestBody CreateMessageRequest request);
}
