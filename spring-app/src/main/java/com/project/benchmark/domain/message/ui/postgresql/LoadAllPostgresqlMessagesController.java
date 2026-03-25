package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.postgresql.LoadAllPostgresqlMessagesUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.LoadAllPostgresqlMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadAllPostgresqlMessagesController implements LoadAllPostgresqlMessagesApiSpec {

    private final LoadAllPostgresqlMessagesUseCase loadAllPostgresqlMessagesUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> loadAll() {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadAllPostgresqlMessagesUseCase.execute()));
    }
}
