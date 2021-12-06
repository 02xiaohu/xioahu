 
  package com.annotation.service;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.stereotype.Service;
  import com.annotation.dao.BookDao;
  
   @Service
   public class BookService {
	 //@Qualifier("bookDao")
	 //@Autowired(required=false)
		 
	  @Autowired
	  private BookDao bookDao;
		
	  public BookDao getBookDao() {
		return bookDao;
	}


// @Override
//		public String toString() {
//			return "BookService [bookDao=" + bookDao + "]";
//		}
  }
