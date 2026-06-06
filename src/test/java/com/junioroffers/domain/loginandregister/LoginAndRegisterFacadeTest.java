package com.junioroffers.domain.loginandregister;

import com.junioroffers.domain.loginandregister.dto.RegisterUserDto;
import com.junioroffers.domain.loginandregister.dto.RegistrationResultDto;
import com.junioroffers.domain.loginandregister.dto.UserDto;
import net.bytebuddy.build.Plugin;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

public class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginFacade = new LoginAndRegisterFacade(new InMemoryLoginRepository());

    @Test
    public void should_find_user_by_username() {
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        RegistrationResultDto register = loginFacade.register(registerUserDto);

        UserDto userByName = loginFacade.findByUsername(register.username());

        assertThat(userByName).isEqualTo(new UserDto(register.id(), "password", "username"));
    }

    @Test
    public void should_throw_exception_when_user_not_found() {
        String username = "newUser";

        Throwable thrown = catchThrowable(() -> loginFacade.findByUsername(username));

        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void should_register_user() {
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "pass");

        RegistrationResultDto register = loginFacade.register(registerUserDto);

        assertAll(
                ()-> assertThat(register.created()).isTrue(),
                ()-> assertThat(register.username()).isEqualTo("username")
        );
    }
}