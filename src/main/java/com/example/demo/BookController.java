package com.example.demo;

import com.example.demo.exceptions.BookNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
class BookController {
    private final BookRepository repository;
    private final BookModelAssembler assembler;

    public BookController(BookRepository repository, BookModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    /*Exercitiul 1*/
    @PostMapping("/library")
    ResponseEntity<?> newBook(@RequestBody Book newBook) {

        EntityModel<Book> entityModel = assembler.toModel(repository.save(newBook));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*Exercitiul 2*/
    @GetMapping("/library/books")
    CollectionModel<EntityModel<Book>> all() throws BookNotFoundException{

        List<EntityModel<Book>>books = repository.findAll().stream()
                .sorted((book1, book2) -> {
                    if(book1.getAuthor().compareTo(book2.getAuthor()) == 0){
                        if(book1.getTitle().compareTo(book2.getTitle()) > 0) return 1;
                        else if(book1.getTitle().compareTo(book2.getTitle()) < 0)return -1;
                        else return 0;
                    }
                    else if(book1.getAuthor().compareTo(book2.getAuthor()) > 0) return 1;
                    else return -1;
                })
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(books, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    /*Exercitiul 3*/
    @Transactional
    @DeleteMapping("/library/deleteBook/{title}")
    ResponseEntity<?> deleteByTitle(@PathVariable String title) {

        repository.deleteByTitle(title);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/library/books/{id}")
    EntityModel<Book> one(@PathVariable Long id) {

        Book book = repository.findById(id) //
                .orElseThrow(() -> new BookNotFoundException(id));

        return assembler.toModel(book);
    }

    /*EXERCITIUL 4*/
    @GetMapping("/library/findBook/{title}")
    EntityModel<Book> findBook(@PathVariable String title) throws BookNotFoundException{


        Book book = repository.findByTitle(title);
                              //.orElseThrow(() -> new BookNotFoundException(id));
        return assembler.toModel(book);
    }



    /*EXERCITIUL 5*/
    //Exista niste erori in ex 5, asa ca am comentat acest cod

/*    @PutMapping("/library/updateBooks/{author}")
    ResponseEntity<?> replaceEmployee(@RequestBody String newAuthor, @PathVariable String author) {

        List<Book> updatedBook = repository.findByAuthor(author)
                .map(book -> {
                    book.setAuthor(newAuthor);
                    return repository.save(book);
                });

        EntityModel<List<Book>> entityModel = assembler.toModel(updatedBook);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
*/




}
