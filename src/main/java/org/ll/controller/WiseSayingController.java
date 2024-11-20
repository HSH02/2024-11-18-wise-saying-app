package org.ll.controller;

import org.ll.model.WiseSaying;
import org.ll.service.WiseSayingService;

import java.io.IOException;
import java.util.Scanner;

/*
 * 역할 : 고객의 명령을 입력받고 적절을 응답을 표현
 * 이 단계에서는 스캐너 사용가능
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
        final Scanner scanner = new Scanner(System.in);
        displayStartMessage(); // 시작 메시지 출력

        while (true) {
            System.out.print("명령 ) ");
            String inputCommand = scanner.nextLine().trim(); // 명령 입력

            if (inputCommand.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            // 명령어에 따라 분기 처리
            switch (parseCommand(inputCommand)) {
                case "등록" -> addWiseSaying(scanner);
                case "목록" -> displayWiseSayingList();
                case "삭제" -> removeWiseSaying(inputCommand);
                case "수정" -> updateWiseSaying(inputCommand, scanner);
                //case "빌드" ->
                default -> System.out.println("올바른 명령이 아닙니다.");
            }
        }

        scanner.close(); // 프로그램 종료 전 Scanner 닫기
    }

    // 시작 메시지 출력
    private void displayStartMessage() {
        String startMessage = wiseSayingService.getDisplayStartMessage();
        System.out.println(startMessage);
    }

    // 명언 등록
    private void addWiseSaying(Scanner scanner){
        System.out.print("명언 : ");
        String wiseSaying = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        try {
            int id = wiseSayingService.addWiseSaying(wiseSaying, author);
            System.out.println(id + "번 명언이 등록되었습니다.");
        } catch (IOException e) {
            System.err.println("명언 등록 중 오류 발생: \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생: " + e.getMessage());
        }
    }

    // 명언 목록 출력
    private void displayWiseSayingList () {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        try {
            for (WiseSaying wiseSaying : wiseSayingService.getAllWiseSayings()) {
                System.out.println(wiseSaying.toString());
            }
        } catch (IOException e) {
            System.err.println("목록 출력 중 오류 발생: \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생: " + e.getMessage());
        }
    }

    // 명언 삭제
    private void removeWiseSaying(String command){
        try {
            int id = extractId(command); // ID 추출

            if (id == -1) return; // 잘못된 ID일 경우 처리 중지

            String resultMessage = wiseSayingService.removeWiseSayingById(id);
            System.out.println(resultMessage);
        } catch (IOException e) {
            System.err.println("명언 삭제 중 오류 발생: \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생: " + e.getMessage());
        }
    }

    // 명언 수정
    private void updateWiseSaying(String command, Scanner scanner) {
        int id = extractId(command); // ID 추출

        if (id == -1) return; // 잘못된 ID일 경우 처리 중지

        String newWiseSaying;
        String newAuthor;

        try {
            // 1. WiseSaying 객체를 ID로 가져오기
            WiseSaying wiseSaying = wiseSayingService.getWiseSayingById(id);

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
            String resultMessage = wiseSayingService.updateWiseSayingById(id, newWiseSaying, newAuthor);
            System.out.println(resultMessage);
        } catch (IOException e) {
            System.out.println("명언 수정 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생: " + e.getMessage());
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
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("잘못된 ID 형식입니다.");
            return -1;
        }
    }

} // end of Controller

