package ru.vldf.sportsportal.dto.security;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.validation.annotations.Password;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PasswordHolderDTO implements DataTransferObject {

    @Password
    @Trimmed
    @NotBlank
    @Size(min = 4, max = 50)
    private String password;
}
