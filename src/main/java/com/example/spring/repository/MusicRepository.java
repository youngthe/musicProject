package com.example.spring.repository;


import com.example.spring.dao.MusicTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicTb, Integer>{

    List<MusicTb> findByEmotionContainsAndSituationContains(String emotion, String situation);
}
