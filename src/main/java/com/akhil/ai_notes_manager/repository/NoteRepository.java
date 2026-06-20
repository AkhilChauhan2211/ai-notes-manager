package com.akhil.ai_notes_manager.repository;
import com.akhil.ai_notes_manager.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface NoteRepository extends JpaRepository<Note,Long>{
    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title,String content);
}