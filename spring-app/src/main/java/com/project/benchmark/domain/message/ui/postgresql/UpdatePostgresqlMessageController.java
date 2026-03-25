package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.postgresql.UpdatePostgresqlMessageUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.UpdatePostgresqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatePostgresqlMessageController implements UpdatePostgresqlMessageApiSpec {

    private final UpdatePostgresqlMessageUseCase updatePostgresqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> update(@PathVariable Long id, @RequestBody UpdateMessageRequest request) {
        return ResponseEntity.ok(BaseResponse.onSuccess(updatePostgresqlMessageUseCase.execute(id, request)));
    }
}
