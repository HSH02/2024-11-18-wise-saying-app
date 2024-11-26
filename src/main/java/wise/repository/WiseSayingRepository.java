    package wise.repository;

    import wise.model.WiseSaying;

    import java.io.*;
    import java.nio.file.*;
    import java.util.*;
    import java.util.stream.Collectors;
    import java.util.stream.Stream;

    /**
     * 명언을 파일 시스템에 JSON 형식으로 관리하는 Repository 클래스
     */
    public class WiseSayingRepository {

        // 파일 경로 관련 상수
        private static final String RUN_PATH = "src/main/resources/db/wiseSaying/";
        private static final String TEST_PATH = "src/test/resources/db/wiseSaying/";
        private static final String JSON_FILE_PATTERN = "^\\d+\\.json$";            // 정규식

        private final String filePath;          // 실제 사용할 파일 경로
        private final String lastIdFile;        // 마지막 ID를 저장할 파일 경로
        private final Path baseDirectory;       // 기본 디텍토리 경로
        private static int lastId;              // 현재 사용중인 마지막 경로

        /**
         * 기본 생성자 - 운영 환경용 리포지터리 생성
         */
        public WiseSayingRepository() {
            this(true);
        }


        /**
         * 환경 설정이 가능한 생성자
         * @param isRunEnvironment true/운영 & false/테스트
         */
        public WiseSayingRepository(boolean isRunEnvironment) {
            this.filePath = isRunEnvironment ? RUN_PATH : TEST_PATH;
            this.lastIdFile = filePath + "lastId.txt";
            this.baseDirectory = Paths.get(filePath);

            initializeDirectory();
            lastId = readLastID(); // 마지막 ID 파일 읽기
        }

        /**
         * 저장소 디텍토리 초기화 - 없으면 생성
         */
        private void initializeDirectory() {
            try {
                Files.createDirectories(baseDirectory); // 지정한 경로에 디렉토리가 존재하지 않으면 새로 생성합니다.
            } catch (IOException e) {
                throw new RuntimeException("다음에 해당하는 디텍토리 생성 실패: " + baseDirectory, e);
            }
        }

        /**
         * 마지막으로 사용된 ID를 파일에서 읽어옴
         * @return 마지막으로 사용된 ID, 파일이 없으면 0
         */
        private int readLastID() {
            Path lastIdPath = Paths.get(lastIdFile);
            if (!Files.exists(lastIdPath)) {
                return 0;
            }
            try {
                return Integer.parseInt(Files.readString(lastIdPath).trim());
            } catch (IOException e) {
                throw new RuntimeException("마지막 ID 읽기 실패", e);
            }
        }

        /**
         * 새로운 명언 추가
         * @param content 명언 내용
         * @param author 명언 작성자
         * @return 새로 작성된 명언의 ID
         */
        public int add(String content, String author)  {
            int newId = ++lastId;
            WiseSaying wiseSaying = new WiseSaying(newId, content, author);

            try {
                Path filePath = baseDirectory.resolve(newId + ".json");   // id.json 경로 생성
                writeWiseSayingToFile(wiseSaying, filePath);                     // 파일 저장
                Files.writeString(Paths.get(lastIdFile), String.valueOf(newId)); // 새 id 값을 lastIdFile 경로에 문자열로 저장
                return newId;
            } catch (IOException e) {
                lastId--; // 실패시 롤백
                throw new RuntimeException("파일 저장 실패", e);
            }
        }

        /**
         * WiseSaying 객체를 JSON 형식으로 파일에 저장
         * @param wiseSaying    저장할 객체
         * @param path          저장할 경로
         */
        private void writeWiseSayingToFile(WiseSaying wiseSaying, Path path) throws IOException {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) { // Files.newBufferedWriter(path)) -  지정한 경로에 대해 BufferedWriter 생성
                writer.write((wiseSaying.toJson()));
            }
        }

        /**
         *  경로의 모든 명언 조회
         * @return ID를 오름차순으로 정렬한 모든 명언 목록
         */
        public List<WiseSaying> findAll() {
            try(Stream<Path> paths = Files.list(baseDirectory)){ // 경로에 있는 모든 파일을 스트림 형태로
                return   paths
                        .filter(path -> path.getFileName().toString().matches(JSON_FILE_PATTERN))   // 정규 표현식 검사
                        .map(this::readWiseSaying)                                                  // 파일 가공 및 반환
                        .filter(Optional::isPresent)                                                // 객체 값 보유 반환
                        .map(Optional::get)                                                         // Optional 안의 값 반환
                        .sorted(Comparator.comparing(WiseSaying::getId))                            // 오름차순 정렬
                        .collect(Collectors.toList());                                              // 리스트 형변환, 반환
            } catch (IOException e) {
                throw new RuntimeException("findAll 메서드 실행 실패" , e);
            }
        }

        /**
         * ID로 명언 조회
         * @param id 찾을 ID
         * @return wiseSaying 객체
         */
        public WiseSaying findByID(int id) {
            Path filePath = baseDirectory.resolve(id + ".json");        // id.json 경로 생성
            return readWiseSaying(filePath).orElse(null);
        }

        /**
         * 파일에서 명언 찾기
         * @param path 검색할 파일 경로
         * @return Optional<WiseSaying> 객체 - 파일이 없거나 읽기에 실패하면 empty
         */
        private Optional<WiseSaying> readWiseSaying(Path path) {
            if (!Files.exists(path)) {
                return Optional.empty();
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) { // Files.newBufferedWriter(path)) -  지정한 경로에 대해 BufferedWriter를 생성
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                return Optional.of(WiseSaying.toJsonList(json.toString())); // wiseSaying 객체를 of 로 감아 반환
            } catch (IOException e) {
                return Optional.empty();
            }
        }

        /**
         * 명언 삭제
         * @param id 삭제할 명언의 ID
         * @return 삭제 성공 여부
         */
        public boolean delete(int id) {
            try {
                Path filePath = baseDirectory.resolve(id + ".json"); // id.json 경로 생성
                return Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("명언 삭제 실패", e);
            }
        }

        /**
         * 명언 수정
         * @param id 수정할 명언의 ID
         * @param newContent 새로운 명언의 내용
         * @param newAuthor 새로운 작성자
         * @return 수정 성공 여부
         */
        public boolean update(int id, String newContent, String newAuthor) {
            Path filePath = baseDirectory.resolve(id + ".json"); // id.json 경로 생성
            if (!Files.exists(filePath)) {
                return false;
            }

            WiseSaying updatedWiseSaying = new WiseSaying(id, newContent, newAuthor);
            try {
                writeWiseSayingToFile(updatedWiseSaying, filePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("명언 수정 실패", e);
            }
        }

        /**
         * 모든 명언을 하나의 JSON 파일로 빌드
         * @return 빌드 성공 여부
         */
        public boolean build() {
            try {
                Path dataFile = baseDirectory.resolve("data.json"); // data.json 경로 생성
                List<WiseSaying> allWiseSayings = findAll();

                try(BufferedWriter writer = Files.newBufferedWriter(dataFile)) {
                    writer.write("[\n");
                    for (int i = 0; i < allWiseSayings.size(); i++) {
                        writer.write((allWiseSayings.get(i).toJson()));
                        if (i < allWiseSayings.size()- 1 ){
                            writer.write(",\n");
                        }
                    }
                    writer.write("\n]\n");
                }
                return true;
            } catch (IOException e){
                throw new RuntimeException("빌드 실패", e);
            }
        }

        /**
         * 테스트 환경에서만 사용 가능한 저장소 초기화
         * 모든 파일 삭제
         */
        public void clear() {
            if(!filePath.equals(TEST_PATH)){
                throw new IllegalArgumentException("초기화는 테스트 환경에서만 가능합니다.");
            }

            try (Stream<Path> paths = Files.walk(baseDirectory)) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException("파일 삭제에 실패했습니다." + path, e);
                            }
                        });
            } catch (Exception e) {
                throw new RuntimeException("파일 초기화 중 오류 발생", e);
            }
        }

        /**
         * 전체 페이지 수 계산
         * @return 총 페이지 수
         */
        public int getTotalPages(int pageSize) {
            try(Stream<Path> paths = Files.list(baseDirectory)) {
                long totalItems = paths // count 반환 타입 및 Stream 요소 개수로 long 선언
                                        .filter(path -> path.getFileName().toString().matches(JSON_FILE_PATTERN))
                                        .count();
                        return (int) Math.ceil((double)(totalItems / pageSize));
            } catch (IOException e) {
                throw new RuntimeException("페이지 수 계산 실패", e);
            }
        }


    //    public List<WiseSaying> findByKeyWord(int page, String keywordType, String keyword) {
    //        List<WiseSaying> filteredWiseSayings  = new ArrayList<>();
    //
    //        File directory = new File(filePath);
    //        if (!directory.exists()) {
    //            return filteredWiseSayings ;
    //        }
    //
    //        File[] files = directory.listFiles((dir, name) -> name.matches("^\\d+\\.json$"));
    //        if (files == null) {
    //            return filteredWiseSayings ;
    //        }
    //
    //        // 파일들을 순차적으로 처리
    //        for (File file : files) {
    //            WiseSaying wiseSaying = readWiseFromJSON(file);
    //            if(keywords.isEmpty() || matchesFilters(wiseSaying, keywords)){
    //                filteredWiseSayings .add(wiseSaying);
    //            }
    //        }
    //
    //        // 내림차순 정렬
    //        filteredWiseSayings.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
    //
    //        if(filteredWiseSayings.isEmpty()) return filteredWiseSayings;
    //
    //        // 페이징
    //        int page = 1;
    //        if(keywords.containsKey("page")){
    //            page = Integer.parseInt(keywords.get("page"));
    //        }
    //
    //        int totalItems = filteredWiseSayings.size();
    //        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
    //
    //        if(page < 1) page = 1;
    //        if(page > totalPages) page = totalPages;
    //
    //        int startIndex = (page - 1) * PAGE_SIZE;
    //        int endIndex = Math.min(startIndex + PAGE_SIZE, totalItems);
    //
    //        return filteredWiseSayings.subList(startIndex, endIndex);
    //    }
    }
