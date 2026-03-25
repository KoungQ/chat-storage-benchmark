package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mysql.LoadMysqlMessageByIdUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.LoadMysqlMessageByIdApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMysqlMessageByIdController implements LoadMysqlMessageByIdApiSpec {

    private final LoadMysqlMessageByIdUseCase loadMysqlMessageByIdUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessageResponse>> loadById(@PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadMysqlMessageByIdUseCase.execute(id)));
    }
}
