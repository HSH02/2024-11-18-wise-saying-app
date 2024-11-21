package org.ll.controller;

import org.ll.model.WiseSaying;
import org.ll.service.WiseSayingService;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * 역할 : 고객의 명령을 입력받고 적절을 응답을 표현
 * 이 단계에서는 스캐너 사용가능
 * 전역 에러 헨들러로 빼보기
 */
public class WiseSayingController {
    // Service 객체
    private final WiseSayingService wiseSayingService;

    // 생성자
    public WiseSayingController(WiseSayingService wiseSayingService) {
        this.wiseSayingService = wiseSayingService;
    }

    // 프로그램 실행 메서드
    public void run() {

        displayStartMessage(); // 시작 메시지 출력

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("명령 ) ");
                String inputCommand = scanner.nextLine().trim(); // 명령 입력

                if (inputCommand.equals("종료")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                // 명령어에 따라 분기 처리
                switch (parseCommand(inputCommand)) {
                    case "등록" -> add(scanner);
                    case "목록" -> findAll();
                    case "삭제" -> deleteById(inputCommand);
                    case "수정" -> updateById(inputCommand, scanner);
                    case "빌드" -> build();
                    default -> System.out.println("올바른 명령이 아닙니다.");
                }
            }
        } catch (Exception e) {
            System.err.println("예상치 못한 오류 발생: " + e.getMessage());
        }
    }

    // 시작 메시지 출력
    private void displayStartMessage() {
        String startMessage = "== 명언 앱 ==";
        System.out.println(startMessage);
    }

    // 명언 등록
    private void add(Scanner scanner){
        System.out.print("명언 : ");
        String wiseSaying = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        try {
            int id = wiseSayingService.add(wiseSaying, author);
            System.out.println(id + "번 명언이 등록되었습니다.");
        } catch (Exception e) {
            System.err.println("명언 등록 중 오류 발생: \n" + e.getMessage());
        }
    }

    // 명언 목록 출력
    private void findAll() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        try {
            for (WiseSaying wiseSaying : wiseSayingService.findAll()) {
                System.out.println(wiseSaying.toString());
            }
        } catch (Exception e) {
            System.err.println("목록 출력 중 오류 발생: \n" + e.getMessage());
        }
    }

    // 명언 삭제
    private void deleteById(String command){
        try {
            int id = extractId(command); // ID 추출

            if (id == -1) return; // 잘못된 ID일 경우 처리 중지

            String resultMessage = wiseSayingService.deleteById(id);
            System.out.println(resultMessage);
        } catch (Exception e) {
            System.err.println("명언 삭제 중 오류 발생: \n" + e.getMessage());
        }
    }

    // 명언 수정
    private void updateById(String command, Scanner scanner) {
        int id = extractId(command); // ID 추출

        if (id == -1) return; // 잘못된 ID일 경우 처리 중지

        String newWiseSaying;
        String newAuthor;

        try {
            // 1. WiseSaying 객체를 ID로 가져오기
            WiseSaying wiseSaying = wiseSayingService.findByID(id);

            // 2. 기존 데이터 출력
            System.out.println("명언(기존): " + wiseSaying.getWiseSaying());
            System.out.print("명언 : ");
            newWiseSaying = scanner.nextLine();

            System.out.println("작가(기존): " + wiseSaying.getAuthor());
            System.out.print("작가: ");
            newAuthor = scanner.nextLine();

            // 3. 유효성 체크
            if (newWiseSaying.trim().isEmpty() || newAuthor.trim().isEmpty()) {
                System.out.println("명언 또는 작가 이름는 비어있을 수 없습니다");
                return;
            }

            // 4. 수정된 정보로 업데이트
            String resultMessage = wiseSayingService.updateById(id, newWiseSaying, newAuthor);
            System.out.println(resultMessage);
        } catch (Exception e) {
            System.out.println("명언 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // data.json 메시지 반환
    private void build() {
        try{
            String resultMessage = wiseSayingService.build();
            System.out.println(resultMessage);
        } catch (Exception e) {
            System.out.println("빌드 중 오류 발생: " + e.getMessage());
        }
    }

    // 명령 판별
    private String parseCommand(String command){
        if(command.startsWith("삭제?id=")) return "삭제";
        if(command.startsWith("수정?id=")) return "수정";
        return command; // 단일 명령어 반환
    }

    // ID 추출
    private int extractId(String command){
        String[] parts = command.split("=");

        try {
            return Integer.parseInt(parts[1]); // ID가 있으면 그 값을 반환
        } catch (Exception e) {
            System.out.println("잘못된 ID 형식입니다.");
            return -1;
        }
    }

} // end of Controller

