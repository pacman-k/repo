package by.epam.training.claimstatus;

import java.util.EnumSet;
import java.util.Optional;

public enum StatusTypes {
    UNDER_CONSIDERATION("under_consideration"),
    ACTUAL("actual"),
    PAYMENT_WAITING("payment_waiting");

    private String name;

    StatusTypes(String name) {
        this.name = name;
    }


    public static Optional<StatusTypes> getInstance(String name) {
        return EnumSet.allOf(StatusTypes.class).stream().filter(roleTypes -> roleTypes.toString().equals(name)).findFirst();
    }

    @Override
    public String toString() {
        return name;
    }
}
