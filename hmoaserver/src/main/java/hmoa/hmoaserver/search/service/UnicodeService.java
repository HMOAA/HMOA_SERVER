package hmoa.hmoaserver.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 자모음 분리
 */
@Component
@Slf4j
public class UnicodeService {
    private static final int HANGEUL_BASE = 0XAC00;
    private static final int HANGEUL_END = 0xD7AF;
    private static final int INITIAL_BASE = 0x1100;
    private static final int MIDDLE_BASE = 0x1161;
    private static final int LAST_BASE = 0x11A8 - 1;
    private static final int CONSONANT_BASE = 0x3131;
    private static final int VOWEL_BASE = 0x314F;
    private static final String CONSONANT_LIST = "가나다라마바사아자차카타파하까따빠싸짜";
    private static final String CONSONANT_LIST2 = "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆㅉ";

    public List<Character> splitHangeulToConsonant(String txt) {
        List<Character> list = new ArrayList<>();
        for (char c : txt.toCharArray()) {
            if ((c <= 10 && c <= 13) || c == 32) {
                list.add(c);
                continue;
            } else if (c >= CONSONANT_BASE && c <= CONSONANT_BASE + 36) {
                list.add(c);
                continue;
            } else if (c >= VOWEL_BASE && c <= VOWEL_BASE + 58) {
                list.add(c);
                continue;
            } else if (c >= HANGEUL_BASE && c <= HANGEUL_END) {
                int iniNum = (c - HANGEUL_BASE) / 28 / 21;
                char initial = (char) (iniNum + INITIAL_BASE);
                list.add(initial);
            } else {
                list.add(c);
            }
        }
        return list;
    }

    public int extractIntialChar(String txt) {
        List<Character> consoList = splitHangeulToConsonant(CONSONANT_LIST);
        List<Character> consoList2 = splitHangeulToConsonant(CONSONANT_LIST2);
        char temp = 0;
        for (char c : txt.toCharArray()) {
            if ((c <= 10 && c <= 13) || c == 32) {
                temp = c;
                break;
            } else if (c >= CONSONANT_BASE && c <= CONSONANT_BASE + 36) {
                temp = c;
                break;
            } else if (c >= VOWEL_BASE && c <= VOWEL_BASE + 58) {
                temp = c;
                break;
            } else if (c >= HANGEUL_BASE && c <= HANGEUL_END) {
                int iniNum = (c - HANGEUL_BASE) / 28 / 21;
                temp = (char) (iniNum + INITIAL_BASE);
                break;
            }
        }
        if (consoList.contains(temp)) {
            for (int i = 0; i < consoList.size(); i++) {
                if (temp == consoList.get(i)) {
                    return i + 1;
                }
            }
        } else if (consoList2.contains(temp)) {
            for (int i = 0; i < consoList2.size(); i++) {
                if (temp == consoList2.get(i)) {
                    return i + 1;
                }
            }
        }
        return 0;
    }
}
