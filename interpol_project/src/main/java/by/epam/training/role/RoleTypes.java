package by.epam.training.role;

import java.util.EnumSet;
import java.util.Optional;

public enum RoleTypes {
    ADMIN("admin"),
    DEFAULT("default"),
    LOCKED("locked");

    private String name;

    RoleTypes(String name) {
        this.name = name;
    }

    public static Optional<RoleTypes> getInstance(String name) {
        return EnumSet.allOf(RoleTypes.class).stream().filter(roleTypes -> roleTypes.toString().equals(name)).findFirst();
    }
    @Override
    public String toString(){
        return name;
    }
}
