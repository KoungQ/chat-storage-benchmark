package com.project.benchmark.domain.message.ui.postgresql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.postgresql.SearchPostgresqlMessagesByConditionUseCase;
import com.project.benchmark.domain.message.ui.postgresql.spec.SearchPostgresqlMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchPostgresqlMessagesController implements SearchPostgresqlMessagesApiSpec {

    private final SearchPostgresqlMessagesByConditionUseCase searchPostgresqlMessagesByConditionUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> search(@RequestParam Long senderId) {
        return ResponseEntity.ok(BaseResponse.onSuccess(searchPostgresqlMessagesByConditionUseCase.execute(senderId)));
    }
}
