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

    // 명언 추가
    public int add(String wiseSaying, String author) throws Exception {
        WiseSaying newWiseSaying = new WiseSaying(++lastId, wiseSaying, author);

        wiseSayingRepository.add(newWiseSaying, lastId); // 파일 저장
        wiseSayingRepository.saveLastId(lastId); // 마지막 ID 파일 업데이트

        return lastId;
    }

    // ID로 명언 반환
    public WiseSaying findByID(int id) throws Exception{
        WiseSaying wiseSaying = wiseSayingRepository.findById(id);
        if(wiseSaying == null) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }
        return wiseSaying;
    }

    // 모든 명언 목록 반환
    public List<WiseSaying> findAll(){
        return wiseSayingRepository.findAll();
    }

    // ID로 명언 삭제
    public String deleteById(int id) throws Exception {
        if (!wiseSayingRepository.deleteById(id))  {
            return id + "번 명언은 존재하지 않습니다.";
        }
        return id + "번 명언이 삭제되었습니다.";
    }

    // ID로 명언 수정
    public String updateById(int id, String newWiseSaying, String newAuthor) throws Exception {
        if (!wiseSayingRepository.updateById(id, newWiseSaying, newAuthor)) {
            return id + "번 명언은 존재하지 않습니다.";
        }
        return id + "번 명언이 수정되었습니다.";
    }

    // 모든 json을 합친 data.json 생성
    public String build() throws Exception{
        boolean isSuccess = wiseSayingRepository.build();
        return isSuccess ? "data.json 파일의 내용이 갱신되었습니다." : "오류 발생";
    }

}
