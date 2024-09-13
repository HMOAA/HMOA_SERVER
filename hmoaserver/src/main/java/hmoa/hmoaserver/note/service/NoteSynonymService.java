package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.NoteSynonym;
import hmoa.hmoaserver.note.repository.NoteSynonymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteSynonymService {

    private final NoteSynonymRepository noteSynonymRepository;

    public NoteSynonym getNoteSynonym(final String noteName) {
        return noteSynonymRepository.findByNoteName(noteName).orElseThrow(() -> new CustomException(null, Code.NOTE_NOT_FOUND));
    }
}
