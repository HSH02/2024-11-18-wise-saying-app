package wise;

import wise.repository.WiseSayingRepository;
import wise.service.WiseSayingService;

public class WiseSayingApplication {

    public static void main(String[] args) {
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
        WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService);

        wiseSayingController.run();
    }
}

