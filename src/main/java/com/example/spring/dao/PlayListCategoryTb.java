package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "PLAYLIST_CATEGORY_TB")
public class PlayListCategoryTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no", nullable = false)
    private int categoryNo;

    @Column(name = "user_no", nullable = false)
    private int userNo;

    @Column(name = "category", nullable = false)
    private String category;

}
