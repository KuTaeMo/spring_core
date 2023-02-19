package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        // 따로 설정해주지 않으면 디폴트로 해당 설정 파일이 위치한 패키지가 시작 위치가 된다.
        basePackages = "hello.core",
        // AppConfig, TestConfig가 스캔되는 것을 막기 위해서
        // @Configuration이 붙은 클래스에 대해 스캔되는 것을 막아줌
        // 원래는 그냥 삭제하면 되지만 기본 코드를 유지하기 위해서 필터로 걸러냄
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}
