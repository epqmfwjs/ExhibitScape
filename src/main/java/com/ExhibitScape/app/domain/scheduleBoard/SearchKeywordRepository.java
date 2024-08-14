package com.ExhibitScape.app.domain.scheduleBoard;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    @Query("SELECT k.keyword FROM SearchKeyword k ORDER BY k.count DESC")
    List<String> findTopKeywords(Pageable pageable);

	Optional<SearchKeyword> findByKeyword(String keyword);
}
