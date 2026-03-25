package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mongo.LoadAllMongoMessagesUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.LoadAllMongoMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadAllMongoMessagesController implements LoadAllMongoMessagesApiSpec {

    private final LoadAllMongoMessagesUseCase loadAllMongoMessagesUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> loadAll() {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadAllMongoMessagesUseCase.execute()));
    }
}
