package com.driver;

import java.util.Objects;

public class Group {
    private String name;
    private int numberOfParticipants;

    public Group() {
    }

    public Group(String name, int numberOfParticipants) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Group group = (Group) object;
        return numberOfParticipants == group.numberOfParticipants && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, numberOfParticipants);
    }
}
