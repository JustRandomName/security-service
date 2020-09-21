package security.service;

import static java.util.Collections.singleton;
import static security.constants.JWTConstants.TOKEN_PREFIX;
import static security.util.UserUtil.fromDtoToEntity;

import model.dto.UserDto;
import model.entity.Role;
import model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.repository.UserRepository;
import security.util.JwtTokenUtil;

import java.util.List;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USER_DISABLED = "USER_DISABLED";
    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public String authenticate(final UserDto dto) throws Exception {
        User user = loadUserByUsername(dto.getUsername());
        authenticate(dto.getUsername(), dto.getPassword(), user);
        return jwtTokenUtil.generateToken(user);
    }

    public User saveUser(final UserDto dto) {
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        final User user = fromDtoToEntity(dto);
        user.setRoles(singleton(new Role(ROLE_USER)));
        return userRepository.save(user);
    }

    public boolean isTokenValid(final String token) {
        try {
            return jwtTokenUtil.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    public List getRolesFromToken(final String token) {
        return jwtTokenUtil.getRolesFromToken(token.replace(TOKEN_PREFIX, ""));
    }

    private void authenticate(final String username, final String password, User user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    password,
                    user.getAuthorities()));
        } catch (DisabledException e) {
            throw new Exception(USER_DISABLED, e);
        } catch (BadCredentialsException e) {
            throw new Exception(INVALID_CREDENTIALS, e);
        }
    }

}
