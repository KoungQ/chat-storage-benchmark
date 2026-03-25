package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.usecase.mongo.DeleteMongoMessageUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.DeleteMongoMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteMongoMessageController implements DeleteMongoMessageApiSpec {

    private final DeleteMongoMessageUseCase deleteMongoMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        deleteMongoMessageUseCase.execute(id);
        return ResponseEntity.ok(BaseResponse.onSuccess());
    }
}
