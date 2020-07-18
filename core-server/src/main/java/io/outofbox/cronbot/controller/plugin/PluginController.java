package io.outofbox.cronbot.controller.plugin;

import io.outofbox.cronbot.controller.util.ControllerUtils;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.integration.IntegrationDefinition;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginConfiguration;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.model.plugin.PluginExecutor;
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
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/sample/integration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntegrationDefinition> getSampleIntegration(){
        IntegrationDefinition integrationDefinition = new IntegrationDefinition();
        integrationDefinition.setDescription("Short description");
        Map<String, IntegrationDefinition.Definition> definitionMap = new HashMap<>();

        IntegrationDefinition.Definition urlDef = new IntegrationDefinition.Definition();
        urlDef.setType("(string/number/password/boolean/array/map/text_area) default is string");
        urlDef.setRequired(true);
        urlDef.setDescription("URL to be post");
        definitionMap.put("url", urlDef);

        IntegrationDefinition.Definition usernameDef = new IntegrationDefinition.Definition();
        usernameDef.setType("string");
        usernameDef.setRequired(false);
        usernameDef.setDescription("username to be posted");
        definitionMap.put("username", usernameDef);

        IntegrationDefinition.Definition enumValuesDef = new IntegrationDefinition.Definition();
        enumValuesDef.setType("string");
        enumValuesDef.setRequired(false);
        enumValuesDef.setDescription("Choose value");
        enumValuesDef.setEnums(Arrays.asList("POST", "GET","DELETE"));
        definitionMap.put("http_method", enumValuesDef);

        IntegrationDefinition.Definition languageDef = new IntegrationDefinition.Definition();
        languageDef.setType("text_area");
        languageDef.setRequired(false);
        languageDef.setDescription("Enter http body");
        languageDef.setLanguage("json, sql, xml, etc...");
        definitionMap.put("body", languageDef);
        integrationDefinition.setDefinition(definitionMap);

        return  ResponseEntity.ok(integrationDefinition);
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

        Plugin plugin = pluginService.update(id, pluginDetails);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping(value = "/{plugin-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> deleteById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                                     @PathVariable("plugin-id") String id) throws FailedLoginException, NotFoundException, OperationFailureException {

        Plugin plugin = pluginService.delete( id);

        return  ResponseEntity.ok(plugin);
    }


    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plugin> addPlugin(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @RequestBody PluginDetails pluginDetails) throws ConflictExcpetion, OperationFailureException {

        Plugin plugin = pluginService.create(pluginDetails);

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

    @RequestMapping(value = "/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PluginConfiguration> getPluginExecutorConfig(@RequestBody @Valid PluginExecutor pluginExecutor) throws NotFoundException, OperationFailureException{
        PluginConfiguration pluginConfiguration = pluginService.getPluginConfig(pluginExecutor.getToken());
        return  ResponseEntity.ok(pluginConfiguration);
    }
}
