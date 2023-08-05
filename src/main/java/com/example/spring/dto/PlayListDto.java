package com.example.spring.dto;

import com.example.spring.dao.MusicTb;
import com.example.spring.dao.PlayListCategoryTb;
import com.example.spring.dao.PlayListMusicTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
public class PlayListDto {

    private int category_no;

    private int userNo;

    private String category;

    private List<MusicTb> musicTbList;


    public PlayListDto(PlayListCategoryTb playListCategoryTb, List<MusicTb> playListMusicTbs){
        this.category_no = playListCategoryTb.getCategoryNo();
        this.userNo = playListCategoryTb.getUserNo();
        this.category = playListCategoryTb.getCategory();
        this.musicTbList = playListMusicTbs;
    }
}
