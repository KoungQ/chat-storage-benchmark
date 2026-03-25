package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.request.UpdateMessageRequest;
import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mysql.UpdateMysqlMessageUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.UpdateMysqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateMysqlMessageController implements UpdateMysqlMessageApiSpec {

    private final UpdateMysqlMessageUseCase updateMysqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> update(@PathVariable Long id, @RequestBody UpdateMessageRequest request) {
        return ResponseEntity.ok(BaseResponse.onSuccess(updateMysqlMessageUseCase.execute(id, request)));
    }
}
