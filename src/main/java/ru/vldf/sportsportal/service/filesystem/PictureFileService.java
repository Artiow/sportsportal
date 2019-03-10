package ru.vldf.sportsportal.service.filesystem;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.vldf.sportsportal.service.filesystem.model.PictureSize;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;
import ru.vldf.sportsportal.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
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
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@Service
public class PictureFileService extends AbstractMessageService {

    private final PictureSize presize;

    private final ServletContext context;
    private final Path location;


    @Value("${pic.dir.pattern}")
    private String dirPattern;

    @Value("${pic.file.pattern}")
    private String filePattern;


    @Autowired
    public PictureFileService(
            @Value("${pic.presize.name}") String preName,
            @Value("${pic.presize.width}") Short preWidth,
            @Value("${pic.presize.height}") Short preHeight,
            @Value("${pic.location}") String location,
            ServletContext context
    ) throws IOException {
        Files.createDirectories(this.location = Paths.get(location).toAbsolutePath().normalize());
        this.presize = PictureSize.of(preName, preWidth, preHeight);
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
            Resource resource = new UrlResource(resolveFilename(id, size).toUri());
            if (resource.exists()) return ResourceBundle.of(resource, context);
            throw new PictureNotFoundException("Picture resource does not exist");
        } catch (MalformedURLException e) {
            throw new PictureNotFoundException("Cannot resolve resource location", e);
        }
    }

    // todo: refactor
    public void create(Integer id, InputStream stream, PictureSize size) throws IOException {
        Files.copy(
                resizePicture(stream, size),
                resolveFilename(id, size),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    // todo: refactor
    public void delete(Integer id, PictureSize size) throws IOException {
        Files.delete(resolveFilename(id, size));
    }


    /**
     * Returns no-crop presized picture.
     *
     * @param inputStream the picture input stream.
     * @return resized picture input stream.
     * @throws IOException if something goes wrong.
     */
    protected InputStream presizePicture(InputStream inputStream) throws IOException {
        return resizePicture(inputStream, presize, false);
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
     * Resolve picture path.
     *
     * @param identifier the picture identifier.
     * @param size       the picture size.
     * @return picture path.
     */
    protected Path resolveFilename(Integer identifier, PictureSize size) {
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
