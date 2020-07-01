package io.outofbox.cronbot.controller.plugin;

import io.outofbox.cronbot.controller.util.ControllerUtils;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.model.user.UserDetails;
import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.service.plugin.IPluginService;
import io.outofbox.cronbot.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/plugins")
@Slf4j
public class PluginController {

    private IPluginService pluginService;

    @Autowired
    public PluginController(IPluginService pluginService){
        this.pluginService = pluginService;
    }

    @RequestMapping(value = "/{plugin-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> getById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                                  @PathVariable("plugin-id") String id) throws NotFoundException, OperationFailureException {

        Plugin plugin = pluginService.findById(id);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping(value = "/{plugin-id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> updatePlugin(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                                     @PathVariable("plugin-id") String id,
                                                     @RequestBody PluginDetails pluginDetails) throws NotFoundException, OperationFailureException {

        Plugin plugin = pluginService.updatePlugin(id, pluginDetails);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping(value = "/{plugin-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> deleteById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                                     @PathVariable("plugin-id") String id) throws FailedLoginException, NotFoundException, OperationFailureException {

        Plugin plugin = pluginService.deletePlugin( id);

        return  ResponseEntity.ok(plugin);
    }


    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> addPlugin(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @RequestBody PluginDetails pluginDetails) throws ConflictExcpetion, OperationFailureException {

        Plugin plugin = pluginService.createPlugin(pluginDetails);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getPlugins(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                         @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "50") int size) throws  OperationFailureException {
        size = ControllerUtils.handleSize(size);
        page = ControllerUtils.handlePage(page);

        List<Plugin> plugins = pluginService.findAllWithPage(page,size);

        return  ResponseEntity.ok(plugins);
    }

}
