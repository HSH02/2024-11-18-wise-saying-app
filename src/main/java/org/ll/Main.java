package org.ll;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("== 명언 앱 ==");

        while (true) {
            String command = scanner.nextLine();
            String wiseSaying = "";
            String writer = "";
            System.out.print("명령 ) ");

            switch (command){
                case "종료":
                    return;
                case "등록":
                    System.out.print("명언 :");
                    wiseSaying = scanner.nextLine();
                    System.out.print("작가 :");
                    writer = scanner.nextLine();
                    break;
            }
        }

    }
}