package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mongo.UpdateMongoMessageUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.UpdateMongoMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateMongoMessageController implements UpdateMongoMessageApiSpec {

    private final UpdateMongoMessageUseCase updateMongoMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> update(@PathVariable Long id, @RequestBody UpdateMessageRequest request) {
        return ResponseEntity.ok(BaseResponse.onSuccess(updateMongoMessageUseCase.execute(id, request)));
    }
}
