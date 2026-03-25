package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.domain.message.application.usecase.postgresql.CreatePostgresqlMessageUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.CreatePostgresqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CreatePostgresqlMessageController implements CreatePostgresqlMessageApiSpec {

    private final CreatePostgresqlMessageUseCase createPostgresqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Long>> create(@RequestBody @Valid CreateMessageRequest request) {
        Long id = createPostgresqlMessageUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/postgresql/messages/" + id))
                .body(BaseResponse.onSuccess(id));
    }
}
