package wise;

import org.junit.jupiter.api.*;
import wise.controller.WiseSayingController;
import wise.repository.WiseSayingRepository;
import wise.service.WiseSayingService;
import wise.utils.FileUtils;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static wise.WiseSayingTestInput.*;

import static org.assertj.core.api.Assertions.assertThat;

class WiseSayingControllerTest {
    private WiseSayingService wiseSayingService;
    private WiseSayingController wiseSayingController;

    // System.out 출력을 캡처하기 위한 변수
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();   // 메모리에 출력을 저장하는 스트림
    private final PrintStream originalOut = System.out;     // System.out 저장하고 테스트 후 복구하기 위한 용도

    private static final String TEST_PATH = "src/test/resources/db/wiseSaying/";

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent)); // System.out 캡처

        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository(false);
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut); // System.out 원래 상태로 복원
        wiseSayingService.clearTestPath(); // 테스트 DB 정리
    }

    public static Scanner setInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());

        return new Scanner(in);
    }

    @Test
    @DisplayName("등록 명령어: 등록을 입력하면 내용과 작가를 입력받는다.")
    void t1()  {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_ADD));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("명언 : ")
                .contains("작가 : ");
    }

    @Test
    @DisplayName("등록 명령어: 등록이 완료되면 명언번호가 출력된다.")
    void t2() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_ADD));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 명령어: 매번 생성되는 명언번호는 1씩 증가 한다.")
    void t3() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_ADD));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("3번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 명령어: 입력된 명언들을 출력한다.")
    void t4() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_FIND_ALL));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("3 / Author 3 / Wise 3")
                .contains("2 / Author 2 / Wise 2")
                .contains("1 / Author 1 / Wise 1");
    }

    @Test
    @DisplayName("삭제 명령이: 번호에 해당하는 명언을 삭제한다.")
    void t5() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_DELETE_SUCCESS));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("1번 명언이 삭제되었습니다.");
    }

    // 명언 삭제 실패 테스트
    @DisplayName("삭제 명령이: 존재하지 않는 명언번호에 대한 처리")
    @Test
    void t6() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_DELETE_FAIL_NULL_ID));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("수정 명령어 : 기존 명언과 기존 작가를 보여준다.")
    void t7() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_UPDATE_SUCCESS));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("명언(기존) : Wise 2")
                .contains("작가(기존) : Author 2");
    }

    @Test
    @DisplayName("수정 명령어 : 명언이 수정된다")
    void t8() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_UPDATE_SUCCESS));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("2 / updated Author 2 / updated Wise 2");
    }

    @Test
    @DisplayName("빌드 명령어: 빌드를 입력하면 성공 메시지를 입력받는다.")
    void t9() {
        // given
        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_BUILD));

        // when
        controller.run();
        String output = outContent.toString();

        // then
        assertThat(output)
                .contains("data.json 파일의 내용이 갱신되었습니다.");

    }

//    @Test
//    @DisplayName("빌드 명령어: 빌드를 입력하면 data.json 파일이 생성된다.")
//    void t10() throws Exception {
//        // given
//        WiseSayingController controller = new WiseSayingController(wiseSayingService, setInput(INPUT_BUILD));
//
//        // when
//        controller.run();
//        String output = outContent.toString();
//
//        // then
//        assertThat(output)
//                .contains("data.json 파일의 내용이 갱신되었습니다.");
//
//        String expectedName = "data.json";
//        //String expectedContent = ""
//        boolean isValid = FileUtils.isFileValid(TEST_PATH, expectedName, expectedContent);
//        assertTrue(isValid);
//    }

}

