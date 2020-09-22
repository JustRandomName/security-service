package security.service;

import static java.lang.String.valueOf;
import static java.util.Collections.singleton;
import static java.util.UUID.fromString;
import static model.enums.UserRoles.ROLE_USER;
import static model.utils.UserUtil.fromDtoToEntity;
import static security.constants.JWTConstants.TOKEN_PREFIX;

import model.dto.AuthDto;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public AuthDto authenticate(final UserDto dto) throws Exception {
        final User user = loadUserByUsername(dto.getUsername());
        authenticate(dto.getUsername(), dto.getPassword(), user);
        return new AuthDto(jwtTokenUtil.generateToken(user), user.getUsername(), valueOf(user.getId()),
                user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
    }

    public User register(final UserDto dto) {
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        final User user = fromDtoToEntity(dto);
        user.setRoles(singleton(new Role(ROLE_USER.value)));
        userRepository.save(user);
        emailService.sendEmail(user.getUsername(), valueOf(user.getId()));
        return user;
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

    public void confirm(final String userId) {
        userRepository.findById(fromString(userId)).ifPresent(this::activateUser);
    }

    private void activateUser(final User user) {
        user.setEnabled(true);
        userRepository.save(user);
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
