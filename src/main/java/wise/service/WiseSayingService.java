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
    private final WiseSayingRepository repository;
//    private static int lastId; // 현재 마지막 ID

    // 생성자
    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    // 테스트 경로 명언 삭제 후 마지막 ID 갱신
    public void clearTestPath() {
        repository.clear();
    }

    // 명언 추가
    public int add(String content, String author)  {
        return repository.add(content, author);
    }

    // ID로 명언 반환
    public WiseSaying findById(int id) {
        return repository.findByID(id);
    }

    // 모든 명언 목록 반환
    public List<WiseSaying> findAll(){
        return repository.findAll();
    }

    // ID로 명언 삭제
    public boolean delete(int id){
        return repository.delete(id);
    }

    // ID로 명언 수정
    public boolean update(int id, String newWiseSaying, String newAuthor)  {
        return repository.update(id, newWiseSaying, newAuthor);
    }

    // 모든 json을 합친 data.json 생성
    public boolean build() {
         return repository.build();
    }

    public int getTotalPages() {
        return repository.getTotalPages();
    }
}
