package security.util;

import static java.lang.String.valueOf;
import static java.util.UUID.fromString;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import model.dto.UserDto;
import model.entity.User;

public final class UserUtil {

    public static User fromDtoToEntity(final UserDto dto) {
        User newUser = new User();
        if (isNotBlank((dto.getId()))) {
            newUser.setId(fromString(dto.getId()));
        }
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setMiddleName(dto.getMiddleName());
        newUser.setDOB(dto.getDOB());
        newUser.setEnabled(dto.isEnabled());
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(dto.getPassword());
        return newUser;
    }

    public static UserDto fromEntityToDto(final User user, final String token) {
        return new UserDto(valueOf(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getDOB(),
                user.getPassword(),
                user.getUsername(),
                user.isEnabled());
    }

    protected UserUtil() {
    }
}
