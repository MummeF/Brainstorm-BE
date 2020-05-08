package com.dhbwstudent.brainstormbe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AccountBuilder extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void init(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
                Account account = accountRepository.findByUserName(userName);
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

                if (account != null) {
                    return User.withUsername(account.getName())
                            .password(encoder.encode(account.getPassword()))
                            .roles(account.getRole().value())
                            .build();
                } else {
                    throw new UsernameNotFoundException("Could not find the user '" + userName + "'");
                }
            }
        };
    }

    @Bean
    public AccountRepository accountRepository(){
        return new AccountRepository();
    }

}
