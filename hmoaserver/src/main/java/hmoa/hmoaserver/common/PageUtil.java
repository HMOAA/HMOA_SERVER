package hmoa.hmoaserver.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {
    public static PageRequest getCursorPageRequest(final int size) {
        return PageRequest.of(PageSize.ZERO_PAGE.getSize(), size);
    }

    public static boolean isFistCursor(final Long cursor) {
        return cursor == PageSize.ZERO_PAGE.getSize();
    }

    public Page<T> convertListToPage(List<T> list, int pageNo, int pageSize){
        int startIdx = pageNo * pageSize;
        int endIdx = Math.min((startIdx + pageSize), list.size());
        List<T> sublist = new ArrayList<>();
        if(!validateIndex(startIdx,endIdx)){
            return new PageImpl<>(sublist, PageRequest.of(pageNo, pageSize), list.size());
        }
        sublist = list.subList(startIdx, endIdx);
        return new PageImpl<>(sublist, PageRequest.of(pageNo, pageSize), list.size());
    }

    public static <T> boolean isLastPage(Page<T> list) {
        return !list.hasNext();
    }

    private static boolean validateIndex(final int start, final int end){
        return start <= end;
    }
}
