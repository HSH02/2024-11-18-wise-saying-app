package wise.controller;

import wise.model.WiseSaying;
import wise.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

// TO-DO 예외처리 핸들러
public class WiseSayingController {
    private final WiseSayingService service;
    private final Scanner sc;

    // 생성자
    public WiseSayingController(WiseSayingService service) {
        this(service, new Scanner(System.in));
    }

    public WiseSayingController(WiseSayingService service, Scanner sc ) {
        this.service = service;
        this.sc = sc;
    }

    // 프로그램 실행 메서드
    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine().trim();

            if (command.equals("종료")) {
                System.out.println("프로그램이 종료되었습니다.");
                break;
            } else if (command.equals("등록")) {
                add();
            } else if (command.startsWith("목록")) {
                find(command);
            } else if (command.startsWith("삭제")) {
                delete(command);
            } else if (command.startsWith("수정")) {
                update(command);
            } else if (command.equals("빌드")) {
                build();
            }
        }
    }

    private void add() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        if (checkValid(content, author)) { return; }

        int id = service.add(content, author);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    // 명언 목록 출력
    private void find(String command) {
        int page = 1;
        int totalPages = service.getTotalPages();
        String keywordType = "";
        String keyword = "";


        if(command.contains("?")){
            String[] parts = command .split("\\?");
            String[] params = parts[1].split("&");

            for(String param : params) {
                String[] keyValue = param.split("=");
                switch (keyValue[0]) {
                    case "page" -> page = Integer.parseInt(keyValue[1]);
                    case "keywordType" -> keywordType = keyValue[1];
                    case "keyword" -> keyword = keyValue[1];
                }
            }
        }
//
        List<WiseSaying> wiseSayings = service.findAll();
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying wiseSaying : wiseSayings) {
            System.out.printf("%d / %s / %s\n",
                    wiseSaying.getId(),
                    wiseSaying.getAuthor(),
                    wiseSaying.getContent());
        }

        System.out.println("----------------------");
        System.out.printf("페이지 : %s / %s\n",
                                                page == 1? "[1]":"1",
                                                page == totalPages?"["+ totalPages+"]": totalPages);
    }

    // 명언 삭제
    private void delete(String command){
        int id = parseId(command);
        if (id == -1) return;

        if (service.delete(id)){
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    // 명언 수정
    private void update(String command) {
        int id = parseId(command);
        if (id == -1) return;

        WiseSaying wiseSaying = service.findById(id);
        if (wiseSaying == null) {
            System.out.printf(id + "번 명언은 존재하지 않습니다.");
            return;
        }

        System.out.println("명언(기존) : " + wiseSaying.getContent());
        System.out.print("명언 : ");
        String newContent = sc.nextLine().trim();

        System.out.println("작가(기존) : " + wiseSaying.getAuthor());
        System.out.print("작가 : ");
        String newAuthor = sc.nextLine().trim();

        if (checkValid(newContent, newAuthor)) { return; }

        if(service.update(id, newContent, newAuthor)){
            System.out.println(id + "번 명언이 수정되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    private void build() {
        if (service.build()){
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }
    }

    private int parseId(String command) {
        String[] parts = command.split("=");
        if (parts.length < 2) {
            System.out.println("잘못된 명령어 형식입니다.");
            return -1;
        }
        return Integer.parseInt(parts[1]);
    }

    private boolean checkValid(String content, String author){
        if(content.isEmpty() || author.isEmpty()){
            System.out.println("작가나 명언이 비어있을 수는 없습니다.");
            return true;
        }
        return false;
    }

} // end of Controller

