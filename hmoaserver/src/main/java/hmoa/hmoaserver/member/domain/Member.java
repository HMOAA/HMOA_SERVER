package hmoa.hmoaserver.member.domain;

import hmoa.hmoaserver.brand.domain.BrandLikedMember;
import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.domain.PerfumeLikedMember;
import hmoa.hmoaserver.perfume.review.domain.PerfumeAge;
import hmoa.hmoaserver.perfume.review.domain.PerfumeGender;
import hmoa.hmoaserver.perfume.review.domain.PerfumeWeather;
import hmoa.hmoaserver.photo.domain.MemberPhoto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Slf4j
@Table(name = "MEMBER")
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;

    private String password;

    private String nickname;

    private int age;

    private boolean sex;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String refreshToken;

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<PerfumeComment> perfumeComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<PerfumeCommentLiked> perfumeCommentLikeds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrandLikedMember> brandLikedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerfumeLikedMember> perfumeLikedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<MemberPhoto> memberPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Community> communities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CommunityComment> communityComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeAge> perfumeAges = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeGender> perfumeGenders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeWeather> perfumeWeathers = new ArrayList<>();

    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void authorizeUser(){
        this.role = Role.USER;
    }
    public void updateRefreshToken(String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }

    public void updateNickname(String updateNickname){
        this.nickname=updateNickname;
    }
    public void updateSex(boolean updateSex){
        this.sex=updateSex;
    }
    public void updateAge(int age){
        this.age=age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role.getKey()));
        return auth;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public MemberPhoto getMemberPhoto() {
        int profilePhotoSize = this.memberPhotos.size();
        if (profilePhotoSize == 0)
            return null;

        MemberPhoto memberPhoto = this.memberPhotos.get(profilePhotoSize - 1);
        if (memberPhoto.isDeleted())
            return null;
        return memberPhoto;
    }
}
