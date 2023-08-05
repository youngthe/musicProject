package com.example.spring.repository;

import com.example.spring.dao.PlayListCategoryTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Repository
public interface PlayListCategoryRepository extends JpaRepository<PlayListCategoryTb, Integer> {

    List<PlayListCategoryTb> getPlayListCategoryTbsByUserNo(int user_no);

    PlayListCategoryTb getPlayListCategoryTbByCategoryNo(int category_no);
}
