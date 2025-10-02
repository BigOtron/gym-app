import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import service.TraineeService;
import service.TrainerService;
import service.TrainingService;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"dao", "entity", "exceptions", "service", "storage", "utility"})
public class Application {
    public static void main(String[] args) {

    }
}
