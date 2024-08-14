package com.ExhibitScape.app.domain.scheduleBoard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "searchkeyword")
@Getter
@Setter
@NoArgsConstructor
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "count")
    private Long count; // 검색 횟수

    public void incrementCount() {
        this.count += 1;
    }
    
    public SearchKeyword(String keyword, long count) {
        this.keyword = keyword;
        this.count = count;
    }
}