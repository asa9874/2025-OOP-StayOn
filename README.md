# StayOn 프로젝트
실행하실때 오른쪽 코끼리 모양아이콘 (Gradle) 에서 Tasks > application > run으로 실행하셔야 실행가능합니다. 
<img width="339" height="275" alt="image" src="https://github.com/user-attachments/assets/66e10e49-530d-4259-a8c5-9a4964c46da5" />
<img width="365" height="159" alt="image" src="https://github.com/user-attachments/assets/acad8a81-52c4-4960-9d18-c07c15a7b73c" />



## 프로젝트 구조
```
src/
├── main/
│   └── java/
│       └── org/
│           └── example/
│               ├── domain/          # 비즈니스 도메인 코드
│               │   └── [도메인명]/
│               │       ├── [도메인명].java           # 도메인 엔티티
│               │       ├── [도메인명]Controller.java # 컨트롤러
│               │       ├── [도메인명]Service.java    # 서비스
│               │       ├── [도메인명]Repository.java # 리포지토리
│               │       ├── dto/                      # DTO 클래스들
│               │       └── strategy/                 # 레포지토리 데이터 전략 패턴 클래스 (필수는 아니고 CleaningStaffRepository에는 전략패턴 한번 적용해봤습니다.)
│               ├── view/            # UI 관련 코드
│               │   ├── LoginView.java   # 로그인 화면 (시작점)
│               │   ├── PensionView.java # 펜션 목록 화면 (메인)
│               │   └── [화면이름]View.java
│               ├── Init.java        # 의존성 초기화
│               └── Main.java        # 애플리케이션 시작점
└── test/
    └── java/
        └── org/
            └── example/
                └── domain/
                    └── [도메인명]/
                        └── [도메인명]Test.java
```

## 코드 작성 가이드라인

### 1. 도메인 코드 작성
- 모든 도메인은 `src/main/java/org/example/domain/[도메인명]` 디렉토리에 작성
- 각 도메인 패키지는 다음 구조를 따름:
  - `[도메인명].java`: 도메인 엔티티
  - `[도메인명]Controller.java`: 컨트롤러 클래스 (싱글톤) , 이클래스를 View에서 getinstance로 불러와서 사용하면됩니당~
  - `[도메인명]Service.java`: 서비스 클래스 (싱글톤)
  - `[도메인명]Repository.java`: 리포지토리 클래스 (싱글톤)
  - `dto/`: Request DTO 클래스들
  - `strategy/`: 전략 패턴 관련 클래스들

### 2. 싱글톤 패턴 구현
- 모든 Controller, Service, Repository는 싱글톤으로 구현
- 싱글톤 초기화는 `Init.java`의 `initializeDependencies()` 메서드에서 수행
- initialize[도메인명] 으로 `Init.java` 에 메서드 생성후 진행


### 3. View 구현
- 모든 View 클래스는 `src/main/java/org/example/view` 디렉토리에 작성
- **LoginView**: 애플리케이션 시작 화면 (로그인)
- **PensionView**: 로그인 후 메인 화면 (펜션 목록)
- 화면 전환 구조:
  1. LoginView → PensionView (로그인 성공 시)
  2. PensionView에서 각 기능 화면으로 이동
- View에서 Controller 사용 시 직접 싱글톤 인스턴스를 가져와서 사용:
```java
public class SomeView {
    private final SomeController controller = SomeController.getInstance();
    // ...
}
```

### 4. 테스트 코드 작성
- 모든 테스트는 `src/test/java/org/example/domain/[도메인명]` 디렉토리에 작성
- 테스트 클래스 명명: `[도메인명]Test.java`
- 각 테스트 메서드는 다음 형식으로 작성:
  - 메서드명 예: `저장_유효한입력_성공적으로저장됨()`
- 테스트 구조:
```java
class SomeTest {
    private SomeController controller;
    private SomeRepository repository;

    @BeforeEach
    void setUp() {
        Init.initialize[도메인명]();
        controller = SomeController.getInstance();
        repository = SomeRepository.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 테스트메서드() {
        // given
        // 테스트 데이터 설정

        // when
        // 테스트할 동작 수행

        // then
        // 결과 검증
    }
}
```

## 주의사항
1. 모든 의존성 초기화는 반드시 `Init.initializeDependencies()`를 통해 수행
2. 화면 전환은 LoginView → PensionView를 기본으로 하고, PensionView에서 각 기능 화면으로 이동
3. 각 도메인의 Controller, Service, Repository는 반드시 싱글톤으로 구현
4. 테스트 코드는 항상 @BeforeEach에서 의존성을 초기화하고 데이터를 리셋
