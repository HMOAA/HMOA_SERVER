package hmoa.hmoaserver.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    MAIN_PAGE(CacheName.MAIN_PAGE, 12, 1),
    NOTES(CacheName.NOTES, 12, 100),
    QUESTION(CacheName.QUESTION, 12, 30);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
