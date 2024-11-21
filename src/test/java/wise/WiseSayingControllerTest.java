package wise;

import org.junit.jupiter.api.*;
import wise.repository.WiseSayingRepository;
import wise.service.WiseSayingService;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static wise.WiseSayingTestInput.*;

class WiseSayingControllerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private WiseSayingRepository wiseSayingRepository;
    private WiseSayingService wiseSayingService;
    private WiseSayingController wiseSayingController;

    @BeforeEach
    public void setUpStreams() {
        wiseSayingRepository = new WiseSayingRepository("db/test/");
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingController = new WiseSayingController(wiseSayingService);

        // 표준 출력 리다이렉션
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        // 표준 입출력 원래대로 복원
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // 명언 등록 성공 테스트
    @Test
    void addSuccess() {
        // 입력 시뮬레이션
        ByteArrayOutputStream output = setOutToByteArray();

        // 컨트롤러 실행
        wiseSayingController.run();

        // 출력 내용 검증
        String result = outContent.toString().trim();
        // int id = wiseSayingRepository.();
        System.out.println(output);
        // assertTrue(output.result("번 명언이 등록되었습니다."));

        clearSetOutToByteArray(output);
        System.out.println(output);
    }

    // 명언 등록 실패 - 빈 입력 테스트
//    @DisplayName("명언 등록에 실패했습니다.")
//    @Test
//    void testAddFailure_BlankInput() {
//        System.setIn(new ByteArrayInputStream(INPUT_ADD_FAIL_BLANK.getBytes()));
//
//        wiseSayingController.run();
//
//        String output = outContent.toString();
//        //assertTrue(output.contains("내용이 비어있을 수는 없습니다."));
//        System.out.println(output);
//    }

    // 명언 목록 조회 성공 테스트
    @DisplayName("명언 목록을 조회합니다.")
    @Test
    void testFindAllSuccess() {
        System.setIn(new ByteArrayInputStream(INPUT_FIND_ALL.getBytes()));

        wiseSayingController.run();

        String output = outContent.toString();
        //assertTrue(output.contains("Wise  1"));
        //assertTrue(output.contains("Wise 2"));
        System.out.println(output);
    }

    // 명언 삭제 성공 테스트
    @DisplayName("명언을 삭제합니다.")
    @Test
    void testDeleteSuccess() {
        System.setIn(new ByteArrayInputStream(INPUT_DELETE_SUCCESS.getBytes()));

        wiseSayingController.run();

        String output = outContent.toString();
        // assertTrue(output.contains("1번 명언이 삭제되었습니다."));
        System.out.println(output);
    }

    // 명언 삭제 실패 - 존재하지 않는 ID 테스트
//    @Test
//    void testDeleteFailure_IDNone() {
//        System.setIn(new ByteArrayInputStream(INPUT_DELETE_FAIL_ID_NONE.getBytes()));
//
//        wiseSayingController.run();
//
//        String output = outContent.toString();
//        // assertTrue(output.contains("10000번 명언은 존재하지 않습니다."));
//        System.out.println(output);
//    }

    // 명언 수정 성공 테스트
    @DisplayName("명언을 수정합니다.")
    @Test
    void testUpdateSuccess() {
        System.setIn(new ByteArrayInputStream(INPUT_UPDATE_SUCCESS.getBytes()));

        wiseSayingController.run();

        String output = outContent.toString();
        // assertTrue(output.contains("1번 명언이 수정되었습니다."));
        System.out.println(output.trim());
    }

    // 명언 수정 실패 - 존재하지 않는 ID 테스트
//    @Test
//    void testUpdateFailure_IDNone() {
//        System.setIn(new ByteArrayInputStream(INPUT_UPDATE_FAIL_ID_NONE.getBytes()));
//
//        wiseSayingController.run();
//
//        String output = outContent.toString();
//        // assertTrue(output.contains("10000번 명언은 존재하지 않습니다."));
//        System.out.println(output);
//    }

    // 빌드 성공 테스트
    @DisplayName("빌드 처리를 진행합니다..")
    @Test
    void testBuildSuccess() {
        System.setIn(new ByteArrayInputStream(INPUT_BUILD.getBytes()));

        wiseSayingController.run();

        String output = outContent.toString();
        // assertTrue(output.contains("data.json 파일의 내용이 갱신되었습니다."));
        System.out.println(output);
    }

    public static Scanner genScanner(final String input) {
        final InputStream in = new ByteArrayInputStream(input.getBytes());

        return new Scanner(in);
    }

    // System.out의 출력을 스트림으로 받기
    public static ByteArrayOutputStream setOutToByteArray() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        return output;
    }

    // setOutToByteArray 함수의 사용을 완료한 후 정리하는 함수, 출력을 다시 정상화 하는 함수
    public static void clearSetOutToByteArray(final ByteArrayOutputStream output) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
