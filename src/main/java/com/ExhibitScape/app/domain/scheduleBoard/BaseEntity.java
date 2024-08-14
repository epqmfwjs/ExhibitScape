package com.ExhibitScape.app.domain.scheduleBoard;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

 
//이 클래스를 상속하는 자식 클래스에 매핑 정보를 상속하기 위해 사용
@MappedSuperclass
//@EntityListeners 어노테이션은 엔티티의 생명주기 이벤트를 처리하기 위한 리스너를 지정합니다.
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

 // @CreationTimestamp 어노테이션은 엔티티가 생성되어 저장될 때의 시각을 자동으로 기록
 @CreationTimestamp
 @Column(updatable = false)
 private LocalDateTime createdAt;

 // @UpdateTimestamp 어노테이션은 엔티티의 내용이 변경되어 업데이트될 때의 시각을 자동으로 기록합
 @UpdateTimestamp
 @Column(insertable = false)// 처음에는 값이 설정되지 않고 업데이트 발생 시 현재 시각으로 값이 설정됨
 private LocalDateTime updatedAt;
}
