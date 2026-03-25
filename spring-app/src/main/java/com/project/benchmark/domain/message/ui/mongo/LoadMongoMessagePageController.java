package com.project.benchmark.domain.message.ui.mongo;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.domain.message.application.usecase.mongo.LoadMongoMessagePageUseCase;
import com.project.benchmark.domain.message.ui.mongo.spec.LoadMongoMessagePageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMongoMessagePageController implements LoadMongoMessagePageApiSpec {

    private final LoadMongoMessagePageUseCase loadMongoMessagePageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessagePageResponse>> loadPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadMongoMessagePageUseCase.execute(page, size)));
    }
}
