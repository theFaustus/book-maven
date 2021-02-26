package com.tekwill.java.fundamentals.book.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Book {
    private Long id;
    private String isbn;
    private String name;
    private boolean isRare;
    private int numberOfPages;
    private List<Page> pages = new ArrayList<>();

    public Book(Long id, String isbn, String name, boolean isRare, int numberOfPages) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.isRare = isRare;
        this.numberOfPages = numberOfPages;
    }

    public Book(String isbn, String name, boolean isRare, int numberOfPages) {
        this.isbn = isbn;
        this.name = name;
        this.isRare = isRare;
        this.numberOfPages = numberOfPages;
    }

    public void addPage(Page page) {
        pages.add(page);
        page.setBook(this);
    }

    public void removePage(Page page) {
        pages.remove(page);
        page.setBook(null);
    }

}
