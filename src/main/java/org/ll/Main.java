package org.ll;

import java.util.*;

class Quote {
    private final int id;         // 문장 번호
    private String wiseSaying;  // 문장
    private String author;

    public Quote(int id, String wiseSaying, String author) {
        this.id = id;
        this.wiseSaying = wiseSaying;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setWriter(String author) {
        this.author = author;
    }

    public String getWriter() {
        return author;
    }

    public void setWiseSaying(String wiseSaying) {
        this.wiseSaying = wiseSaying;
    }

    public String getWiseSaying() {
        return wiseSaying;
    }

    @Override
    public String toString() {
        return id + " / " + author + " / " + wiseSaying;
    }
}

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    public void run(){
        System.out.println("== 명언 앱 ==");

        System.out.print("명령) ");
    }
}

