package com.knowledgehub.repository;

import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.Rating;
import com.knowledgehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndDocument(User user, Document document);
}