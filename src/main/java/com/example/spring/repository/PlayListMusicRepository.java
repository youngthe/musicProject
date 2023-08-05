package com.example.spring.repository;

import com.example.spring.dao.PlayListMusicTb;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayListMusicRepository extends JpaRepository<PlayListMusicTb, Integer> {

    List<PlayListMusicTb> getPlayListMusicTbsByCategoryNoAndUserNo(int category_no, int user_no);

    boolean existsPlayListMusicTbByMusicNoAndCategoryNo(int music_no, int category_no);
    @Transactional
    void removePlayListMusicTbByMusicNoAndCategoryNo(int music_no, int category_no);

}
