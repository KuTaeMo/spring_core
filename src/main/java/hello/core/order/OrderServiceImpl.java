package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    // 조회 대상 빈이 2개 이상일 경우 - FixDiscountPolicy, RateDiscountPolicy 가 모두 빈에 등록되어있을 경우
    // 그대로 실행시킬 경우 discountPolicy에 DI하면서 에러가 남 (하나의 역할에 구현체가 두개임)
    // No qualifying bean of type 'hello.core.discount.DiscountPolicy' available: expected single matching bean but found 2: fixDiscountPolicy,rateDiscountPolicy

    // 해결방법
    // 1. 필드 명, 파라미터 명 으로 빈 이름 매칭 - 생성자 주입 시 주입할 구현체의 이름으로 설정한다.
    // DiscountPolicy rateDiscountPolicy를 생성자에 넣어줌

    // 2. @Qualifier

    // 3. @Primary
    // 주로 사용할 빈에 @Primary 어노테이션을 달아준다.
    
    // 결론 : 메인으로 사용하는 빈에는 @Primary 빈을 달아주고 서브로 사용하는 빈에는 @Qualifier를 지정해서 명시적으로 획득하게 하는 것이 코드를 깔끔하게 유지할 수 있다
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, /* @Qualifier("mainDiscountPolicy") */ @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    //수정자 자동 주입
//    @Autowired
//    public DiscountPolicy setDiscountPolicy(@MainDiscountPolicy DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
