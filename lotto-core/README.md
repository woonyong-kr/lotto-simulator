# lotto-core

여러 모듈을 설계하면서 공통 라이브러리로 사용하고자 만들었습니다. 중앙 서버, POS 터미널, 봇 클라이언트가 모두 동일한 로또 규칙을 공유해야 했기 때문에, 값 객체와 상수를 한 곳에 모아 관리합니다.

## 제공 기능

- **도메인 값 객체**
    - `LottoNumber`, `LottoNumbers`: 입력 검증과 중복 검사, 랜덤 생성 헬퍼 제공
    - `WinningNumbers`, `WinningRank`: 당첨 번호/순위 판정
    - `TicketNumber`, `TicketStatus`, `TicketType`, `RoundStatus`
- **DTO/에러**: `ApiResponse`, `ErrorResponse`, `LottoException` 기반 예외 계층
- **상수/유틸리티**: `LottoConstants`, `FormatConstants`, `JsonKeyConstants`, `LottoNumberGenerator`

## 사용 예시

```java
// 랜덤 번호 6개 생성
LottoNumbers autoNumbers = LottoNumbers.generateRandom();

// 수동 번호로 티켓 생성 시 검증
LottoNumbers manualNumbers = LottoNumbers.from(List.of(1, 2, 3, 4, 5, 6));

// 당첨 결과 판정
WinningNumbers winning = WinningNumbers.of(
        LottoNumbers.from(List.of(3, 11, 22, 33, 40, 45)),
        LottoNumber.of(7)
);
WinningRank rank = winning.match(manualNumbers);
```

## 한계

- README에 언급되어 있던 단위 테스트는 아직 (미구현) 상태입니다. 모든 모듈에서 이 라이브러리를 신뢰하므로 추후 테스트 추가가 필요합니다.
- 난수 생성기는 `java.util.Random`을 기반으로 하며 암호학적으로 강력하지 않습니다.

## 빌드

이 모듈은 `java-library` 플러그인을 사용하며, 루트 `./gradlew :lotto-core:build`로만 컴파일됩니다. 다른 모듈에서 자동으로 의존성으로 포함됩니다.
