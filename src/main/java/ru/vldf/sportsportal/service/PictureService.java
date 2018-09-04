package ru.vldf.sportsportal.service;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.repository.common.PictureRepository;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceFileNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PictureService extends AbstractMessageService {

    @Value("${file.pattern}")
    private String pattern;

    @Value("${file.location}")
    private String dir;

    private Path pictureDirectory;
    private PictureRepository pictureRepository;

    @Autowired
    public void setMessages(MessageContainer messages) {
        super.setMessages(messages);
    }

    @Autowired
    public void setPictureRepository(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }


    @PostConstruct
    public void setFileStorageLocation() {
        this.pictureDirectory = Paths.get(dir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.pictureDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns picture by id.
     *
     * @param id   {@link Integer} resource identifier
     * @param size {@link PictureSize} resource size
     * @return picture {@link Resource}
     * @throws ResourceNotFoundException     if record not found in database
     * @throws ResourceFileNotFoundException if file not found on disk
     */
    @Transactional(readOnly = true)
    public Resource get(@NotNull Integer id, PictureSize size) throws ResourceNotFoundException, ResourceFileNotFoundException {
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else {
            try {
                Resource resource = new UrlResource(resolveFilename(pictureRepository.getOne(id).getId(), size).toUri());
                if (resource.exists()) {
                    return resource;
                } else {
                    throw new ResourceFileNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistByFile.message", id));
                }
            } catch (EntityNotFoundException e) {
                throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id), e);
            } catch (MalformedURLException e) {
                throw new ResourceFileNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistByFile.message", id), e);
            }
        }
    }

    /**
     * Create new picture and returns its resource id.
     *
     * @param picture picture {@link MultipartFile}
     * @return {@link Integer} resource identifier
     * @throws ResourceCannotCreateException if resource cannot create
     */
    @Transactional
    public Integer create(@NotNull MultipartFile picture) throws ResourceCannotCreateException {
        if (!picture.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)) {
            throw new ResourceCannotCreateException(mGet("sportsportal.common.Picture.couldNotStore.message"));
        } else {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setSize(picture.getSize());
            pictureEntity.setUploaded(Timestamp.valueOf(LocalDateTime.now()));
            try {
                StandardCopyOption option = StandardCopyOption.REPLACE_EXISTING;
                Integer newId = pictureRepository.save(pictureEntity).getId();
                for (PictureSize size : PictureSize.values()) {
                    Files.copy(picture.getInputStream(), resolveFilename(newId, size), option);
                }
                return newId;
            } catch (IOException e) {
                throw new ResourceCannotCreateException(mGet("sportsportal.common.Picture.couldNotStore.message"), e);
            }
        }
    }

    /**
     * Delete picture by id.
     *
     * @param id {@link Integer} picture identifier
     * @throws ResourceNotFoundException if record not found in database
     */
    @Transactional
    public void delete(@NotNull Integer id) throws ResourceNotFoundException {
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else {
            pictureRepository.deleteById(id);
            for (PictureSize size : PictureSize.values()) {
                try {
                    Files.delete(resolveFilename(id, size));
                } catch (IOException ignored) {
                }
            }
        }
    }


    /**
     * Resolve picture path.
     *
     * @param identifier {@link Integer} picture identifier
     * @param size       {@link PictureSize} picture size
     * @return {@link Path} picture path
     */
    private Path resolveFilename(@NotNull Integer identifier, PictureSize size) {
        return this.pictureDirectory.resolve(getFilename(identifier, size));
    }

    /**
     * Form picture filename on disk.
     *
     * @param identifier {@link Integer} picture identifier
     * @param size       {@link PictureSize} picture size
     * @return {@link String} picture filename
     */
    private String getFilename(@NotNull Integer identifier, PictureSize size) {
        return String.format(
                pattern, (identifier + Optional.ofNullable(size).map(PictureSize::toString).orElse(""))
        );
    }

    public enum PictureSize {
        ORIGINAL(null),
        SMALL("sm"),
        MIDDLE("md"),
        LARGE("lg");

        @JsonValue
        private final String value;

        PictureSize(String value) {
            this.value = value;
        }

        public static PictureSize fromValue(String value) {
            if (value == null) return ORIGINAL;
            for (PictureSize v : values()) {
                if (value.equalsIgnoreCase(v.value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
