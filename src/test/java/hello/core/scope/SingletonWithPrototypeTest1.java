package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        bean1.addCount();
        assertThat(bean1.getCount()).isEqualTo(1);

        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        bean2.addCount();
        assertThat(bean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean{

        // 기존에 PrototypeBean을 DI한 경우에는 ClientBean이 DI되는 시점에 주입된 것을 사용하고, ClientBean을 사용할 때마다 새로 생성되는 것이 아니다.
        // 따라서 ClientBean 인스턴스 2개를 만들어서 login()을 실행하면 결과값이 계속 이어지는 것을 볼 수 있었다.
        
        // 하지만 ClientBean을 DI하고 logic을 실행할 때마다 PrototypeBean을 DI하거나 Provider를 사용하면 
        // ClientBean을 사용할 때마다 PrototypeBean을 스프링 컨테이너에서 찾아오게 된다.
        
        // provider 사용 - DI가 아니라 DL(Dependency Lookup)을 한다.
        // provider : ObjectFactory, ObjectProvider, JSR-330 Provider (javax.inject)
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
            // ctrl + alt + n : Inline variable (합쳐줌)
            // return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static  class PrototypeBean{
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
