package com.example.spring.repository;

import com.example.spring.dao.MusicLikeTb;
import com.example.spring.dao.MusicTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicLikeRepository extends JpaRepository<MusicLikeTb, Integer> {

    boolean existsMusicLikeTbByMusicNoAndUserNo(int music_no, int user_no);

    MusicLikeTb getMusicLikeTbByMusicNoAndUserNoAndType(int music_no, int user_no, int type);

    List<MusicLikeTb> getMusicLikeTbsByUserNoAndType(int user_no, int type);
}
