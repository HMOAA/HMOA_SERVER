package hmoa.hmoaserver.magazine.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.magazine.domain.ContentRequest;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.ContentRequestDto;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.repository.MagazineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MagazineService {
    private final MagazineRepository magazineRepository;

    public Magazine save(MagazineSaveRequestDto magazineSaveRequestDto) {
        try {
            Magazine magazine = magazineRepository.save(magazineSaveRequestDto.toEntity());
            magazine.setContents(extractContentRequests(magazine, magazineSaveRequestDto.getContents()));
            return magazine;
        }catch (RuntimeException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    private static List<ContentRequest> extractContentRequests(Magazine magazine, List<ContentRequestDto> contentRequestDtos) {
        return contentRequestDtos.stream()
                .map(contentRequestDto -> contentRequestDto.toEntity(magazine))
                .collect(Collectors.toList());
    }
}
