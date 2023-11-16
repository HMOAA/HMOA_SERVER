package hmoa.hmoaserver.perfumer.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfumer.domain.Perfumer;
import hmoa.hmoaserver.perfumer.dto.PerfumerSaveRequestDto;
import hmoa.hmoaserver.perfumer.dto.PerfumerUpdateRequestDto;
import hmoa.hmoaserver.perfumer.repository.PerfumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.PERFUMER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumerService {

    private final PerfumerRepository perfumerRepository;

    public Perfumer save(PerfumerSaveRequestDto requestDto) {
        return perfumerRepository.save(requestDto.toEntity());
    }

    public List<Perfumer> findPerfumer() {
        return perfumerRepository.findAll();
    }

    public Perfumer findById(Long perfumerId) {
        Perfumer perfumer = perfumerRepository.findById(perfumerId)
                .orElseThrow(() -> new CustomException(null, PERFUMER_NOT_FOUND));

        if (perfumer.isDeleted() == true) {
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
