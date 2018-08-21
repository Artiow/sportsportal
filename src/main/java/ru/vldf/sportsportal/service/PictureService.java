package ru.vldf.sportsportal.service;

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

@Service
public class PictureService {

    @Value("${file.pattern}")
    private String pattern;

    @Value("${file.location}")
    private String dir;

    private MessageContainer messages;

    private Path fileStorageLocation;

    private PictureRepository pictureRepository;

    @Autowired
    public void setMessages(MessageContainer messages) {
        this.messages = messages;
    }

    @Autowired
    public void setPictureRepository(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }


    @PostConstruct
    public void setFileStorageLocation() {
        this.fileStorageLocation = Paths.get(dir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns picture by id.
     *
     * @param id {@link Integer} resource identifier
     * @return picture {@link Resource}
     * @throws ResourceNotFoundException     if record not found in database
     * @throws ResourceFileNotFoundException if file not found on disk
     */
    @Transactional(readOnly = true)
    public Resource get(@NotNull Integer id) throws ResourceNotFoundException, ResourceFileNotFoundException {
        try {
            Resource resource = new UrlResource((this.fileStorageLocation.resolve(getFilename(pictureRepository.getOne(id).getId()))).toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceFileNotFoundException(messages.getAndFormat("sportsportal.common.Picture.notExistByFile.message", id));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.common.Picture.notExistById.message", id), e);
        } catch (MalformedURLException e) {
            throw new ResourceFileNotFoundException(messages.getAndFormat("sportsportal.common.Picture.notExistByFile.message", id), e);
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
            throw new ResourceCannotCreateException(messages.get("sportsportal.common.Picture.couldNotStore.message"));
        } else {

            PictureEntity pictureEntity = new PictureEntity();

            pictureEntity.setSize(picture.getSize());
            pictureEntity.setUploaded(Timestamp.valueOf(LocalDateTime.now()));

            try {
                Integer newId = pictureRepository.save(pictureEntity).getId();
                Files.copy(picture.getInputStream(), this.fileStorageLocation.resolve(getFilename(newId)), StandardCopyOption.REPLACE_EXISTING);
                return newId;
            } catch (IOException e) {
                throw new ResourceCannotCreateException(messages.get("sportsportal.common.Picture.couldNotStore.message"), e);
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
            throw new ResourceNotFoundException(messages.getAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else {
            pictureRepository.deleteById(id);

            try {
                Files.delete(this.fileStorageLocation.resolve(getFilename(id)));
            } catch (IOException ignored) {
            }
        }
    }


    /**
     * Form picture filename on disk.
     *
     * @param identifier {@link Integer} picture identifier
     * @return {@link String} picture filename
     */
    private String getFilename(@NotNull Integer identifier) {
        return String.format(pattern, identifier);
    }
}
