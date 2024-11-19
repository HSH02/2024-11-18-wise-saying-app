package org.ll.service;

import org.ll.entity.Quote;
import org.ll.repository.QuoteRepository;

import java.util.List;

public class QuoteService {
    private QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository){
        this.quoteRepository = quoteRepository;
    }

    public void addQuote(String wiseSaying, String author) {
        int number = quoteRepository.gelAllQuotes().size() + 1;
        Quote newQuote = new Quote(number, wiseSaying, author);
        quoteRepository.addQuote(newQuote);

        System.out.println(number + "번 명언이 등록되었습니다.");
    }

    public Quote getQuoteById(int id) {
        Quote quote = quoteRepository.getQuoteById(id);

        if(quote == null) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        return quote;
    }

    public List<Quote> getAllQuotes() {
        return quoteRepository.gelAllQuotes();
    }

    public void removeQuote(int id){
        boolean isRemoved = quoteRepository.removeQuote(id); // 삭제 여부

        if(!isRemoved) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        System.out.println(id + "번 명언이 삭제되었습니다.");
    }

    public void update(int id , String newWiseSaying, String newAuthor){
        boolean isUpdated = quoteRepository.updateQuote(id, newWiseSaying, newAuthor);

        if(!isUpdated) {
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        }

        System.out.println(id + "번 명언이 수정되었습니다.");
    }
}
