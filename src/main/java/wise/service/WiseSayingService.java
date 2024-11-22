package wise.service;

import wise.model.WiseSaying;
import wise.repository.WiseSayingRepository;

import java.util.List;

/*
 * 역할 : 순수 비지니스 로직
 * 스캐너 사용금지, 출력 금지
 */
public class WiseSayingService {

    // Repository 객체
    private final WiseSayingRepository wiseSayingRepository;
    private static int lastId; // 현재 마지막 ID

    // 생성자
    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
        lastId = wiseSayingRepository.readLastID(); // 마지막 ID 파일 읽기
    }

    // 테스트 경로 명언 삭제 후 마지막 ID 갱신
    public void clearTestPath() {
        wiseSayingRepository.clear();
        lastId = wiseSayingRepository.readLastID(); // ID 재설정
    }

    // 명언 추가
    public int add(String wiseSaying, String author)  {
        WiseSaying newWiseSaying = new WiseSaying(++lastId, wiseSaying, author);

        wiseSayingRepository.add(newWiseSaying, lastId); // 파일 저장
        wiseSayingRepository.saveLastID(lastId); // 마지막 ID 파일 업데이트

        return lastId;
    }

    // ID로 명언 반환
    public WiseSaying findByID(int id) {
        WiseSaying wiseSaying = wiseSayingRepository.findByID(id);
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
    public boolean deleteById(int id) throws Exception {
        return wiseSayingRepository.deleteByID(id);
    }

    // ID로 명언 수정
    public boolean updateById(int id, String newWiseSaying, String newAuthor)  {
        return wiseSayingRepository.updateByID(id, newWiseSaying, newAuthor);
    }

    // 모든 json을 합친 data.json 생성
    public String build() {
        boolean isSuccess = wiseSayingRepository.build();
        return isSuccess ? "data.json 파일의 내용이 갱신되었습니다." : "오류 발생";
    }


}
