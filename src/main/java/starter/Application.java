package starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.vaadin.flow.spring.annotation.EnableVaadin;

@ComponentScan(basePackages = {"rest", "lucene", "service", "frontend"})
@EnableVaadin(value = {"frontend"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}