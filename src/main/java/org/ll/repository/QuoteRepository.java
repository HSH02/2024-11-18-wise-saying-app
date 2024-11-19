package org.ll.repository;

import org.ll.entity.Quote;
import java.util.*;

public class QuoteRepository {
    List<Quote> quotes = new ArrayList<>(); // 명령 저장 리스트

    // 명언 추가
    public void addQuote(Quote quote){
        quotes.add(quote);
    }

    // 명언 전부 반환
    public List<Quote> gelAllQuotes(){
        return quotes;
    }

    // ID와 일치하는 명언 조회
    public Quote getQuoteById(int id){
        for(Quote quote : quotes) {
            if(quote.getId() == id){
                return quote;
            }
        }

        return null;
    }

    // ID와 일치하는 명언 삭제
    public boolean removeQuote(int id){
        return quotes.removeIf(quote -> quote.getId() == id);
    }

    // ID와 일치하는 명언 수정
    public boolean updateQuote(int id , String newWiseSaying, String newAuthor){
        for (Quote quote : quotes) {
            if (quote.getId() == id) {
                quote.setWiseSaying(newWiseSaying);  // 명언 수정
                quote.setAuthor(newAuthor);          // 작가 수정
                return true;                         // 수정 성공
            }
        }

        return false;   // 수정 실패
    }

}
