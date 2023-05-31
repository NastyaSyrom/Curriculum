package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Класс Plan. Данный класс представляет модель (model) данных для объекта План (Plan).
 * Этот объект содержит информацию о плане обучения для конкретной группы студентов
 * Класс содержит приватные поля groupName, formOfEducation, subject, simest и formOfAttestation
 * К каждому полю предоставлены методы доступа getProperty(), пересчитывающие значение поля в SimpleStringProperty и возвращающие его.
 * К каждому полю предоставлены методы доступа get() и set*() для получения и изменения значений полей извне.
 */
public class Plan {
    private SimpleStringProperty groupName;

    private SimpleStringProperty formOfEducation;

    private SimpleStringProperty subject;

    private SimpleStringProperty simest;

    public String getFormOfEducation() {
        return formOfEducation.get();
    }

    public void setFormOfEducation(String formOfEducation) {
        this.formOfEducation.set(formOfEducation);
    }

    private SimpleStringProperty formOfAttestation;

    public String getGroupName() {
        return groupName.get();
    }

    public SimpleStringProperty formOfEducationProperty() {
        return formOfEducation;
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public String getSimest() {
        return simest.get();
    }

    public SimpleStringProperty simestProperty() {
        return simest;
    }

    public String getFormOfAttestation() {
        return formOfAttestation.get();
    }

    public SimpleStringProperty formOfAttestationProperty() {
        return formOfAttestation;
    }

    public Plan(String groupName, String formOfEducation, String subject, String simest, String formOfAttestation) {
        this.groupName = new SimpleStringProperty(groupName);
        this.formOfEducation = new SimpleStringProperty(formOfEducation);
        this.subject = new SimpleStringProperty(subject);
        this.simest = new SimpleStringProperty(simest);
        this.formOfAttestation = new SimpleStringProperty(formOfAttestation);
    }

}