package com.SSSP.dao;
import com.SSSP.bean.logBean;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
public interface LogRepository extends PagingAndSortingRepository<logBean,Integer> {

}
