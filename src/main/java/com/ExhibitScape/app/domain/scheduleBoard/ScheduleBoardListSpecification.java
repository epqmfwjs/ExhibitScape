package com.ExhibitScape.app.domain.scheduleBoard;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ScheduleBoardListSpecification {

    public static Specification<ScheduleBoardList> titleOrContentContains(String keyword) {
        return new Specification<ScheduleBoardList>() {
            @Override
            public Predicate toPredicate(Root<ScheduleBoardList> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 키워드가 null이거나 빈 문자열인 경우 빈 조건을 반환합니다.
                if (keyword == null || keyword.isEmpty()) {
                    return criteriaBuilder.conjunction(); // 빈 조건
                }
                //like 패턴을  정의
                String likePattern = "%" + keyword + "%";
                
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), likePattern);
                Predicate contentPredicate = criteriaBuilder.like(root.get("content"), likePattern);
                Predicate placePredicate = criteriaBuilder.like(root.get("place"), likePattern);
                Predicate organizersPredicate = criteriaBuilder.like(root.get("organizers"), likePattern);
                // 각 컬럼에 키워드가 포함되는 경우를 OR 조건으로 결합하여 반환
                return criteriaBuilder.or(titlePredicate, contentPredicate, placePredicate, organizersPredicate);
            }
        };
    }
}
