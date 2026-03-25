package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mongo.SearchMongoMessagesByConditionUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.SearchMongoMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchMongoMessagesController implements SearchMongoMessagesApiSpec {

    private final SearchMongoMessagesByConditionUseCase searchMongoMessagesByConditionUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> search(@RequestParam Long senderId) {
        return ResponseEntity.ok(BaseResponse.onSuccess(searchMongoMessagesByConditionUseCase.execute(senderId)));
    }
}
