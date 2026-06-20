package com.akhil.ai_notes_manager.service;
import java.util.*;
import com.akhil.ai_notes_manager.entity.Note;
import com.akhil.ai_notes_manager.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhil.ai_notes_manager.dto.NoteDetailDTO;
import com.akhil.ai_notes_manager.dto.NoteResponseDTO;
import com.akhil.ai_notes_manager.dto.PaginatedNotesResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    public boolean existsById(Long id){
        if(noteRepository.existsById(id)){
            return true;
        }
        return false;
    }
    public Note saveNote(Note note){
        return noteRepository.save(note);
    }
    public List<NoteResponseDTO> getAllNotes(){
        return noteRepository.findAll().stream().
                map(note->new NoteResponseDTO(
                    note.getId(),
                    note.getTitle()
                ))
                .toList();
    }
    public Optional<NoteDetailDTO> getNoteById(Long id){
        return noteRepository.findById(id)
            .map(note->new NoteDetailDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
            ));
    }
    public void deleteNoteById(Long id){
        noteRepository.deleteById(id);
    }
    public Note updateNoteById(Long id,Note updatedNote){
        Optional<Note> exisitingnote=noteRepository.findById(id);
        if(exisitingnote.isPresent()){
            Note note=exisitingnote.get();         //Take Note out of Optional.
            note.setTitle(updatedNote.getTitle());
            note.setContent(updatedNote.getContent());
            return noteRepository.save(note);        //Because the note already has an ID, Hibernate performs an UPDATE instead of INSERT.
        }
        return null;
    }

    public List<NoteResponseDTO> searchNotes(String keyword){
        return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword).stream().
            map(note->new NoteResponseDTO(
                note.getId(),
                note.getTitle()
            ))
            .toList();
    }

    public PaginatedNotesResponseDTO getNotesPaginated(int page,int size,String sort, String direction){
        Sort sorting;
        if(direction.equalsIgnoreCase("desc")){
            sorting=Sort.by(sort).descending();
        }
        else{
            sorting=Sort.by(sort).ascending();
        }
        Pageable pageable = PageRequest.of(page,size,sorting);
        Page<Note> notesPage=noteRepository.findAll(pageable);
        List<NoteResponseDTO> notes=notesPage.getContent().stream().
                                                            map(note-> new NoteResponseDTO(
                                                                note.getId(),
                                                                note.getTitle()
                                                            )).toList();
        return new PaginatedNotesResponseDTO(
            notes,notesPage.getNumber(),
            notesPage.getTotalPages(),
            notesPage.getTotalElements()
        );
    }
}

