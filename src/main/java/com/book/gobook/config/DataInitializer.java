package com.book.gobook.config;

import com.book.gobook.model.Books;
import com.book.gobook.model.Members;
import com.book.gobook.repository.BookRepository;
import com.book.gobook.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public DataInitializer(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 한국 시간대를 기준으로 현재 날짜를 얻습니다.
        LocalDate kstDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        // 날짜를 "yyyy-MM-dd" 형태로 포맷합니다.
        String formattedDate = kstDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(memberRepository.count() == 0) {
            memberRepository.save(Members.builder()
                    .num(1)
                    .name("관리자")
                    .phone("010-1234-5678")
                    .id("admin")
                    .pwd(encoder.encode("admin"))
                    .email("admin@admin")
                    .joindate(Date.valueOf(formattedDate))
                    .qrCode("admin")
                    .authority("ADMIN")
                    .build());
        }

        if (bookRepository.count() == 0) {
            bookRepository.save(Books.builder()
                    .num(1)
                    .title("이처럼 사소한 것들")
                    .author("클레이 키건")
                    .publisher("다산책방")
                    .category("소설")
                    .price(12420)
                    .introduction("* 문학평론가 신형철, 르포작가 은유 추천 * 2022 오웰상 소설 부문 수상 * 킬리언 머피 주연·제작 영화화 2023년 4월 국내에 처음 소개된 『맡겨진 소녀』로 국내 문인들과 문학 독자들의 열렬한 환영을 받음.")
                    .uid("584603163614")
                    .img("/img/book/novel1.jpg")
                    .location("Location 1")
                    .cnt(5)
                    .cnt_noCategory(2)
                    .sales(0)
                    .build());

            bookRepository.save(Books.builder()
                    .num(2)
                    .title("모순")
                    .author("양귀자")
                    .publisher("쓰다")
                    .category("소설")
                    .price(11700)
                    .introduction("양귀자 소설의 힘을 보여준 베스트셀러 『모순』. 1998년에 초판이 출간된 이후 132쇄를 찍으며 여전히 많은 사랑을 받고 있는 작품을, 오래도록 소장할 수 있는 양장본으로 새롭게 선보임.")
                    .uid("584603229151")
                    .img("/img/book/novel2.jpg")
                    .location("Location 2")
                    .cnt(7)
                    .cnt_noCategory(3)
                    .sales(0)
                    .build());

            bookRepository.save(Books.builder()
                    .num(3)
                    .title("남의 시선에 아랑곳하지 않기")
                    .author("차이웨이")
                    .publisher("미디어숲")
                    .category("자기계발")
                    .price(16920)
                    .introduction("멘탈이 약한 사람은 매사에 소극적이다. 마음이 단단하지 않으면 집단에 적응하기 위해 타인의 비위를 맞춰야 하고, 남의 시선과 남의 말에 신경 쓰고 쓸데없이 에너지를 낭비해야 한다. 그러는 사이..")
                    .uid("584603032512")
                    .img("/img/book/personal_development1.jpg")
                    .location("Location 3")
                    .cnt(3)
                    .cnt_noCategory(1)
                    .sales(0)
                    .build());

            bookRepository.save(Books.builder()
                    .num(4)
                    .title("똑똑한 사람은 어떻게 생각하고 질문하는가")
                    .author("이시한")
                    .publisher("북플레저")
                    .category("자기계발")
                    .price(17820)
                    .introduction("책 《똑똑한 사람은 어떻게 생각하고 질문하는가》는 당신의 생각을 확장시켜주는 책이다. 저자 이시한은 한국 멘사의 회원으로 활동했으며 삼성, SK 등 국내 유수의 기업강연을 진행했다. 그리고..")
                    .uid("584603098049")
                    .img("/img/book/personal_development2.jpg")
                    .location("Location 4")
                    .cnt(10)
                    .cnt_noCategory(4)
                    .sales(0)
                    .build());

            bookRepository.save(Books.builder()
                    .num(5)
                    .title("컬처, 문화로 쓴 세계사")
                    .author("마틴 푸크너")
                    .publisher("어크로스")
                    .category("역사/문화")
                    .price(19800)
                    .introduction("하버드대 마틴 푸크너의 인류 문화 오디세이. 모든 영웅의 원형을 만든 호메로스 서사시에서 한강과 마거릿 애트우드가 함께할 2114년 미래의 도서관까지, 인류 문화의 15가지 이야기를 통해 인간이..")
                    .uid("584603294684")
                    .img("/img/book/history_culture1.jpg")
                    .location("Location 5")
                    .cnt(8)
                    .cnt_noCategory(3)
                    .sales(0)
                    .build());
            
            bookRepository.save(Books.builder()
                    .num(6)
                    .title("테스트 제목")
                    .author("테스트 저자")
                    .publisher("테스트 출판사")
                    .category("테스트 카테고리")
                    .price(100)
                    .introduction("테스트 소개")
                    .uid("633546012590")
                    .img("/img/book/personal_development2.jpg")
                    .location("테스트 위치")
                    .cnt(8)
                    .cnt_noCategory(3)
                    .sales(0)
                    .build());
        }
    }
}
