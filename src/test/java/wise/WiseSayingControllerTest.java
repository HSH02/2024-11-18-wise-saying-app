package wise;

import org.junit.jupiter.api.*;
import wise.controller.WiseSayingController;
import wise.repository.WiseSayingRepository;
import wise.service.WiseSayingService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static wise.WiseSayingTestInput.*;

class WiseSayingControllerTest {
    private static final String TEST_DB_PATH = "src/test/resources/db/wiseSaying/";
    private WiseSayingService wiseSayingService;
    private WiseSayingController wiseSayingController;

    // System.out 출력을 캡처하기 위한 변수
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();   // 메모리에 출력을 저장하는 스트림
    private final PrintStream originalOut = System.out;     // System.out를 저장하고 테스트 후 복구하기 위한 용도

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent)); // System.out을 캡처

        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository(false);
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingController = new WiseSayingController(wiseSayingService);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut); // System.out을 원래 상태로 복원
        wiseSayingService.clearTestPath(); // 테스트 DB 정리
    }

    private void setInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    // 명언 등록 성공 테스트
    @Test
    @DisplayName("명언 등록 성공 테스트")
    void addSuccess() throws Exception {
        // given
        setInput(INPUT_ADD);

        // when
        wiseSayingController.run();
        String output = outContent.toString();

        // then
        System.err.println("\n=== 출력 메시지 검증 ===");
        assertAll(
                () -> assertTrue(output.contains("== 명언 앱 =="), "앱 시작 메시지가 출력되어야 함"),
                () -> assertTrue(output.contains("명언 :"), "명언 입력 프롬프트가 출력되어야 함"),
                () -> assertTrue(output.contains("작가 :"), "작가 입력 프롬프트가 출력되어야 함"),
                () -> assertTrue(output.contains("1번 명언이 등록되었습니다."), "등록 성공 메시지가 출력되어야 함")
        );
    }

    // 명언 목록 조회 성공 테스트
    @DisplayName("명언 목록 조회 테스트")
    @Test
    void findAllSuccess() {
        // given
        setInput(INPUT_FIND_ALL);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString(); // 캡처된 내용을 문자열로 변환

        System.err.println("\n=== 출력 메시지 검증 ===");
        assertAll(
                () -> assertTrue(output.contains("1 / Author 1 / Wise 1"), "1번 명언은 출력되어야 합니다"),
                () -> assertTrue(output.contains("2 / Author 2 / Wise 2"), "2번 명언은 출력되어야 합니다")
        );
    }

    // 명언 삭제 성공 테스트
    @DisplayName("명언 삭제 테스트")
    @Test
    void deleteSuccess() {
        // given
        setInput(INPUT_DELETE_SUCCESS);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString(); // 캡처된 내용을 문자열로 변환

        assertAll(
                () -> assertTrue(output.contains("1번 명언이 삭제되었습니다."), "1번 명언은 삭제되어야 합니다")
        );
    }

    // 명언 삭제 실패 테스트
    @DisplayName("명언 삭제 실패 테스트")
    @Test
    void deleteFail_IDNone() {
        // given
        setInput(INPUT_DELETE_FAIL_NULL_ID);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString(); // 캡처된 내용을 문자열로 변환

        assertAll(
                () -> assertTrue(output.contains("1번 명언은 존재하지 않습니다."), "1번 명언은 존재하지 않아야 합니다.")
        );
    }

    // 명언 수정 성공 테스트
    @DisplayName("명언 수정 테스트")
    @Test
    void updateSuccess() {
        // given
        setInput(INPUT_UPDATE_SUCCESS);

        // when
        wiseSayingController.run();

        // then
        String output = outContent.toString(); // 캡처된 내용을 문자열로 변환

        assertAll(
                () -> assertTrue(output.contains("2번 명언이 수정되었습니다."), "2번 명언은 수정되어야 합니다.")
        );
    }

    // 빌드 성공 테스트
    @DisplayName("빌드 처리 테스트")
    @Test
    void testBuildSuccess() {
        // given
        setInput(INPUT_BUILD);

        // when;
        wiseSayingController.run();

        // then
        String output = outContent.toString(); // 캡처된 내용을 문자열로 변환

        assertAll(
                () -> assertTrue(output.contains("data.json 파일의 내용이 갱신되었습니다."), "data.json 파일은 갱신되어야만 합니다.")
        );
    }

}
