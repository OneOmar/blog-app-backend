package com.omardev.blog.services.impl;

import com.omardev.blog.domain.entities.User;
import com.omardev.blog.repositories.UserRepository;
import com.omardev.blog.security.BlogUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load a user by their email (used as username).
     *
     * @param email the email of the user
     * @return UserDetails containing user information
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(BlogUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
