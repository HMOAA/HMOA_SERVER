package hmoa.hmoaserver.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("멤버 정보가 업데이트 된다.")
    void member_info_update() {

        //given
        Member member = new Member();

        //when
        member.authorizeUser();
        member.updateAge(26);
        member.updateNickname("Jonghyun");
        member.updateSex(true);
        member.updateFCMToken("hmoa");
        member.updateRefreshToken("hmoa");

        //then
        assertAll(
                () -> assertEquals(Role.USER, member.getRole()),
                () -> assertEquals(26, member.getAge()),
                () -> assertEquals("Jonghyun", member.getNickname()),
                () -> assertTrue(member.isSex()),
                () -> assertEquals("hmoa", member.getFirebaseToken()),
                () -> assertEquals("hmoa", member.getRefreshToken())
        );
    }

    @Test
    @DisplayName("MemberPhoto가 없을 경우 null을 반환한다.")
    void member_photo_null() {

        //given
        Member member = new Member();

        //when, then
        assertNull(member.getMemberPhoto());
    }

    @Test
    @DisplayName("추천 받은 향료가 없을 경우 null을 반환한다.")
    void member_recommend_note_null() {

        //given
        Member member = new Member();

        //when, then
        assertNull(member.getNoteRecommend());
    }

    @Test
    @DisplayName("Member id가 같을 경우 equals True를 반환한다.")
    void member_id_equals_True() {

        //given
        Member member = Member.builder().id(1L).build();
        Member member2 = Member.builder().id(1L).build();

        //when, then
        assertTrue(member.equals(member2));
    }
}