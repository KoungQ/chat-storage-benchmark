package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.usecase.postgresql.DeletePostgresqlMessageUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.DeletePostgresqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeletePostgresqlMessageController implements DeletePostgresqlMessageApiSpec {

    private final DeletePostgresqlMessageUseCase deletePostgresqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        deletePostgresqlMessageUseCase.execute(id);
        return ResponseEntity.ok(BaseResponse.onSuccess());
    }
}
