package com.kms.stock.stockservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
public class StockController {

    @Autowired
    private RestTemplate restTemplate;

    private YahooFinance yahooFinance;

    public StockController() {
        yahooFinance = new YahooFinance();
    }

    @GetMapping("/{userName}")
    public List<Stock> getStock(@PathVariable("userName") final String userName) {
       ResponseEntity<List<String>> quotesResponse = restTemplate.exchange("http://db-service/rest/db/" + userName,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>(){});
       return quotesResponse.getBody()
                                .stream()
                                .map(quote -> {
                                    return getStockQuote(quote);
                                }).collect(Collectors.toList());
    }

    private static Stock getStockQuote(String quote) {
        try {
            return YahooFinance.get(quote);
        } catch (IOException e) {
            e.printStackTrace();
            return new Stock(quote);
        }
    }
}
