package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;



interface BookRepository extends JpaRepository<Book, Long>{

    Book findByTitle(String title);

    List<Book> findByAuthor(String author);

    Book deleteByTitle(String title);
}
