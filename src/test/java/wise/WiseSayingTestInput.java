package wise;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingTestInput {

    public static final String INPUT_ADD = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .add("Wise 3", "Author 3")
            .create()
            .generateString();

    public static final String INPUT_FIND_ALL = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .add("Wise 3", "Author 3")
            .list()
            .create()
            .generateString();

    public static final String INPUT_FIND_KEYWORD = TestScenarioInput.builder()
            .add("A","A")
            .add("B","B")
            .add("C","A")
            .list("author","A")
            .create()
            .generateString();

    public static final String INPUT_DELETE_SUCCESS = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .list()
            .delete(1)
            .create()
            .generateString();

    public static final String INPUT_DELETE_FAIL_NULL_ID = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .list()
            .delete(1)
            .delete(1)
            .create()
            .generateString();


    public static final String INPUT_UPDATE_SUCCESS = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .list()
            .delete(1)
            .delete(1)
            .update(1, "", "")
            .update(3, "", "")
            .update(2, "updated Wise 2", "updated Author 2")
            .list()
            .create()
            .generateString();

    public static final String INPUT_BUILD = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .add("Wise 3", "Author 3")
            .list()
            .delete(1)
            .delete(1)
            .update(2, "updated Wise 2", "updated Author 2")
            .list()
            .build()
            .create()
            .generateString();

    public static final String INPUT_PAGING_DATA = TestScenarioInput.builder()
            .add("명언 1", "작자미상 1")
            .add("명언 2", "작자미상 2")
            .add("명언 3", "작자미상 3")
            .add("명언 4", "작자미상 4")
            .add("명언 5", "작자미상 5")
            .add("명언 6", "작자미상 6")
            .add("명언 7", "작자미상 7")
            .add("명언 8", "작자미상 8")
            .add("명언 9", "작자미상 9")
            .add("명언 10", "작자미상 10")
            .list() // 기본 페이지 목록 조회
            .list("2") // 2페이지 목록 조회
            .build()
            .create()
            .generateString();


    // 테스트 시나리오를 위한 입력 생성 메서드
    public static class TestScenarioInput {
        private final List<String> commands;

        private TestScenarioInput(List<String> commands) {
            this.commands = commands;
        }

        public String generateString() {
            StringBuilder sb = new StringBuilder();
            for (String command : commands) {
                sb.append(command).append("\n");
            }
            sb.append("종료\n");
            return sb.toString();
        }

        public static ScenarioBuilder builder() {
            return new ScenarioBuilder();
        }

        // 입력 생성을 위한 빌더 클래스
        public static class ScenarioBuilder {
            private final List<String> commands = new ArrayList<>();

            public TestScenarioInput create() {
                return new TestScenarioInput(commands);
            }

            public ScenarioBuilder add(String content, String author) {
                commands.add("등록");
                commands.add(content);
                commands.add(author);
                return this;
            }

            public ScenarioBuilder list() {
                commands.add("목록");
                return this;
            }

            public ScenarioBuilder list(String page, String keywordType, String keyword) {
                commands.add("목록?keywordType=" + keywordType + "&keyword=" + keyword + "&page=" + page);
                return this;
            }

            public ScenarioBuilder list(String keywordType, String keyword) {
                commands.add("목록?keywordType=" + keywordType + "&keyword=" + keyword);
                return this;
            }

            public ScenarioBuilder list(String page) {
                commands.add("목록?page=" + page);
                return this;
            }

            public ScenarioBuilder delete(int id) {
                commands.add("삭제?id=" + id);
                return this;
            }

            public ScenarioBuilder update(int id, String newContent, String newAuthor) {
                commands.add("수정?id=" + id);
                commands.add(newContent);
                commands.add(newAuthor);
                return this;
            }

            public ScenarioBuilder build() {
                commands.add("빌드");
                return this;
            }
        }
    }

}