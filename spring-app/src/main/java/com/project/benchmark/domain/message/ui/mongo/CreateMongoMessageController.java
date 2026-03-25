package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.request.CreateMessageRequest;
import com.project.benchmark.domain.message.application.usecase.mongo.CreateMongoMessageUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.CreateMongoMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CreateMongoMessageController implements CreateMongoMessageApiSpec {

    private final CreateMongoMessageUseCase createMongoMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Long>> create(@RequestBody @Valid CreateMessageRequest request) {
        Long id = createMongoMessageUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/mongo/messages/" + id))
                .body(BaseResponse.onSuccess(id));
    }
}
