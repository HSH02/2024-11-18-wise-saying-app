package org.ll;

import org.ll.controller.WiseSayingController;
import org.ll.repository.WiseSayingRepository;
import org.ll.service.WiseSayingService;

public class WiseSayingApplication {

    public static void main(String[] args) {
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
        WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService);

        wiseSayingController.run();
    }
}

