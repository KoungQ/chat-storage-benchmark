package com.project.benchmark.domain.message.ui.mysql.spec;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "MySQL Benchmark")
@RequestMapping("/api/mysql/messages")
public interface LoadMysqlMessagePageApiSpec {

    @Operation(summary = "MySQL 페이지 조회")
    @GetMapping("/page")
    ResponseEntity<BaseResponse<MessagePageResponse>> loadPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    );
}
