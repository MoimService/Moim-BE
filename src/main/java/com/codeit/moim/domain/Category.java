package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private int categoryId;

    @Column(name= "category_title", nullable = false)
    private String categoryTitle;

    @OneToMany(mappedBy= "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Meeting> meetingList = new ArrayList<>();

}
