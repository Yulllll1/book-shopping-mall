package io.elice.shoppingmall.product.controller;

import io.elice.shoppingmall.product.dto.BookDTO;
import io.elice.shoppingmall.product.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //상품 목록 조회 페이지
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> viewBookList() {
        List<BookDTO> bookList = bookService.getAllBooks();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    //상품 상세 조회 페이지
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDTO> viewBook(@PathVariable("bookId") Long id) {
        BookDTO bookDTO = bookService.searchBookById(id);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    //상품 추가 페이지(관리자 전용)
    @PostMapping("/admin/book")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO newBook = bookService.saveBook(bookDTO);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    //상품 수정 페이지(관리자 전용)
    @PutMapping("/admin/book/{bookId}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("bookId") Long id, @RequestBody BookDTO bookDTO) {
        bookDTO.setId(id);
        BookDTO selectedBook = bookService.modifyBookInfo(bookDTO);
        return new ResponseEntity<>(selectedBook, HttpStatus.OK);
    }

    //상품 삭제 페이지(관리자 전용)
    @DeleteMapping("/admin/book/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Long id) {
        bookService.removeBook(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
