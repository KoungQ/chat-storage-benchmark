package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessageResponse;
import com.project.benchmark.domain.message.application.usecase.mysql.SearchMysqlMessagesByConditionUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.SearchMysqlMessagesApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchMysqlMessagesController implements SearchMysqlMessagesApiSpec {

    private final SearchMysqlMessagesByConditionUseCase searchMysqlMessagesByConditionUseCase;

    @Override
    public ResponseEntity<BaseResponse<List<MessageResponse>>> search(@RequestParam Long senderId) {
        return ResponseEntity.ok(BaseResponse.onSuccess(searchMysqlMessagesByConditionUseCase.execute(senderId)));
    }
}
