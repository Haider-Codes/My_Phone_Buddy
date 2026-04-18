package com.myphonebuddy.service;

import com.myphonebuddy.constants.AppConstants;
import com.myphonebuddy.entity.Role;
import com.myphonebuddy.entity.User;
import com.myphonebuddy.exception.UserNotFoundException;
import com.myphonebuddy.repository.UserRepository;
import com.myphonebuddy.utility.utils.EmailVerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository  userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public User saveUser(User user) {

        // Creating a default role
        Role default_role = new Role();

        if(user.getEmail().equals("haiderraza78611072@gmail.com"))
            default_role.setRoleName(AppConstants.ROLE_ADMIN);
        else
            default_role.setRoleName(AppConstants.ROLE_USER);

        // encoding the raw password using bcrypt
        log.info("user password before encode: {}", user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));;
        log.info("user password after encode: {}", user.getPassword());
        // Setting the user-role
        user.getRoles().add(default_role);

        if(!user.isEnabled()) {

            // creating a verification token, to be used later for verifying user email.
            String verificationToken = UUID.randomUUID().toString();

            String emailVerificationLink = EmailVerificationUtil.generateEmailVerificationLink(verificationToken);

            // send verification link
            emailService.sendEmail(user.getEmail(),
                    "MyPhoneBuddy : Verify your email to enable your account",
                    emailVerificationLink);

            log.info("Email verification sent to {}", user.getEmail());

            // saving verification token in the database
            user.setVerificationToken(verificationToken);
        }
        else {
            log.info("User account is already enabled as it is logged in using: {}", user.getProvider());
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));

        // Updating user information if user exists
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(!user.getPassword().equals(existingUser.getPassword()) && !user.getPassword().equals("dummy") ?
                passwordEncoder.encode(user.getPassword()) : existingUser.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());
        existingUser.setEmailVerified(user.isEmailVerified());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setPhoneVerified(user.isPhoneVerified());
        existingUser.setProvider(user.getProvider());
        existingUser.setProfilePic(user.getProfilePic());

        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        var exist_user = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(exist_user);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User exist_user = userRepository.findByEmail(email).orElse(null);
        return exist_user != null;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByToken(String token) {
        return userRepository.findByVerificationToken(token);
    }
}
