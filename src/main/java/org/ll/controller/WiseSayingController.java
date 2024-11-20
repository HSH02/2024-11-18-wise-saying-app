package org.ll.controller;

import org.ll.model.WiseSaying;
import org.ll.service.WiseSayingService;

import java.util.Scanner;

public class WiseSayingController {
    private WiseSayingService wiseSayingService;

    public WiseSayingController(WiseSayingService wiseSayingService) {
        this.wiseSayingService = wiseSayingService;
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);
        start();

        while (true) {
            System.out.print("명령 ) ");
            String command = scanner.nextLine().trim();

            if (command.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (command.equals("등록")) {
                addQuote(scanner);
            } else if (command.equals("목록")) {
                listQuotes();
            } else if (command.startsWith("삭제?id=")) {
                removeQuote(command);
            } else if (command.startsWith("수정?id=")) {
                updateQuote(command, scanner);
            } else {
                System.out.println("알 수 없는 명령입니다.");
            }
        } // end of while

        scanner.close(); // 반복문 끝나고 호출
    } // end of run

    private void start(){
        String content = wiseSayingService.start();
        System.out.println(content);
    }

    // 명언 등록
    private void addQuote(Scanner scanner){
        System.out.print("명언 : ");
        String wiseSaying = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        wiseSayingService.addWiseSaying(wiseSaying, author);
    }

    // 명언 목록 출력
    private void listQuotes () {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying wiseSaying : wiseSayingService.getAllWiseSaying()) {
            System.out.println(wiseSaying.toString());
        }
    }

    // 명언 삭제
    private void removeQuote(String command){
        try {
            int id = Integer.parseInt(command.split("=")[1]); // ID 추출
            wiseSayingService.removeItemByTitle(id); // 삭제 여부
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생");
        }
    }

    // 명언 수정
    private void updateQuote(String command, Scanner scanner){
        try {
            int id = Integer.parseInt(command.split("=")[1]); // ID 추출
            WiseSaying wiseSaying = wiseSayingService.getQuoteById(id);

            System.out.println("명언(기존): " + wiseSaying.getWiseSaying());
            System.out.print("명언 : ");
            String newWiseSaying = scanner.nextLine();

            System.out.println("작가(기존): " + wiseSaying.getAuthor());
            System.out.print("작가: ");
            String newAuthor = scanner.nextLine();

            wiseSayingService.update(id , newWiseSaying, newAuthor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생");
        }
    }


} // end of Controller
