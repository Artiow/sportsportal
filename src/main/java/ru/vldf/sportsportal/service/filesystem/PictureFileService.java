package ru.vldf.sportsportal.service.filesystem;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.unit.DataSize;
import ru.vldf.sportsportal.service.filesystem.model.PictureSize;
import ru.vldf.sportsportal.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Service
public class PictureFileService {

    private final PictureSize presize;
    private final int buffer;

    private final ServletContext context;
    private final Path location;

    @Value("${pic.dir.pattern}")
    private String dirPattern;

    @Value("${pic.file.pattern}")
    private String filePattern;


    @Autowired
    public PictureFileService(
            @Value("${pic.presize.width}") Short preWidth,
            @Value("${pic.presize.height}") Short preHeight,
            @Value("${spring.servlet.multipart.max-file-size}") DataSize maxDataSize,
            @Value("${pic.location}") String location,
            ServletContext context
    ) throws IOException {
        Files.createDirectories(this.location = Paths.get(location).toAbsolutePath().normalize());
        this.presize = PictureSize.of(preWidth, preHeight);
        this.buffer = Math.toIntExact(maxDataSize.toBytes());
        this.context = context;
    }


    /**
     * Returns picture resource from filesystem by identifier and size.
     *
     * @param id   the picture identifier.
     * @param size the picture size.
     * @return picture resource bundle.
     * @throws PictureNotFoundException if picture not found in filesystem.
     */
    public ResourceBundle get(Integer id, PictureSize size) throws PictureNotFoundException {
        try {
            Assert.notNull(size, "Picture size must be not null");
            Resource resource = new UrlResource(resolvePath(id, size).toUri());
            if (resource.exists()) return ResourceBundle.of(resource, context);
            throw new PictureNotFoundException("Picture resource does not exist");
        } catch (MalformedURLException e) {
            throw new PictureNotFoundException("Cannot resolve resource location", e);
        }
    }

    /**
     * Create new picture and returns its identifier.
     *
     * @param id     the picture identifier.
     * @param source source picture input stream.
     * @param sizes  the picture stored sizes.
     * @return new created picture resource identifier.
     * @throws PictureCannotCreateException if {@link IOException} occur.
     */
    public Integer create(Integer id, InputStream source, PictureSize[] sizes) throws PictureCannotCreateException {
        try {
            Files.createDirectories(resolvePath(id));
            BufferedInputStream stream = presizePicture(source);
            stream.mark(Integer.MAX_VALUE);
            for (PictureSize size : sizes) {
                Files.copy(
                        resizePicture(stream, size),
                        resolvePath(id, size),
                        StandardCopyOption.REPLACE_EXISTING
                );
                stream.reset();
            }
            return id;
        } catch (IOException e) {
            throw new PictureCannotCreateException("Cannot store picture resource", e);
        }
    }

    /**
     * Delete pictures by identifier.
     *
     * @param id the picture identifier.
     */
    public void delete(Integer id) {
        try {
            FileSystemUtils.deleteRecursively(resolvePath(id));
        } catch (IOException e) {
            throw new PictureCannotDeleteException("Cannot delete picture resources", e);
        }
    }


    /**
     * Returns no-crop presized picture.
     *
     * @param inputStream the picture input stream.
     * @return resized picture buffered input stream.
     * @throws IOException if something goes wrong.
     */
    protected BufferedInputStream presizePicture(InputStream inputStream) throws IOException {
        return new BufferedInputStream(resizePicture(inputStream, presize, false), buffer);
    }

    /**
     * Returns crop (if necessary) resized picture.
     *
     * @param inputStream the picture input stream.
     * @param size        the picture size.
     * @return resized picture input stream.
     * @throws IOException if something goes wrong.
     */
    protected InputStream resizePicture(InputStream inputStream, PictureSize size) throws IOException {
        return resizePicture(inputStream, size, true);
    }

    private InputStream resizePicture(InputStream inputStream, PictureSize size, boolean crop) throws IOException {
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

        // crop
        if (crop) {
            img = Scalr.crop(img, (img.getWidth() - newWidth) / 2, (img.getHeight() - newHeight) / 2, newWidth, newHeight);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }


    /**
     * Resolve picture directory path.
     *
     * @param identifier the picture identifier.
     * @return picture directory path.
     */
    protected Path resolvePath(Integer identifier) {
        return this.location.resolve(getDirname(identifier));
    }

    /**
     * Resolve picture file path.
     *
     * @param identifier the picture identifier.
     * @param size       the picture size.
     * @return picture file path.
     */
    protected Path resolvePath(Integer identifier, PictureSize size) {
        return this.location.resolve(getDirname(identifier)).resolve(getFilename(identifier, size));
    }


    private String getDirname(Integer identifier) {
        return String.format(dirPattern, Objects.toString(identifier, ""));
    }

    private String getFilename(Integer identifier, PictureSize size) {
        Assert.notNull(size, "Picture size must be not null");
        return String.format(filePattern, Objects.toString(identifier, ""), size.toString());
    }
}
