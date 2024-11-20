package org.ll.repository;

import org.ll.model.WiseSaying;

import java.io.*;
import java.util.*;

public class WiseSayingRepository {
    List<WiseSaying> wiseSayings = new ArrayList<>(); // 명령 저장 리스트

    private static final String FILE_DIRECTORY = "db/wiseSaying/"; // JSON 파일 위치
    private static final String LAST_ID_FILE = FILE_DIRECTORY + "lastId.txt"; // 마지막 ID 저장 파일

    // 디텍토리 존재 여부를 확인하는 메서드
    public boolean fileDirectory(){
        // 1. 디텍토리 존재 여부 확인
        File direcotry = new File(FILE_DIRECTORY);

        // 2. 없을 경우 false 반환
        if(!direcotry.exists()) {
            return false;
        }

        // 3. 존재할 경우 true 반환
        return true;
    }

    // 명언 저장
    public void saveWiseSayingToFile(WiseSaying wiseSaying, int newId) {
        try {
            // 1. 파일 경로 생성
            String fileName = FILE_DIRECTORY + newId  +".json";

            // 2. 디텍토리 존재 여부 확인
            File direcotry = new File(FILE_DIRECTORY);
            if(!direcotry.exists()) {
                direcotry.mkdirs();
            }

            // 3. 파일 쓰기 작업 시작
            try(FileWriter writer = new FileWriter(fileName, true)){
                writer.write(wiseSaying.toJson());  // 데이터 작성
            }
        } catch (IOException e) {
            // 오류 발생시 로그 출력
            System.out.println("파일 저장 중 오류 발생 : " + e.getMessage());
        }
    }

    // 명언 전부 반환
    public List<WiseSaying> gelAllWiseSaying(){
        List<WiseSaying> wiseSayingsTmp = new ArrayList<>(); // 명언 목록

        // 1. 디텍토리 존재 여부 확인
        File direcotry = new File(FILE_DIRECTORY);
        if(!direcotry.exists()) {
            System.out.println("디텍토리가 존재하지 않습니다.");
            return wiseSayingsTmp;  // 빈 목록 반환
        }

        File[] files = direcotry.listFiles((dir, name) -> name.endsWith(".json"));
        if(files == null || files.length == 0) {
            System.out.println("저장된 파일이 없습니다.");
            return wiseSayingsTmp;
        }

        // 3. 각 파일을 읽고 JSON 파싱하여 리스트에 추가
        for(File file : files) {
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                StringBuilder jsonContent = new StringBuilder();

                // 3.1 반복문으로 파일의 한 줄씩 읽고 jsonContent에 저장
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }

                // 3.2 읽은 JSON 내용들을 String 처리로 파싱
                String jsonString = jsonContent.toString();

                // 3.3 파싱한 String을 사용하여 객체 생성 및 추가
                wiseSayingsTmp.add(WiseSaying.fromJson(jsonString));
            } catch (FileNotFoundException e) {
                System.out.println("파일 읽기 오류 : " + e.getMessage());
            } catch (IOException e) {
                System.out.println("파일 읽기 오류: " + e.getMessage());
            }
        }

        return wiseSayingsTmp;
    }

    // ID와 일치하는 명언 조회
    public WiseSaying getQuoteById(int id){
        for(WiseSaying wiseSaying : wiseSayings) {
            if(wiseSaying.getId() == id){
                return wiseSaying;
            }
        }

        return null;
    }

    // ID와 일치하는 명언 삭제
    public boolean removeItemByTitle(int id){
        String fileName = FILE_DIRECTORY + id  +".json";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("파일이 존재하지 않습니다: " + fileName);
            return false;
        }

        try {
            // 파일 삭제 시도
            if (file.delete()) {
                System.out.println("파일이 성공적으로 삭제되었습니다: " + fileName);
                return true;
            } else {
                // 삭제 실패
                System.out.println("파일 삭제에 실패했습니다: " + fileName);
                return false;
            }
        } catch (SecurityException e) {
            System.err.println("파일 삭제 권한이 없습니다: " + fileName);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("알 수 없는 오류가 발생했습니다: " + fileName);
            e.printStackTrace();
            return false;
        }
    }

    // ID와 일치하는 명언 수정
    public boolean updateQuote(int id , String newWiseSaying, String newAuthor){
        for (WiseSaying wiseSaying : wiseSayings) {
            if (wiseSaying.getId() == id) {
                wiseSaying.setWiseSaying(newWiseSaying);  // 명언 수정
                wiseSaying.setAuthor(newAuthor);          // 작가 수정
                return true;                         // 수정 성공
            }
        }

        return false;   // 수정 실패
    }


    // 마지막 ID 파일 저장 메서드
    public void saveLastIdToFile(int lastId) {
        try (FileWriter writer = new FileWriter(LAST_ID_FILE)) {    // 파일을 연다
            writer.write(String.valueOf(lastId)); // lastId 값을 변환 후 텍스트 파일에 씁니다
        } catch (IOException e) {
            System.err.println("마지막 ID 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 마지막 ID 파일 읽기
    public int readLastId() {
        File file = new File(LAST_ID_FILE);

        if (!file.exists()) {
            return 0; // 파일이 없으면 ID는 0부터 시작
        }

        try {
            return Integer.parseInt(new String(java.nio.file.Files.readAllBytes(file.toPath())));
        } catch (IOException | NumberFormatException e) {
            System.err.println("마지막 ID 읽기 중 오류 발생: " + e.getMessage());
            return 0; // 오류 발생 시 기본값 반환
        }
    }

}

