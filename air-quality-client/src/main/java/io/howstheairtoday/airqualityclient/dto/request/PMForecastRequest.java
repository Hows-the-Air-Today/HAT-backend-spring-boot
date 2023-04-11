package io.howstheairtoday.airqualityclient.dto.request;


import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@Getter
public class PMForecastRequest {

    // 서비스 키
    private String serviceKey;

    // 데이터 표출 방식 (json, xml)
    private String returnType;

    // 한 페이지 결과 수
    private int numOfRows;

    // 페이지 번호
    private int pageNo;

    // 조회 날짜
    private LocalDate searchDate;

    // 통보 코드
    private String informCode;
}
