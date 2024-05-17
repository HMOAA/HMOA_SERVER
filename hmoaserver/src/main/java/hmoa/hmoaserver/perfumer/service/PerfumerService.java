package hmoa.hmoaserver.perfumer.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfumer.domain.Perfumer;
import hmoa.hmoaserver.perfumer.dto.PerfumerSaveRequestDto;
import hmoa.hmoaserver.perfumer.dto.PerfumerUpdateRequestDto;
import hmoa.hmoaserver.perfumer.repository.PerfumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hmoa.hmoaserver.exception.Code.PERFUMER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumerService {

    private final PerfumerRepository perfumerRepository;
    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.FIFTY_SIZE.getSize());

    public Perfumer save(PerfumerSaveRequestDto requestDto) {
        return perfumerRepository.save(requestDto.toEntity());
    }

    public Page<Perfumer> findPerfumer(int pageNum) {
        return perfumerRepository.findAll(PageRequest.of(pageNum, PageSize.FIFTY_SIZE.getSize()));
    }

    public Page<Perfumer> findPerfumerByCursor(Long cursor) {
        if (PageUtil.isFistCursor(cursor)) {
            return perfumerRepository.findAllByOrderByIdDesc(DEFAULT_PAGE_REQUEST);
        }
        return perfumerRepository.findPerfumerNextPage(cursor, DEFAULT_PAGE_REQUEST);

    }
    public Perfumer findById(Long perfumerId) {
        Perfumer perfumer = perfumerRepository.findById(perfumerId)
                .orElseThrow(() -> new CustomException(null, PERFUMER_NOT_FOUND));

        if (perfumer.isDeleted()) {
            throw new CustomException(null, PERFUMER_NOT_FOUND);
        }

        return perfumer;
    }

    public void updatePerfumerContent(Long perfumerId, PerfumerUpdateRequestDto requestDto) {
        Perfumer foundPerfumer = findById(perfumerId);
        foundPerfumer.updateContent(requestDto.getContent());
        perfumerRepository.save(foundPerfumer);
    }

    public void deletePerfumer(Long perfumerId) {
        Perfumer perfumer = findById(perfumerId);

        perfumer.delete();
        perfumerRepository.save(perfumer);
    }
}
