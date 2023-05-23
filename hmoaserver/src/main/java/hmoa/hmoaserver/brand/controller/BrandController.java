package hmoa.hmoaserver.brand.controller;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.brand.dto.BrandSaveRequestDto;
import hmoa.hmoaserver.brand.service.BrandService;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.oauth.jwt.service.JwtService;
import hmoa.hmoaserver.photo.service.BrandPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"브랜드"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;
    private final PhotoService photoService;
    private final BrandPhotoService brandPhotoService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @ApiOperation(value = "브랜드 저장")
    @PostMapping(value = "/new")
    public ResponseEntity<ResultDto<Object>> saveBrand(HttpServletRequest request, @RequestParam(value="image") MultipartFile file, BrandSaveRequestDto requestDto) {

        Brand brand = brandService.save(requestDto);

        photoService.validateFileExistence(file);
        photoService.validateFileType(file);

        brandPhotoService.saveBrandPhotos(brand, file);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation(value = "브랜드 단건 조회")
    @GetMapping("/{brandId}")
    public ResponseEntity<ResultDto<Object>> findOneBrand(@PathVariable Long brandId) {
        Brand brand = brandService.findById(brandId);

        BrandDefaultResponseDto responseDto = new BrandDefaultResponseDto(brand);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation(value = "브랜드 공감하기")
    @PutMapping("/{brandId}/like")
    public ResponseEntity<ResultDto<Object>> saveBrandLikes(
            @PathVariable Long brandId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        Brand brand = brandService.findById(brandId);

        brandService.addPostLikes(brand, member);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

    @ApiOperation(value = "브랜드 공감 취소하기")
    @DeleteMapping("{brandId}/like")
    public ResponseEntity<ResultDto<Object>> deleteBrandLikes(
            @PathVariable Long brandId, @RequestHeader("X-AUTH-TOKEN") String token
    ) {
        String email = jwtService.getEmail(token);
        Member member = memberService.findByEmail(email);

        Brand brand = brandService.findById(brandId);

        brandService.deleteBrandLikes(brand, member);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build()
                );
    }

}