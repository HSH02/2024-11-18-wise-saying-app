package org.ll.controller;

import org.ll.entity.Quote;
import org.ll.service.QuoteService;

import java.util.Scanner;

public class QuoteController {
    private QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);

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

    // 명언 등록
    private void addQuote(Scanner scanner){
        System.out.print("명언 : ");
        String wiseSaying = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        quoteService.addQuote(wiseSaying, author);
    }

    // 명언 목록 출력
    private void listQuotes () {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (Quote quote : quoteService.getAllQuotes()) {
            System.out.println(quote.toString());
        }
    }

    // 명언 삭제
    private void removeQuote(String command){
        try {
            int id = Integer.parseInt(command.split("=")[1]); // ID 추출
            quoteService.removeQuote(id); // 삭제 여부
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
            Quote quote = quoteService.getQuoteById(id);

            System.out.println("명언(기존): " + quote.getWiseSaying());
            System.out.print("명언 : ");
            String newWiseSaying = scanner.nextLine();

            System.out.println("작가(기존): " + quote.getAuthor());
            System.out.print("작가: ");
            String newAuthor = scanner.nextLine();

            quoteService.update(id , newWiseSaying, newAuthor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 오류 발생");
        }
    }




} // end of Controller
