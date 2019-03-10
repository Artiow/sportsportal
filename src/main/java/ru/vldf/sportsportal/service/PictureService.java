package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.util.CollectionConverter;
import ru.vldf.sportsportal.util.ResourceBundle;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Service
public class PictureService extends AbstractSecurityService {

    private final PictureFileService fileService;

    private final PictureRepository pictureRepository;
    private final PictureSizeRepository pictureSizeRepository;
    private final PictureSizeMapper pictureSizeMapper;


    @Autowired
    public PictureService(
            PictureFileService fileService,
            PictureRepository pictureRepository,
            PictureSizeRepository pictureSizeRepository,
            PictureSizeMapper pictureSizeMapper
    ) {
        this.fileService = fileService;
        this.pictureRepository = pictureRepository;
        this.pictureSizeRepository = pictureSizeRepository;
        this.pictureSizeMapper = pictureSizeMapper;
    }


    /**
     * Returns picture by identifier and size code.
     *
     * @param id       the picture identifier.
     * @param sizeCode the picture size code.
     * @return picture resource.
     * @throws ResourceNotFoundException if picture not found.
     */
    @Transactional(readOnly = true, rollbackFor = ResourceNotFoundException.class)
    public ResourceBundle get(Integer id, String sizeCode) throws ResourceNotFoundException {
        boolean sizeIsPresent = StringUtils.hasText(sizeCode);
        if (!pictureRepository.existsById(id)) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Picture.notExistById.message", id));
        } else if (sizeIsPresent && !pictureSizeRepository.existsByCode(sizeCode)) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Picture.notExistById.message", id));
        } else {
            return fileService.get(id, pictureSizeMapper.toSize(
                    sizeIsPresent
                            ? pictureSizeRepository.findByCode(sizeCode)
                            : pictureSizeRepository.findFirstByIsDefaultIsTrue()
            ));
        }
    }

    /**
     * Create new picture and returns its identifier.
     *
     * @param picture the picture resource file.
     * @return new created picture resource identifier.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ResourceCannotCreateException if resource cannot create.
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ResourceCannotCreateException.class},
            noRollbackFor = {IOException.class}
    )
    public Integer create(MultipartFile picture) throws UnauthorizedAccessException, ResourceCannotCreateException {
        if (!Objects.equals(picture.getContentType(), MediaType.IMAGE_JPEG_VALUE)) {
            throw new ResourceCannotCreateException(msg("sportsportal.common.Picture.wrongExtension.message"));
        } else {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setOwner(getCurrentUserEntity());
            pictureEntity.setUploaded(Timestamp.valueOf(LocalDateTime.now()));
            Integer newId = pictureRepository.save(pictureEntity).getId();
            try {
                return fileService.create(newId, picture.getInputStream(), sizes());
            } catch (IOException e) {
                throw new ResourceCannotCreateException(msg("sportsportal.common.Picture.couldNotStore.message"), e);
            }
        }
    }

    /**
     * Delete picture by identifier.
     *
     * @param id the picture identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this picture.
     * @throws ResourceNotFoundException   if picture not found in database.
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(@NotNull Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            PictureEntity pictureEntity = pictureRepository.getOne(id);
            if ((!currentUserIsAdmin()) && (!isCurrentUser(pictureEntity.getOwner()))) {
                throw new ForbiddenAccessException(msg("sportsportal.common.Picture.forbidden.message"));
            } else {
                pictureRepository.delete(pictureEntity);
                fileService.delete(id, sizes());
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.common.Picture.notExistById.message", id), e);
        }
    }


    private PictureSize[] sizes() {
        return CollectionConverter.convertToList(pictureSizeRepository.findAll(), pictureSizeMapper::toSize).toArray(new PictureSize[0]);
    }
}
