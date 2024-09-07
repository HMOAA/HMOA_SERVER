package hmoa.hmoaserver.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    MAIN_PAGE(CacheName.MAIN_PAGE, 12, 5),
    MAIN_PAGE_PERFUME(CacheName.MAIN_PAGE_PERFUME, 12, 30),
    PERFUME(CacheName.PERFUME, 12, 500),
    MAIN_PAGE_DTO(CacheName.MAIN_PAGE_DTO, 12, 5),
    NOTES(CacheName.NOTES, 12, 100),
    QUESTION(CacheName.QUESTION, 12, 30),
    PERFUME_SURVEY(CacheName.PERFUME_SURVEY, 12, 1),
    NOTE_SURVEY(CacheName.NOTE_SURVEY, 12, 1),
    SURVEY(CacheName.SURVEY, 12, 10),;

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
