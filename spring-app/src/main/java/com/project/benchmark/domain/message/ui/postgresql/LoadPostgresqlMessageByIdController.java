package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.postgresql.LoadPostgresqlMessageByIdUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.LoadPostgresqlMessageByIdApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadPostgresqlMessageByIdController implements LoadPostgresqlMessageByIdApiSpec {

    private final LoadPostgresqlMessageByIdUseCase loadPostgresqlMessageByIdUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> loadById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadPostgresqlMessageByIdUseCase.execute(id)));
    }
}
