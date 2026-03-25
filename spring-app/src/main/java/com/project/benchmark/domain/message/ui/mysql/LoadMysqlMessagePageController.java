package com.project.benchmark.domain.message.ui.mysql;

import com.project.benchmark.domain.message.application.dto.response.MessagePageResponse;
import com.project.benchmark.domain.message.application.usecase.mysql.LoadMysqlMessagePageUseCase;
import com.project.benchmark.domain.message.ui.mysql.spec.LoadMysqlMessagePageApiSpec;
import com.project.benchmark.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMysqlMessagePageController implements LoadMysqlMessagePageApiSpec {

    private final LoadMysqlMessagePageUseCase loadMysqlMessagePageUseCase;

    @Override
    public ResponseEntity<BaseResponse<MessagePageResponse>> loadPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.onSuccess(loadMysqlMessagePageUseCase.execute(page, size)));
    }
}
