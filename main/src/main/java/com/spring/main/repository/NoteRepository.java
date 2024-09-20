package com.spring.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.main.enitity.Note;
import com.spring.main.enitity.User;
@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{
//    @Query("Select n from Note n inner join User u on n.user.id = u.id where n.user.id = :userId")
List<Note> findAllNoteByUser(User user);


@Query("SELECT n FROM Note n INNER JOIN User u ON n.user.id = u.id WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
List<Note> findAllNoteByUserIdOrderedByDateDesc(@Param("userId") Long userId);


@Query("SELECT n FROM Note n INNER JOIN User u ON n.user.id = u.id WHERE n.user.id = :userId ORDER BY n.priority ASC")
List<Note> findAllNoteByUserIdOrderedByPriorityDesc(@Param("userId") Long userId);


@Query("SELECT DISTINCT n FROM Note n LEFT JOIN n.user u WHERE (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND u.id = :userId ORDER BY n.priority ASC")
List<Note> searchByKeyword(@Param("keyword") String keyword, @Param("userId") Long userId);

}
