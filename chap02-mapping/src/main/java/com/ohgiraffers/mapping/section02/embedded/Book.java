package com.ohgiraffers.mapping.section02.embedded;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_book")
public class Book {

    @Id
    @Column(name = "book_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookNo;
    @Column(name = "book_title")
    private String bookTitle;
    @Column(name = "author")
    private String author;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "published_date")
    private LocalDate publishedDate;
    @Embedded
    private Price price;

    /**
     * Price는 값 타입(Embeddable 클래스)으로, Book 엔티티에 내장된다. - Price 클래스에 정의된 필드들이 Book 테이블에 칼럼으로 함께 포함된다.
     * - 별도의 테이블로 분리되지 않는다.
     */

    protected Book() {
    }

    public Book(
        String bookTitle, String author, String publisher,
        LocalDate publishedDate, Price price
    ) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
            "bookNo=" + bookNo +
            ", bookTitle='" + bookTitle + '\'' +
            ", author='" + author + '\'' +
            ", publisher='" + publisher + '\'' +
            ", publishedDate=" + publishedDate +
            ", price=" + price +
            '}';
    }
}