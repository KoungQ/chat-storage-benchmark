package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.usecase.mysql.DeleteMysqlMessageUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.DeleteMysqlMessageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteMysqlMessageController implements DeleteMysqlMessageApiSpec {

    private final DeleteMysqlMessageUseCase deleteMysqlMessageUseCase;

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        deleteMysqlMessageUseCase.execute(id);
        return ResponseEntity.ok(BaseResponse.onSuccess());
    }
}
