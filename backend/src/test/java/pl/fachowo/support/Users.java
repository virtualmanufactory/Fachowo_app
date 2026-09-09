package pl.fachowo.support;

import pl.fachowo.user.Role;
import pl.fachowo.user.User;

import java.lang.reflect.Field;
import java.util.UUID;

public final class Users {

    private Users() {
    }

    public static User withId(String email) {
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setPasswordHash("hash");
        setId(user, UUID.randomUUID());
        return user;
    }

    public static void setId(User user, UUID id) {
        try {
            Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
