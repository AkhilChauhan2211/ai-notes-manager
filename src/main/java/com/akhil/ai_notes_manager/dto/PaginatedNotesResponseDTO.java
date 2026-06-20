package com.akhil.ai_notes_manager.dto;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedNotesResponseDTO {
    private List<NoteResponseDTO> notes;
    private int currentPages;
    private int totalPages;
    private Long totalElements;

}
