package org.ll.entity;

public class Quote {
    private final int id;         // 문장 번호
    private String wiseSaying;    // 명언
    private String author;        // 작가

    // 생성자
    public Quote(int id, String wiseSaying, String author) {
        this.id = id;
        this.wiseSaying = wiseSaying;
        this.author = author;
    }

    // Getter and Setter
    public int getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setWiseSaying(String wiseSaying) {
        this.wiseSaying = wiseSaying;
    }
    public String getWiseSaying() {
        return wiseSaying;
    }

    // toString 오버라이딩
    @Override
    public String toString() {
        return id + " / " + author + " / " + wiseSaying;
    }
}
