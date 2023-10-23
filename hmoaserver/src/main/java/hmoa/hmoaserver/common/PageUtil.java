package hmoa.hmoaserver.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {
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

    private static boolean validateIndex(int start, int end){
        return start <= end;
    }
}
