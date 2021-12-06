
  package com.annotation.controller;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Controller;
  import com.annotation.service.BookService;
  
  @Controller
  public class BookController {
      @Autowired
	  private BookService bookService1;
  
 

	public BookService getBookService1() {
		return bookService1;
	}

  }
