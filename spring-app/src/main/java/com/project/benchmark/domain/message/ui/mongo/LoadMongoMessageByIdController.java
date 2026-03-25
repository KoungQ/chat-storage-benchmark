package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mongo.LoadMongoMessageByIdUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.LoadMongoMessageByIdApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMongoMessageByIdController implements LoadMongoMessageByIdApiSpec {

    private final LoadMongoMessageByIdUseCase loadMongoMessageByIdUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> loadById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadMongoMessageByIdUseCase.execute(id)));
    }
}
