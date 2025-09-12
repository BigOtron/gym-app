package entity;

import java.time.Duration;
import java.time.LocalDateTime;

public class Training {
    private long traineeId;
    private long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingTime;
    private Duration duration;

    public Training(String trainingName, TrainingType trainingType, LocalDateTime trainingTime, Duration duration) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingTime = trainingTime;
        this.duration = duration;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public Training setTraineeId(long traineeId) {
        this.traineeId = traineeId;
        return this;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public Training setTrainerId(long trainerId) {
        this.trainerId = trainerId;
        return this;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public Training setTrainingName(String trainingName) {
        this.trainingName = trainingName;
        return this;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public Training setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
        return this;
    }

    public LocalDateTime getTrainingTime() {
        return trainingTime;
    }

    public Training setTrainingTime(LocalDateTime trainingTime) {
        this.trainingTime = trainingTime;
        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public Training setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }
}
