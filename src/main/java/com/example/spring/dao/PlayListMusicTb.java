package com.example.spring.dao;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "PLATLIST_MUSIC_TB")
public class PlayListMusicTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @Column(name = "category_no")
    private int categoryNo;

    @Column(name = "music_no", nullable = false)
    private int musicNo;

    @Column(name = "user_no", nullable = false)
    private int userNo;


}
