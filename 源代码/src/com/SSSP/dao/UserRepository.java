package com.SSSP.dao;
import javax.persistence.*;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.SSSP.bean.AllUserBean;
public interface UserRepository  extends PagingAndSortingRepository<AllUserBean,Integer>{

}
