package com.example.spring.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "MusicLikeTb")
public class MusicLikeTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @Column(name = "user_no")
    @JsonIgnore
    private int userNo;

    @Column(name = "music_no")
    private int musicNo;

    //1 좋아요 2 싫어요
    @Column(name = "type")
    private int type;

}
