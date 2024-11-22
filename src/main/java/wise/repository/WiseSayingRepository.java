package wise.repository;

import wise.model.WiseSaying;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WiseSayingRepository {
    private final String filePath; // JSON 파일 위치
    private final String lastIdFile; // 마지막 ID 저장 파일
    private final String RUN_FILE_PATH = "src/main/resources/db/wiseSaying/";
    private final String TEST_FILE_PATH = "src/test/resources/db/wiseSaying/";

    // 기본 생성자 (프로덕션 환경 경로 설정)
    public WiseSayingRepository() {
        this(true);
    }

    // 경로를 주입받는 생성자
    public WiseSayingRepository(boolean isRunEnvironment) {
        String filePath = isRunEnvironment ? RUN_FILE_PATH : TEST_FILE_PATH;
        this.filePath = filePath;
        this.lastIdFile = filePath + "lastId.txt";
    }

    // 명언을 파일로 저장, 있으면 덮어쓰기
    public void add(WiseSaying wiseSaying, int newId) {
        String fileName = getFileName(newId + "");
        File directory = new File(filePath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if(!created) {
                throw new RuntimeException("디렉토리 생성 실패: " + filePath);
            }
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(toJSON(wiseSaying));  // WiseSaying 객체를 JSON 문자열로 변환하여 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생:\n " + e.getMessage(), e);
        }
    }

    // 모든 명언을 반환
    public List<WiseSaying> findAll() {
        List<WiseSaying> wiseSayings = new ArrayList<>();

        File directory = new File(filePath);
        if (!directory.exists()) {
            return wiseSayings;
        }

        File[] files = directory.listFiles((dir, name) -> name.matches("^\\d+\\.json$"));
        if (files == null) {
            return wiseSayings;
        }

        for (File file : files) {
            WiseSaying wiseSaying = readWiseFromJSON(file);
            wiseSayings.add(wiseSaying);
        }

        return wiseSayings;
    }

    // ID로 명언 조회
    public WiseSaying findByID(int id) {
        String fileName = getFileName(id + "");
        File file = new File(fileName);

        if (!file.exists()) {
            throw new RuntimeException("해당 파일은 존재하지 않습니다: " + fileName);
        }
        return readWiseFromJSON(file);
    }

    // ID로 명언 삭제
    public boolean deleteByID(int id) throws Exception {
        String fileName = getFileName(id + "");
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("저장된 파일이 없습니다.\n 입력된 파일명: " + fileName);
        }

        try {
            return file.delete();
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 예기치 못한 오류 발생: " + e.getMessage(), e);
        }

    }

    // 명언 수정
    public boolean updateByID(int id, String newWiseSaying, String newAuthor) {
        String fileName = getFileName(id + "");
        File file = new File(fileName);

        if (!file.exists()) {
            return false;
        }

        WiseSaying existingWiseSaying = readWiseFromJSON(file);
        existingWiseSaying.setWiseSaying(newWiseSaying);
        existingWiseSaying.setAuthor(newAuthor);

        return writeWiseToJSON(file, existingWiseSaying);
    }

    // data.json 생성
    public boolean build() {
        List<WiseSaying> list = findAll();
        String fileName = getFileName("data");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            writer.write(toJSON(list));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("빌드 파일 쓰기 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 마지막 ID 저장
    public void saveLastID(int lastId) {
        try (FileWriter writer = new FileWriter(lastIdFile)) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: \n" + e.getMessage(), e);
        }
    }

    // 마지막 ID 파일 읽기
    public int readLastID() {
        File file = new File(lastIdFile);

        if (!file.exists()) {
            return 0;
        }

        try {
            return Integer.parseInt(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            throw new RuntimeException("마지막 ID 읽기 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // WiseSaying 객체를 JSON 문자열로 변환
    private String toJSON(WiseSaying wiseSaying) {
        return "{\n" +
                "   \"id\": " + wiseSaying.getId() + ",\n" +
                "   \"wiseSaying\": \"" + wiseSaying.getWiseSaying() + "\",\n" +
                "   \"author\": \"" + wiseSaying.getAuthor() + "\"\n" +
                "}";
    }

    private String toJSON(List<WiseSaying> list) {
        StringBuilder jsonBuilder = new StringBuilder("[\n");

        for (int i = 0; i < list.size(); i++) {
            jsonBuilder.append(toJSON(list.get(i)));

            if (i < list.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }

        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    private WiseSaying JSONToString(String json) {
        json = json.replaceAll("^\\{|\\}$", "").trim();

        Map<String, String> jsonMap = new HashMap<>();

        for (String pair : json.split(",")) {
            String[] keyValues = pair.split(":");
            String key = keyValues[0].replace("\"", "").trim();
            String value = keyValues[1].replace("\"", "").trim();
            jsonMap.put(key, value);
        }

        int id = Integer.parseInt(jsonMap.get("id"));
        String wiseSaying = jsonMap.get("wiseSaying");
        String author = jsonMap.get("author");

        return new WiseSaying(id, wiseSaying, author);
    }

    // JSON 파일에서 WiseSaying 객체 읽기
    private WiseSaying readWiseFromJSON(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return JSONToString(jsonContent.toString().trim());
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 중 오류 발생: \n" + e.getMessage(), e);
        }
    }

    // 파일에 WiseSaying 객체 저장
    private boolean writeWiseToJSON(File file, WiseSaying wiseSaying) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(toJSON(wiseSaying));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일 저장 중 오류 발생" + e.getMessage(), e);
        }
    }

    // ID에 해당하는 파일 경로 얻기
    private String getFileName(String id){
        return filePath + id + ".json";
    }

    // 테스트 용 코드
    public void clear() {
        try {
            File folder = new File(TEST_FILE_PATH);

            if (folder.exists()) {
                deleteRecursion(folder);
            }

        } catch (Exception e) {
            throw new RuntimeException("파일 초기화 중 오류 발생", e);
        }
    }

    private void deleteRecursion(File file) throws Exception{
        // 디렉토리 여부 확인
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteRecursion(f);
                }
            }
        }

        // 파일 또는 빈 디렉토리 삭제
        if (!file.delete()) {
            throw new Exception("파일 삭제 실패: " + file.getAbsolutePath());
        }
    }
}
