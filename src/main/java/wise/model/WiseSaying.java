package wise.model;

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
}
