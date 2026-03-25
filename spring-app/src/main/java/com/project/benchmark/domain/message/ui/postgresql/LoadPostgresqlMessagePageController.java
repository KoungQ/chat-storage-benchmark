package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.domain.message.application.usecase.postgresql.LoadPostgresqlMessagePageUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.LoadPostgresqlMessagePageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadPostgresqlMessagePageController implements LoadPostgresqlMessagePageApiSpec {

    private final LoadPostgresqlMessagePageUseCase loadPostgresqlMessagePageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessagePageResponse>> loadPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadPostgresqlMessagePageUseCase.execute(page, size)));
    }
}
