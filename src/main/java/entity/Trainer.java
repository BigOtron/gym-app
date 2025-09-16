package entity;

import java.util.List;

public class Trainer extends User{
    private long userId;
    private List<TrainingType> specialization;

    public Trainer() {}

    public Trainer(String firstName, String lastName, String username,
                   String password, List<TrainingType> specialization) {
        super(firstName, lastName, username, password);
        this.specialization = specialization;
    }

    public long getUserId() {
        return userId;
    }

    public Trainer setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public List<TrainingType> getSpecialization() {
        return specialization;
    }

    public Trainer setSpecialization(List<TrainingType> specialization) {
        this.specialization = specialization;
        return this;
    }
}
