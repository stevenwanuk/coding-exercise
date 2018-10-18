package com.sven.ce.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.ce.exception.ExclusionUserException;
import com.sven.ce.exception.UserNotFoundException;
import com.sven.ce.model.User;
import com.sven.ce.repository.UserRepository;
import com.sven.ce.util.Constants;

@Service
public class UserService
{

    private SimpleDateFormat sdf;
    private UserRepository userRepository;
    private ExclusionService exclusionService;

    @Autowired
    public UserService(UserRepository userRepository, ExclusionService exclusionService)
    {
        this.userRepository = userRepository;
        this.exclusionService = exclusionService;
        sdf = new SimpleDateFormat(Constants.DOB_DATE_FORMAT_PATTERN);
    }

    /**
     * create a new {@link User}
     * 
     * @param registerUserRequest register request of User
     * @return new created {@link User}
     * @throws ExclusionUserException if user is invalid / blocked by
     *         {@link ExclusionService#validate(String, String)}
     */
    public User create(User user)
    {

        String dobString = sdf.format(user.getDateOfBirth());
        if (!exclusionService.validate(dobString, user.getSsn()))
        {
            throw new ExclusionUserException();
        }

        return userRepository.save(user);

    }

    /**
     * find {@link User} by given <i>userName</i>
     * 
     * @param userName must not be null
     * @return a {@link User} matching the given <i>userName</i> or {@link Optional#empty()} if none
     *         was found.
     */
    public Optional<User> findUserByName(String userName)
    {
        return userRepository.findByUsername(userName);

    }

    /**
     * find {@link User} by the given <i>id</i>
     * 
     * @param id must not be null
     * @return a User entity matching the given <i>id</i>
     * @throws UserNotFoundException if given <i>id</i> was found
     */
    public User getUserById(Long id)
    {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    }

    /**
     * retrieve all {@link User}
     * 
     * @return all entities matching the given {@link User} or an empty {@link List} if none was
     *         found
     */
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }
}
