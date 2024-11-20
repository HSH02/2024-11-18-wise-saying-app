package org.ll.service;

import org.ll.model.WiseSaying;
import org.ll.repository.WiseSayingRepository;

import java.io.IOException;
import java.util.List;

/*
 * 역할 : 순수 비지니스 로직
 * 스캐너 사용금지, 출력 금지
 */
public class WiseSayingService {

    // Repository 객체
    private final WiseSayingRepository wiseSayingRepository;
    private int lastId; // 현재 마지막 ID

    // 생성자
    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
        lastId = wiseSayingRepository.readLastId(); // 마지막 ID 파일 읽기
    }

    // 프로그램 시작 메시지 반환
    public String getDisplayStartMessage(){
        return wiseSayingRepository.isFileDirectoryExists() ? "프로그램 다시 시작..." : "== 명언 앱 ==";
    }

    // 명언 추가
    public int addWiseSaying(String wiseSaying, String author) throws IOException {
        WiseSaying newWiseSaying = new WiseSaying(++lastId, wiseSaying, author);

        wiseSayingRepository.saveWiseSaying(newWiseSaying, lastId); // 파일 저장
        wiseSayingRepository.saveLastId(lastId); // 마지막 ID 파일 업데이트

        return lastId;
    }

    // ID로 명언 반환
    public WiseSaying getWiseSayingById(int id) throws IOException{
        WiseSaying wiseSaying = wiseSayingRepository.getWiseSayingById(id);
        if(wiseSaying == null) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }
        return wiseSaying;
    }

    // 모든 명언 목록 반환
    public List<WiseSaying> getAllWiseSayings() throws IOException {
        return wiseSayingRepository.getAllWiseSayings();
    }

    // ID로 명언 삭제
    public String removeWiseSayingById(int id) throws IOException {
        if (!wiseSayingRepository.removeItemById(id))  {
            return id + "번 명언은 존재하지 않습니다.";
        }
        return id + "번 명언이 삭제되었습니다.";
    }

    // ID로 명언 수정
    public String updateWiseSayingById(int id, String newWiseSaying, String newAuthor) throws IOException {
        if (!wiseSayingRepository.updateWiseSaying(id, newWiseSaying, newAuthor)) {
            return id + "번 명언은 존재하지 않습니다.";
        }
        return id + "번 명언이 수정되었습니다.";
    }

}
