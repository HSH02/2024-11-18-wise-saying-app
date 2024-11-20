package org.ll.model;


/*
* 역할 : 명언 객체(번호/명언내용/작가)
*
* 이 파일은 컨트롤러, 서비스, 리포지터티 모두에서 사용가능
*/
public class WiseSaying {
    private final int id;         // 문장 번호
    private String wiseSaying;    // 명언
    private String author;        // 작가

    // 생성자
    public WiseSaying(int id, String wiseSaying, String author) {
        this.id = id;
        this.wiseSaying = wiseSaying;
        this.author = author;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getWiseSaying() {
        return wiseSaying;
    }

    public String getAuthor() {
        return author;
    }

    // Setter
    public void setWiseSaying(String wiseSaying) {
        this.wiseSaying = wiseSaying;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // toString 오버라이딩
    @Override
    public String toString() {
        return id + " / " + author + " / " + wiseSaying;
    }

}
