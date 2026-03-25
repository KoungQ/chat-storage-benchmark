package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mysql.LoadAllMysqlMessagesUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.LoadAllMysqlMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadAllMysqlMessagesController implements LoadAllMysqlMessagesApiSpec {

    private final LoadAllMysqlMessagesUseCase loadAllMysqlMessagesUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> loadAll() {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadAllMysqlMessagesUseCase.execute()));
    }
}
