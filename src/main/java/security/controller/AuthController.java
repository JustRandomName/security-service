package security.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import model.dto.AuthDto;
import model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.service.UserDetailsServiceImpl;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 */
@RestController
public class AuthController {

    private UserDetailsServiceImpl detailsService;

    @Autowired
    public AuthController(final UserDetailsServiceImpl detailsService) {
        this.detailsService = detailsService;
    }

    @RequestMapping(value = "/auth", method = POST)
    public AuthDto auth(@RequestBody final UserDto dto) throws Exception {
        return detailsService.authenticate(dto);
    }

    @RequestMapping(value = "/registration", method = POST)
    public void register(@RequestBody final UserDto dto) {
        detailsService.register(dto);
    }

    @RequestMapping(value = "/confirm", method = GET)
    public void confirm(@PathParam("userId") final String userId) {
        detailsService.confirm(userId);
    }

    @RequestMapping(value = "/isValid", method = POST)
    public boolean isValid(@RequestBody final String token) {
        return detailsService.isTokenValid(token);
    }

    @RequestMapping(value = "/getRoles", method = POST)
    public List getRoles(@RequestBody final String token) {
        return detailsService.getRolesFromToken(token);
    }
}
