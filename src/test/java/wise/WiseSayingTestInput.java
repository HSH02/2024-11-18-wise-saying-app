package wise;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingTestInput {

    // 성공 시나리오들
    public static final String INPUT_ADD = TestScenarioInput.builder()
            .add("현재를 사랑하라.", "작자미상")
            .create()
            .generateInput();

    public static final String INPUT_FIND_ALL = TestScenarioInput.builder()
            .add("Wise 1", "Author 1")
            .add("Wise 2", "Author 2")
            .list()
            .create()
            .generateInput();

    public static final String INPUT_DELETE_SUCCESS = TestScenarioInput.builder()
            .add("Wise", "Author")
            .delete(1)
            .create()
            .generateInput();

    public static final String INPUT_UPDATE_SUCCESS = TestScenarioInput.builder()
            .add("Before Wise", "Before Author")
            .update(1, "After Wise", "After Author")
            .create()
            .generateInput();

    public static final String INPUT_BUILD = TestScenarioInput.builder()
            .build()
            .create()
            .generateInput();


    // 테스트 시나리오를 위한 입력 생성 메서드
    public static class TestScenarioInput {
        private final List<String> commands;

        private TestScenarioInput(List<String> commands) {
            this.commands = commands;
        }

        public String generateInput() {
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

            public ScenarioBuilder add(String wiseSaying, String author) {
                commands.add("등록");
                commands.add(wiseSaying);
                commands.add(author);
                return this;
            }

            public ScenarioBuilder list() {
                commands.add("목록");
                return this;
            }

            public ScenarioBuilder delete(int id) {
                commands.add("삭제?id=" + id);
                return this;
            }

            public ScenarioBuilder update(int id, String newWiseSaying, String newAuthor) {
                commands.add("수정?id=" + id);
                commands.add(newWiseSaying);
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