package com.ExhibitScape.app.domain.scheduleBoard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByScheduleBoardList(ScheduleBoardList scheduleBoardList);
}