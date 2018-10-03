package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.service.generic.AbstractSecurityService;

@Service
public class OrderService extends AbstractSecurityService {

    @Autowired
    public OrderService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
    }
}
