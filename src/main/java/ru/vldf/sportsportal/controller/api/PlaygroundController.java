package ru.vldf.sportsportal.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.lease.PlaygroundDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.PlaygroundShortDTO;
import ru.vldf.sportsportal.service.PlaygroundService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceOptimisticLockException;
import ru.vldf.sportsportal.util.ResourceLocationBuilder;

import java.net.URI;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

@RestController
@Api(tags = {"Playground"})
@RequestMapping("${api-path.lease.playground}")
public class PlaygroundController {

    private PlaygroundService playgroundService;

    @Autowired
    public void setPlaygroundService(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }


    /**
     * Returns requested page with playgrounds for current page divider.
     *
     * @param divider {@link PageDividerDTO} with pagination params
     * @return {@link PageDTO<PlaygroundShortDTO>}
     */
    @PostMapping("/list")
    @ApiOperation("получить страницу с площадками")
    public PageDTO<PlaygroundShortDTO> getList(@RequestBody PageDividerDTO divider) {
        return playgroundService.getList(divider);
    }

    /**
     * Returns playground by identifier with full information.
     *
     * @param id {@link int} playground identifier
     * @return {@link PlaygroundDTO}
     * @throws ResourceNotFoundException if requested playground not found
     */
    @GetMapping("/{id}")
    @ApiOperation("получить площадку")
    public PlaygroundDTO get(@PathVariable int id) throws ResourceNotFoundException {
        return playgroundService.get(id);
    }

    /**
     * Create playground and returns its location.
     *
     * @param playgroundDTO sent {@link PlaygroundDTO}
     * @return new playgrounds {@link URI}
     */
    @PostMapping
    @ApiOperation("создать площадку")
    public ResponseEntity<Void> create(@RequestBody @Validated(PlaygroundDTO.CreateCheck.class) PlaygroundDTO playgroundDTO) {
        return ResponseEntity.created(ResourceLocationBuilder.buildURL(playgroundService.create(playgroundDTO))).build();
    }

    /**
     * Update playground by id.
     *
     * @param id            playground identifier
     * @param playgroundDTO playground data
     * @return no content
     * @throws ResourceNotFoundException       if playground not found
     * @throws ResourceOptimisticLockException if playground was already updated
     */
    @PutMapping("/{id}")
    @ApiOperation("редактировать площадку")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody @Validated(PlaygroundDTO.UpdateCheck.class) PlaygroundDTO playgroundDTO)
            throws ResourceNotFoundException, ResourceOptimisticLockException {
        playgroundService.update(id, playgroundDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete playground by id.
     *
     * @param id playground identifier
     * @return no content
     * @throws ResourceNotFoundException if playground not found
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить площадку")
    public ResponseEntity<Void> delete(@PathVariable int id) throws ResourceNotFoundException {
        playgroundService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
