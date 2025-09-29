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
        System.out.println("Hello");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Application.class);
        TrainerService service = context.getBean(TrainerService.class);
        System.out.println(service.selectTrainer(1).getPassword());
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        System.out.println(traineeService.selectTrainee(1).getFirstName());
        System.out.println(trainingService.selectTraining(1).getTrainingName());
    }
}
