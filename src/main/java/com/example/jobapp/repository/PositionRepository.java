package com.example.jobapp.repository;

import com.example.jobapp.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    @Query("SELECT p FROM Position p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "AND LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Position> searchPositions(@Param("title") String title, @Param("location") String location);
}
