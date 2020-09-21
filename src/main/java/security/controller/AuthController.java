package security.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.service.UserDetailsServiceImpl;

import java.util.List;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@RestController
public class AuthController {

    private UserDetailsServiceImpl detailsService;

    @Autowired
    public AuthController(final UserDetailsServiceImpl detailsService) {
        this.detailsService = detailsService;
    }

    @RequestMapping(value = "/auth", method = POST)
    public String auth(@RequestBody final UserDto dto) throws Exception {
        return detailsService.authenticate(dto);
    }

    @RequestMapping(value = "/registration", method = POST)
    public void register(@RequestBody final UserDto dto) {
        detailsService.saveUser(dto);
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
