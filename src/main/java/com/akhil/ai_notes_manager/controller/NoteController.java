package com.akhil.ai_notes_manager.controller;
import com.akhil.ai_notes_manager.service.NoteService;
import com.akhil.ai_notes_manager.dto.NoteDetailDTO;
import com.akhil.ai_notes_manager.dto.NoteResponseDTO;
import com.akhil.ai_notes_manager.dto.PaginatedNotesResponseDTO;
import com.akhil.ai_notes_manager.entity.Note;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notes")
@Tag(name = "Notes Management", description = "APIs for managing notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping
    @Operation(summary = "Create a new note")
    public Note saveNote(@Valid @RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @GetMapping
    @Operation(summary = "Get all notes")
    public List<NoteResponseDTO> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a note by ID")
    public ResponseEntity<NoteDetailDTO> getNoteById(@PathVariable Long id){
        Optional<NoteDetailDTO> note=noteService.getNoteById(id);
        if(note.isPresent()){
            return ResponseEntity.ok(note.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a note by ID")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id){
        if(noteService.existsById(id)){
            noteService.deleteNoteById(id);
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing note")
    public ResponseEntity<Note> updateNoteById(@PathVariable Long id,@RequestBody Note updatednote){
        Note note=noteService.updateNoteById(id, updatednote);
        if(note!=null){
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/search")
    @Operation(summary = "Search notes",description = "Searches notes by matching title or content")
    public List<NoteResponseDTO> searchNotes(@RequestParam String keyword){
        return noteService.searchNotes(keyword);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated and sorted notes")
    public PaginatedNotesResponseDTO getNotesPaginated(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String sort,
        @RequestParam String direction
    ){
        return noteService.getNotesPaginated(page, size,sort,direction);
    }
}
