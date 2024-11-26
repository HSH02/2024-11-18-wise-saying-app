package wise.repository;

import wise.model.WiseSaying;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 명언을 파일 시스템에 JSON 형식으로 관리하는 Repository 클래스 (IO 사용)
 */
public class WiseSayingRepository {

    private static final String RUN_PATH = "src/main/resources/db/wiseSaying/";
    private static final String TEST_PATH = "src/test/resources/db/wiseSaying/";
    private static final String JSON_FILE_PATTERN = "^\\d+\\.json$";

    private final String filePath;
    private final String lastIdFile;
    private final File baseDirectory;
    private static int lastId;

    public WiseSayingRepository() {
        this(true);
    }

    public WiseSayingRepository(boolean isRunEnvironment) {
        this.filePath = isRunEnvironment ? RUN_PATH : TEST_PATH;
        this.lastIdFile = filePath + "lastId.txt";
        this.baseDirectory = new File(filePath);

        initializeDirectory();
        lastId = readLastID();
    }

    private void initializeDirectory() {
        if (!baseDirectory.exists() && !baseDirectory.mkdirs()) {
            throw new RuntimeException("다음에 해당하는 디렉토리 생성 실패: " + baseDirectory);
        }
    }

    private int readLastID() {
        File lastIdFile = new File(this.lastIdFile);
        if (!lastIdFile.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(lastIdFile))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException e) {
            throw new RuntimeException("마지막 ID 읽기 실패", e);
        }
    }

    public int add(String content, String author) {
        int newId = ++lastId;
        WiseSaying wiseSaying = new WiseSaying(newId, content, author);

        try {
            File file = new File(baseDirectory, newId + ".json");
            writeWiseSayingToFile(wiseSaying, file);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.lastIdFile))) {
                writer.write(String.valueOf(newId));
            }
            return newId;
        } catch (IOException e) {
            lastId--;
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    private void writeWiseSayingToFile(WiseSaying wiseSaying, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(wiseSaying.toJson());
        }
    }

    public List<WiseSaying> findAll() {
        File[] files = baseDirectory.listFiles((dir, name) -> name.matches(JSON_FILE_PATTERN));
        if (files == null) {
            throw new RuntimeException("findAll 메서드 실행 실패");
        }

        return Arrays.stream(files)
                .map(this::readWiseSaying)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(WiseSaying::getId))
                .collect(Collectors.toList());
    }

    public WiseSaying findByID(int id) {
        File file = new File(baseDirectory, id + ".json");
        return readWiseSaying(file).orElse(null);
    }

    private Optional<WiseSaying> readWiseSaying(File file) {
        if (!file.exists()) {
            return Optional.empty();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return Optional.of(WiseSaying.toJsonList(json.toString()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean delete(int id) {
        File file = new File(baseDirectory, id + ".json");
        return file.delete();
    }

    public boolean update(int id, String newContent, String newAuthor) {
        File file = new File(baseDirectory, id + ".json");
        if (!file.exists()) {
            return false;
        }

        WiseSaying updatedWiseSaying = new WiseSaying(id, newContent, newAuthor);
        try {
            writeWiseSayingToFile(updatedWiseSaying, file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("명언 수정 실패", e);
        }
    }

    public boolean build() {
        File dataFile = new File(baseDirectory, "data.json");
        List<WiseSaying> allWiseSayings = findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            writer.write("[\n");
            for (int i = 0; i < allWiseSayings.size(); i++) {
                writer.write(allWiseSayings.get(i).toJson());
                if (i < allWiseSayings.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]\n");
            return true;
        } catch (IOException e) {
            throw new RuntimeException("빌드 실패", e);
        }
    }

    public void clear() {
        if (!filePath.equals(TEST_PATH)) {
            throw new IllegalArgumentException("초기화는 테스트 환경에서만 가능합니다.");
        }

        File[] files = baseDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    throw new RuntimeException("파일 삭제 실패: " + file.getAbsolutePath());
                }
            }
        }
    }

    public int getTotalPages(int pageSize) {
        File[] files = baseDirectory.listFiles((dir, name) -> name.matches(JSON_FILE_PATTERN));
        if (files == null) {
            throw new RuntimeException("페이지 수 계산 실패");
        }

        long totalItems = files.length;
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
