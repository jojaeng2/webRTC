package webrtc.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webrtc.authservice.domain.Point;
import webrtc.authservice.domain.User;
import webrtc.authservice.dto.UserDto;
import webrtc.authservice.dto.UserDto.CreateUserRequest;
import webrtc.authservice.dto.UserDto.FindUserWithPointByEmailResponse;
import webrtc.authservice.exception.UserException;
import webrtc.authservice.exception.UserException.InsufficientPointException;
import webrtc.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Transactional
    public User saveUser(CreateUserRequest request) {
        User user = new User(request.getNickname(), bcryptEncoder.encode(request.getPassword()), request.getEmail());
        userRepository.saveUser(user);
        return user;
    }

    @Transactional
    @Cacheable(key = "#email", value = "users")
    public User findOneUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    
    @Transactional
    public FindUserWithPointByEmailResponse findOneUserWithPointByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        Point point = user.getPoint();
        return new FindUserWithPointByEmailResponse(user.getId(), user.getEmail(), user.getNickname(), point.getPoint());
    }

    @Transactional
    public void decreasePoint(String email, Long point) {
        User user = userRepository.findUserByEmail(email);
        Point userPoint = user.getPoint();
        if(userPoint.getPoint() < point) {
            throw new InsufficientPointException();
        }
        userPoint.setPoint(userPoint.getPoint() - point);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void redisDataEvict() {

    }
}
