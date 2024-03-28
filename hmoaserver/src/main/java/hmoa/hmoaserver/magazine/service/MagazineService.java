package hmoa.hmoaserver.magazine.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.magazine.domain.MagazineContent;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.dto.ContentRequestDto;
import hmoa.hmoaserver.magazine.dto.MagazineSaveRequestDto;
import hmoa.hmoaserver.magazine.repository.MagazineRepository;
import hmoa.hmoaserver.photo.domain.MagazinePhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.MAGAZINE_NOT_FOUND;
import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MagazineService {
    private static final String IMAGE_TYPE = "image";
    private final MagazineRepository magazineRepository;

    public Magazine save(MagazineSaveRequestDto magazineSaveRequestDto) {
        try {
            Magazine magazine = magazineRepository.save(magazineSaveRequestDto.toEntity());
            magazine.setContents(extractContentRequests(magazine, magazineSaveRequestDto.getContents()));
            return magazine;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    public Magazine savePreview(Magazine magazine, MagazinePhoto magazinePhoto, String content) {
        magazine.setPreviews(content, magazinePhoto.getPhotoUrl());
        return magazine;
    }

    public Page<Magazine> findRecentMagazineList(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return magazineRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Magazine saveImages(Magazine magazine, List<MagazinePhoto> magazinePhotos) {
        List<MagazineContent> magazineContents = magazine.getContents();
        List<Integer> imageTypeIndexs = extractContentImageIndex(magazineContents);
        int photoIndex = 0;
        if (imageTypeIndexs.size() == magazinePhotos.size()) {
            for(int index : imageTypeIndexs) {
                magazineContents.get(index).setData(magazinePhotos.get(photoIndex++).getPhotoUrl());
            }
            return magazine;
        }
        throw new CustomException(null, SERVER_ERROR);
    }

    public Magazine findById(Long magazineId) {
        return magazineRepository.findById(magazineId).orElseThrow(() -> new CustomException(null, MAGAZINE_NOT_FOUND));
    }

    public void increaseViewCount(Magazine magazine) {
        magazine.increaseViewCount();
    }

    private static List<MagazineContent> extractContentRequests(Magazine magazine, List<ContentRequestDto> contentRequestDtos) {
        return contentRequestDtos.stream()
                .map(contentRequestDto -> contentRequestDto.toEntity(magazine))
                .collect(Collectors.toList());
    }

    private static List<Integer> extractContentImageIndex(List<MagazineContent> magazineContents) {
        List<Integer> imageIndexs = new ArrayList<>();
        for(int i = 0; i < magazineContents.size(); i++) {
            if (magazineContents.get(i).getType().equals(IMAGE_TYPE)) {
                imageIndexs.add(i);
            }
        }
        return imageIndexs;
    }
}
