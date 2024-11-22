package wise;

import org.junit.jupiter.api.*;
import wise.controller.WiseSayingController;
import wise.repository.WiseSayingRepository;
import wise.service.WiseSayingService;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static wise.WiseSayingTestInput.*;

class WiseSayingControllerTest {
    private WiseSayingService wiseSayingService;
    private WiseSayingController wiseSayingController;

    // System.out 출력을 캡처하기 위한 변수
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // 출력 스트림 캡처
        System.setOut(new PrintStream(outContent));

        // 객체 초기화
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository(false);
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingController = new WiseSayingController(wiseSayingService);
    }

    @AfterEach
    public void tearDown() {
        // 출력 스트림 복구
        System.setOut(originalOut);
    }

    private void setInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    // 명언 등록 성공 테스트
    @Test
    @DisplayName("명언 등록 성공 테스트")
    void addSuccess() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_ADD);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("명언 : "), "명언 입력 프롬프트가 출력되어야 합니다.");
        assertTrue(output.contains("작가 : "), "작가 입력 프롬프트가 출력되어야 합니다.");
        assertTrue(output.contains("번 명언이 등록되었습니다."), "등록 완료 메시지가 출력되어야 합니다.");
    }

    // 명언 목록 조회 성공 테스트
    @DisplayName("명언 목록 조회 테스트")
    @Test
    void findAllSuccess() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_FIND_ALL);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("1 / Author 1 / Wise 1"), "1번 명언은 출력되어야 합니다");
        assertTrue(output.contains("2 / Author 2 / Wise 2"), "2번 명언은 출력되어야 합니다");
    }

    // 명언 삭제 성공 테스트
    @DisplayName("명언 삭제 테스트")
    @Test
    void deleteSuccess() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_DELETE_SUCCESS);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("번 명언이 삭제되었습니다."), "1번 명언은 삭제되어야 합니다");
    }

    // 명언 삭제 실패 테스트
    @DisplayName("명언 삭제 실패 테스트")
    @Test
    void deleteFail_IDNone() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_DELETE_FAIL_NULL_ID);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("해당 파일은 존재하지 않습니다."), "명언이 존재하지 않을 경우 메시지가 출력되어야 합니다");
    }

    // 명언 수정 성공 테스트
    @DisplayName("명언 수정 테스트")
    @Test
    void updateSuccess() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_UPDATE_SUCCESS);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("번 명언이 수정되었습니다."), "명언은 수정되어야 합니다.");
    }

    // 빌드 성공 테스트
    @DisplayName("빌드 처리 테스트")
    @Test
    void testBuildSuccess() {
        // given
        wiseSayingService.clearTestPath();
        setInput(INPUT_BUILD);

        // when;
        wiseSayingController.run();

        // then
        String output = outContent.toString();
        System.err.println(output); // 디버깅용 출력

        assertTrue(output.contains("data.json 파일의 내용이 갱신되었습니다."), "data.json 파일은 갱신되어야만 합니다.");
    }

}
