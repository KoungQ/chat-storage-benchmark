package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.domain.message.application.usecase.mysql.CreateMysqlMessageUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.CreateMysqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CreateMysqlMessageController implements CreateMysqlMessageApiSpec {

    private final CreateMysqlMessageUseCase createMysqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Long>> create(@RequestBody @Valid CreateMessageRequest request) {
        Long id = createMysqlMessageUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/mysql/messages/" + id))
                .body(BaseResponse.onSuccess(id));
    }
}
