package shibakir.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import shibakir.project.domain.CommentEntity;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

}

