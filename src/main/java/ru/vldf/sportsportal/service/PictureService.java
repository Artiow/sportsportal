package ru.vldf.sportsportal.service;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureSizeEntity;
import ru.vldf.sportsportal.mapper.sectional.common.PictureSizeMapper;
import ru.vldf.sportsportal.repository.common.PictureRepository;
import ru.vldf.sportsportal.repository.common.PictureSizeRepository;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceFileNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class PictureService extends AbstractMessageService {

    @Value("${file.pattern}")
    private String pattern;

    @Value("${file.location}")
    private String dir;

    private Path pictureDirectory;
    private PictureRepository pictureRepository;
    private PictureSizeRepository pictureSizeRepository;
    private PictureSizeMapper pictureSizeMapper;


    @Autowired
    public PictureService(MessageContainer messages) {
        super(messages);
    }


    @Autowired
    public void setPictureRepository(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Autowired
    public void setPictureSizeRepository(PictureSizeRepository pictureSizeRepository) {
        this.pictureSizeRepository = pictureSizeRepository;
    }

    @Autowired
    public void setPictureSizeMapper(PictureSizeMapper pictureSizeMapper) {
        this.pictureSizeMapper = pictureSizeMapper;
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
     * Returns picture by id and size code.
     *
     * @param id       {@link Integer} resource identifier
     * @param sizeCode {@link String} resource size code
     * @return {@link Resource} picture
     * @throws ResourceNotFoundException     if record not found in database
     * @throws ResourceFileNotFoundException if file not found on disk
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {ResourceNotFoundException.class, ResourceFileNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public Resource get(@NotNull Integer id, String sizeCode) throws ResourceNotFoundException, ResourceFileNotFoundException {
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else if ((sizeCode != null) && !pictureSizeRepository.existsByCode(sizeCode)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else {
            try {
                Resource resource = new UrlResource(resolveFilename(
                        pictureRepository.getOne(id).getId(),
                        pictureSizeMapper.toSize(pictureSizeRepository.findByCode(sizeCode))
                ).toUri());
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
    @Transactional(
            rollbackFor = {ResourceCannotCreateException.class},
            noRollbackFor = {MaxUploadSizeExceededException.class, IOException.class}
    )
    public Integer create(MultipartFile picture) throws ResourceCannotCreateException {
        if (!Objects.equals(picture.getContentType(), MediaType.IMAGE_JPEG_VALUE)) {
            throw new ResourceCannotCreateException(mGet("sportsportal.common.Picture.couldNotStore.message"));
        } else {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setSize(picture.getSize());
            pictureEntity.setUploaded(Timestamp.valueOf(LocalDateTime.now()));
            Integer newId = pictureRepository.save(pictureEntity).getId();
            try {
                StandardCopyOption option = StandardCopyOption.REPLACE_EXISTING;
                Files.copy(picture.getInputStream(), resolveFilename(newId, null), option);
                for (PictureSizeEntity sizeEntity : pictureSizeRepository.findAll()) {
                    PictureSize size = pictureSizeMapper.toSize(sizeEntity);
                    Files.copy(
                            resizePicture(picture.getInputStream(), size),
                            resolveFilename(newId, size),
                            option
                    );
                }
                return newId;
            } catch (MaxUploadSizeExceededException e) {
                throw new ResourceCannotCreateException(mGet("sportsportal.common.Picture.uploadSizeExceeded.message"), e);
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
    @Transactional(
            rollbackFor = {ResourceNotFoundException.class}
    )
    public void delete(@NotNull Integer id) throws ResourceNotFoundException {
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(mGetAndFormat("sportsportal.common.Picture.notExistById.message", id));
        } else {
            pictureRepository.deleteById(id);
            for (PictureSizeEntity sizeEntity : pictureSizeRepository.findAll()) {
                try {
                    Files.delete(resolveFilename(id, pictureSizeMapper.toSize(sizeEntity)));
                } catch (IOException ignored) {
                }
            }
        }
    }


    /**
     * Returns resized picture.
     *
     * @param inputStream {@link InputStream} picture bytes
     * @param size        {@link PictureSize} size
     * @return {@link InputStream} resized picture bytes
     * @throws IOException if something goes wrong
     */
    private InputStream resizePicture(InputStream inputStream, PictureSize size) throws IOException {
        BufferedImage img = ImageIO.read(inputStream);
        int newWidth = size.getWidth();
        int newHeight = size.getHeight();
        double requestedFactor = size.getFactor();

        // calculate resize mode
        Scalr.Mode mode;
        Scalr.Method quality = Scalr.Method.ULTRA_QUALITY;
        double factor = ((double) img.getWidth()) / ((double) img.getHeight());
        if (factor < requestedFactor) {
            mode = Scalr.Mode.FIT_TO_WIDTH;
        } else if (factor > requestedFactor) {
            mode = Scalr.Mode.FIT_TO_HEIGHT;
        } else {
            mode = Scalr.Mode.AUTOMATIC;
        }

        // resize
        img = Scalr.resize(img, quality, mode, newWidth, newHeight);

        // calculate crop params
        int xStart, yStart;
        xStart = (img.getWidth() - newWidth) / 2;
        xStart = (xStart < 0) ? 0 : xStart;
        yStart = (img.getHeight() - newHeight) / 2;
        yStart = (yStart < 0) ? 0 : yStart;

        // crop
        img = Scalr.crop(img, xStart, yStart, newWidth, newHeight);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
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
                pattern, Optional.ofNullable(size).map((s) -> (identifier + s.toString())).orElse(identifier.toString())
        );
    }


    public static class PictureSize {

        private final String value;
        private final short width;
        private final short height;
        private final double factor;


        public PictureSize(@NotNull String value, short width, short height) {
            this.value = value;
            this.width = width;
            this.height = height;
            this.factor = ((double) this.width) / ((double) this.height);
        }


        public String getValue() {
            return value;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public double getFactor() {
            return factor;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
}
