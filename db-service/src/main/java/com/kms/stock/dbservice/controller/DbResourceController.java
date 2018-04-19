package com.kms.stock.dbservice.controller;

import com.kms.stock.dbservice.model.Quote;
import com.kms.stock.dbservice.repository.QuoteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/db")
public class DbResourceController {
    private QuoteRepository quoteRepository;

    public DbResourceController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("")
    public List<String> getListQuotes() {
        return quoteRepository.findAll()
                .stream()
                .map(quote -> quote.getQuote())
                .collect(Collectors.toList());
    }

    @GetMapping("/{userName}")
    public List<String> getQuotes(@PathVariable("userName") final String userName) {
        return quoteRepository.findByUserName(userName)
                .stream()
                .map(quote -> quote.getQuote())
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public Quote addQuote(@RequestBody final Quote quote) {
        quoteRepository.save(quote);
        return quote;
    }

    @PostMapping("/delete/{userName}")
    public List<Quote> deleteQuote(@PathVariable("userName") final String userName) {
        List<Quote> quotes = quoteRepository.findByUserName(userName);
        quoteRepository.deleteInBatch(quotes);
        return quotes;
    }
}
