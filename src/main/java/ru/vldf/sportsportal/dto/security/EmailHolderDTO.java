package ru.vldf.sportsportal.dto.security;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.validation.annotations.Email;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class EmailHolderDTO implements DataTransferObject {

    @Email
    @Trimmed
    @NotBlank
    @Size(min = 5, max = 254)
    private String email;
}
