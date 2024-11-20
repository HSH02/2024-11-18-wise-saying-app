package org.ll.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    // JSON 문자열을 받아서 WiseSaying 객체로 변환하는 메서드
    public static WiseSaying fromJson(String json) {
        // 중괄호 제거 및
        json = json.substring(1, json.length() - 1);
        Map<String, String> jsonMap = new HashMap<>();

        for(String pair : json.split(",")) {
            String[] keyValues = pair.split(":");
            jsonMap.put(keyValues[0].replace("\"", ""), keyValues[1]);
        }

        int id = Integer.parseInt(jsonMap.get("id"));
        String wiseSaying =jsonMap.get("wiseSaying");
        String author =jsonMap.get("author");

        // WiseSaying 객체 생성 후 반환
        return new WiseSaying(id, wiseSaying, author);
    }

    // JSON 문자열로 변환
    public String toJson() {
        return String.format("{\"id\":%d,\"wiseSaying\":\"%s\",\"author\":\"%s\"}", id, wiseSaying, author);
    }
}
