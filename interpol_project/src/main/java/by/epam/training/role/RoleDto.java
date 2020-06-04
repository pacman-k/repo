package by.epam.training.role;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDto {
    private Long id;
    private RoleTypes roleTypes = RoleTypes.DEFAULT;

    @Override
    public String toString() {
        return roleTypes.toString();
    }
}
