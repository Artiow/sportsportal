package ru.vldf.sportsportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.mapper.sectional.common.PictureSizeMapper;
import ru.vldf.sportsportal.repository.common.PictureRepository;
import ru.vldf.sportsportal.repository.common.PictureSizeRepository;
import ru.vldf.sportsportal.service.filesystem.PictureFileService;
import ru.vldf.sportsportal.service.filesystem.model.PictureSize;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;
import ru.vldf.sportsportal.util.CollectionConverter;
import ru.vldf.sportsportal.util.models.ResourceBundle;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Service
@RequiredArgsConstructor
public class PictureService extends AbstractSecurityService {

    private final PictureFileService fileService;

    private final PictureRepository pictureRepository;
    private final PictureSizeRepository pictureSizeRepository;
    private final PictureSizeMapper pictureSizeMapper;


    /**
     * Returns picture by identifier and size code.
     *
     * @param id       the picture identifier.
     * @param sizeCode the picture size code.
     * @return picture resource.
     * @throws ResourceNotFoundException if picture not found.
     */
    @Transactional(readOnly = true, rollbackFor = ResourceNotFoundException.class)
    public ResourceBundle download(Integer id, String sizeCode) throws ResourceNotFoundException {
        boolean sizeIsPresent = StringUtils.hasText(sizeCode);
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Picture.notExistById.message", id));
        } else if (sizeIsPresent && !pictureSizeRepository.existsByCode(sizeCode)) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Picture.notExistById.message", id));
        } else {
            // noinspection OptionalGetWithoutIsPresent
            return fileService.get(id, pictureSizeMapper.toSize(
                    sizeIsPresent
                            ? pictureSizeRepository.findByCode(sizeCode).get()
                            : pictureSizeRepository.findFirstByIsDefaultIsTrue()
            ));
        }
    }


    /**
     * Upload new picture and returns its identifier.
     * WARING: recommend for service use only!
     *
     * @param picture the picture resource file.
     * @return new created picture entity.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ResourceCannotCreateException if resource cannot create.
     */
    @Transactional(rollbackFor = {UnauthorizedAccessException.class, ResourceCannotCreateException.class})
    public PictureEntity upload(MultipartFile picture) throws UnauthorizedAccessException, ResourceCannotCreateException {
        if (!Objects.equals(picture.getContentType(), MediaType.IMAGE_JPEG_VALUE)) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.Picture.wrongExtension.message"));
        } else {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setUploader(getCurrentUserEntity());
            pictureEntity.setUploaded(Timestamp.valueOf(LocalDateTime.now()));
            Integer newId = pictureRepository.saveAndFlush(pictureEntity).getId();
            return pictureRepository.getOne(fileService.create(newId, picture, sizes()));
        }
    }

    /**
     * Delete picture by identifier.
     * WARING: recommend for service use only!
     *
     * @param id the picture identifier.
     * @throws ResourceNotFoundException if picture not found.
     */
    @Transactional(rollbackFor = {ResourceNotFoundException.class})
    public void delete(Integer id) throws ResourceNotFoundException {
        PictureEntity pictureEntity = pictureRepository.findById(id).orElseThrow(
                ResourceNotFoundException.supplier(msg("sportsportal.common.Picture.notExistById.message", id))
        );
        pictureRepository.delete(pictureEntity);
        fileService.delete(id);
    }


    private PictureSize[] sizes() {
        return CollectionConverter.toList(pictureSizeRepository.findAll(), pictureSizeMapper::toSize).toArray(new PictureSize[0]);
    }
}
