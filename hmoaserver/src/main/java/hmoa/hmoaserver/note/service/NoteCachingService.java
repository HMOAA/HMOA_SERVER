package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.config.CacheName;
import hmoa.hmoaserver.note.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteCachingService {
    private final NoteService noteService;

    // Notes 목록은 항상 같기 때문에 key 값을 정적으로 사용
    @Cacheable(cacheNames = CacheName.NOTES, key = "'NOTES'")
    public List<Note> getNotes() {
        return noteService.getNotesWithDetails();
    }
}
