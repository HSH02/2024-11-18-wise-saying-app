package org.ll.repository;

import org.ll.model.WiseSaying;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
 * 역할 : 데이터의 조회/수정/삭제/생성을 담당
 * 스캐너 사용금지, 출력 금지
 */
public class WiseSayingRepository {
    private static final String FILE_DIRECTORY = "db/wiseSaying/"; // JSON 파일 위치
    private static final String LAST_ID_FILE = FILE_DIRECTORY + "lastId.txt"; // 마지막 ID 저장 파일

    // 디렉토리 존재 여부 확인
    public boolean isFileDirectoryExists() {
        return new File(FILE_DIRECTORY).exists();
    }

    // 명언을 파일로 저장, 있으면 덮어쓰기
    public void saveWiseSaying(WiseSaying wiseSaying, int newId) throws IOException {
        String fileName = getFileName(newId);

        File directory = new File(FILE_DIRECTORY);

        // 디렉토리가 존재하지 않으면 생성
        if (!directory.exists()) {
            boolean created = directory.mkdirs();

            // 디텍터리 생성 결과
            if(!created) {
                throw new RuntimeException("디렉토리 생성 실패: " + FILE_DIRECTORY);
            }
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(toJson(wiseSaying));  // WiseSaying 객체를 JSON 문자열로 변환하여 저장
        } catch (IOException e) {
            throw new IOException("파일 저장 중 오류 발생:\n " + e.getMessage(), e);
        }
    }

    // 모든 명언을 반환
    public List<WiseSaying> getAllWiseSayings() throws IOException {
        List<WiseSaying> wiseSayings = new ArrayList<>();

        File directory = new File(FILE_DIRECTORY);
        if (!directory.exists()) {
            throw new IOException("디렉토리가 존재하지 않습니다.");
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            throw new IOException("저장된 파일이 없습니다.");
        }

        for (File file : files) {
            WiseSaying wiseSaying = readWiseSayingFromJson(file);
            wiseSayings.add(wiseSaying);
        }

        return wiseSayings;
    }

    // ID로 명언 조회
    public WiseSaying getWiseSayingById(int id) throws IOException {
        String fileName = getFileName(id);
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("저장된 파일이 없습니다.");
        }

        return readWiseSayingFromJson(file);
    }

    // ID로 명언 삭제
    public boolean removeItemById(int id) throws IOException {
        String fileName = getFileName(id);
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("저장된 파일이 없습니다. 파일명: " + fileName);
        }

        try {
           return file.delete();
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 예기치 못한 오류 발생: " + e.getMessage(), e);
        }

    }

    // 명언 수정
    public boolean updateWiseSaying(int id, String newWiseSaying, String newAuthor) throws IOException {
        String fileName = getFileName(id);
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("저장된 파일이 없습니다.");
        }

        WiseSaying existingWiseSaying = readWiseSayingFromJson(file);

        existingWiseSaying.setWiseSaying(newWiseSaying);
        existingWiseSaying.setAuthor(newAuthor);

        return writeWiseSayingToJson(file, existingWiseSaying);
    }

    // 마지막 ID 저장
    public void saveLastId(int lastId) {
        try (FileWriter writer = new FileWriter(LAST_ID_FILE)) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: \n" + e.getMessage(), e);
        }
    }

    // 마지막 ID 파일 읽기
    public int readLastId() {
        File file = new File(LAST_ID_FILE);

        if (!file.exists()) {
            return 0;
        }

        try {
            return Integer.parseInt(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException | NumberFormatException e) {
            System.err.println("마지막 ID 읽기 중 오류 발생: " + e.getMessage());
            return 0;
        }
    }

    // WiseSaying 객체를 JSON 문자열로 변환
    private String toJson(WiseSaying wiseSaying) {
        return "{\n" +
                "  \"id\": " + wiseSaying.getId() + ",\n" +
                "  \"wiseSaying\": \"" + wiseSaying.getWiseSaying() + "\",\n" +
                "  \"author\": \"" + wiseSaying.getAuthor() + "\"\n" +
                "}";
    }

    // JSON 문자열을 WiseSaying 객체로 변환
    private WiseSaying jsonToString(String json) {
        json = json.substring(1, json.length() - 1);  // 앞뒤 중괄호 제거
        Map<String, String> jsonMap = new HashMap<>();

        for (String pair : json.split(",")) {
            String[] keyValues = pair.split(":");
            jsonMap.put(keyValues[0].replace("\"", "").trim(), keyValues[1].replace("\"", "").trim());
        }

        int id = Integer.parseInt(jsonMap.get("id"));
        String wiseSaying = jsonMap.get("wiseSaying");
        String author = jsonMap.get("author");

        // WiseSaying 객체 생성 후 반환
        return new WiseSaying(id, wiseSaying, author);
    }

    // JSON 파일에서 WiseSaying 객체 읽기
    private WiseSaying readWiseSayingFromJson(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return jsonToString(jsonContent.toString());
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 중 오류 발생: \n" + e.getMessage(), e);
        }
    }

    // 파일에 WiseSaying 객체 저장
    private boolean writeWiseSayingToJson(File file, WiseSaying wiseSaying) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(toJson(wiseSaying));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: \n" + e.getMessage(), e);
        }
    }

    // ID에 해당하는 파일 경로 얻기
    private String getFileName(int id) {
        return FILE_DIRECTORY + id + ".json";
    }

}
