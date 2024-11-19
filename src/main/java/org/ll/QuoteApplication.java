package org.ll;

import org.ll.controller.QuoteController;
import org.ll.repository.QuoteRepository;
import org.ll.service.QuoteService;

public class QuoteApplication {

    public static void main(String[] args) {
        QuoteRepository quoteRepository = new QuoteRepository();
        QuoteService quoteService = new QuoteService(quoteRepository);
        QuoteController quoteController = new QuoteController(quoteService);

        quoteController.run();
    }
}

