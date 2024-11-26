package wise.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 명언 엔티티를 나타내는 클래스입니다.
 * 각 명언은 고유 번호(id), 내용(content), 작가(author)를 포함합니다.
 *
 */
public class WiseSaying {
    private int id;         // 문장 고유 번호
    private String content; // 명언 내용
    private String author;  // 명언 작가

    /**
     * WiseSaying 객체를 생성합니다.
     *
     * @param id       문장 고유 번호
     * @param content  명언 내용
     * @param author   명언 작가
     */
    public WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    /**
     * 고유 번호를 반환합니다.
     *
     * @return 고유 번호
     */
    public int getId() {
        return id;
    }

    /**
     * 명언 내용을 반환합니다.
     *
     * @return 명언 내용
     */
    public String getContent() {
        return content;
    }

    /**
     * 명언 내용을 설정합니다.
     *
     * @param content 새로운 명언 내용
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 명언 작가를 반환합니다.
     *
     * @return 명언 작가
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 명언 작가를 설정합니다.
     *
     * @param author 새로운 명언 작가
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * WiseSaying 객체를 JSON 문자열로 반환*
     * @return  JSON 문자열
     */
    public String toJson() {
        return  "{\n" +
                "  \"id\": " + getId() + ",\n" +
                "  \"wiseSaying\": \"" + getContent() + "\",\n" +
                "  \"author\": \"" + getAuthor() + "\"\n" +
                "}";
    }

    /**
     * JSON 문자열을 WiseSaying 객체로 파싱
     * @param json - JSON 문자열
     * @return WiseSaying 객체
     */
    public static WiseSaying toJsonList(String json) {
        json = json.trim().replaceAll("^[{]|[}]$", ""); // JSON 문자열의 중괄호와 양쪽 공백 제거
        Map<String, String> values = new HashMap<>();

        for (String pair : json.split(",")) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim().replaceAll("\"", "");
            String value = keyValue[1].trim().replaceAll("\"", "");
            values.put(key, value);
        }

        return new WiseSaying(
                Integer.parseInt(values.get("id")),
                values.get("wiseSaying"),
                values.get("author")
        );
    }
}
