package org.ll.service;

import org.ll.model.WiseSaying;
import org.ll.repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {

    private int lastId; // 최근 문장 번호
    private final WiseSayingRepository wiseSayingRepository;

    private WiseSayingRepository repository = new WiseSayingRepository();

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;

        // 마지막 ID 파일 읽기
        lastId = wiseSayingRepository.readLastId();
    }

    // 프로그램의 시작 메시지를 반환하는 메서드
    public String start(){
        // 디렉토리가 존재하면 "프로그램 다시 시작..." 메시지 반환, 아니면 "== 명언 앱 ==" 메시지 반환
        boolean fileExist = wiseSayingRepository.fileDirectory();
        String content = fileExist ? "프로그램 다시 시작..." : "== 명언 앱 ==";

        return content;
    }

    // 명언 추가
    public void addWiseSaying(String wiseSaying, String author) {
        // 새로운 ID 생성
        int newId = ++lastId;

        WiseSaying newWiseSaying = new WiseSaying(newId, wiseSaying, author);

        // 파일 저장
        wiseSayingRepository.saveWiseSayingToFile(newWiseSaying, newId);

        // 마지막 ID 파일 업데이트
        wiseSayingRepository.saveLastIdToFile(lastId);

        System.out.println(newId + "번 명언이 등록되었습니다.");
    }

    public WiseSaying getQuoteById(int id) {
        WiseSaying wiseSaying = wiseSayingRepository.getQuoteById(id);

        if(wiseSaying == null) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        return wiseSaying;
    }

    public List<WiseSaying> getAllWiseSaying() {
        return wiseSayingRepository.gelAllWiseSaying();
    }

    // ID와 일치하는 명언 삭제
    public void removeItemByTitle(int id){
        boolean isRemoved = wiseSayingRepository.removeItemByTitle(id); // 삭제 여부

        if(!isRemoved) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        System.out.println(id + "번 명언이 삭제되었습니다.");
    }

    public void update(int id , String newWiseSaying, String newAuthor){
        boolean isUpdated = wiseSayingRepository.updateQuote(id, newWiseSaying, newAuthor);

        if(!isUpdated) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        System.out.println(id + "번 명언이 수정되었습니다.");
    }


}
