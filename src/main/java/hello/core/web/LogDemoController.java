package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    // scope가 request 이기 때문에 그냥 DI를 하면 에러가 난다.
    // provider를 사용해 request scope 빈의 생성을 지연시켜서 정상 작동하도록 한다.
//    private final ObjectProvider<MyLogger> myLoggerProvider;

    // proxy를 사용하여 더 깔끔하게 사용하기
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();
//        MyLogger myLogger = myLoggerProvider.getObject();

        System.out.println("myLogger = " + myLogger.getClass());
        // 가짜가 나옴!
        // myLogger = class hello.core.common.MyLogger$$EnhancerBySpringCGLIB$$16dc2bf
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }
}
