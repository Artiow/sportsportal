package ru.vldf.sportsportal.dto.sectional.common;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.IdentifiedDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.net.URI;
import java.time.LocalDateTime;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PictureDTO implements IdentifiedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @Null(groups = FieldCheck.class)
    private URI url;

    @Null(groups = FieldCheck.class)
    private Long size;

    @Null(groups = FieldCheck.class)
    private LocalDateTime uploaded;


    public interface IdCheck {

    }

    public interface FieldCheck {

    }
}
