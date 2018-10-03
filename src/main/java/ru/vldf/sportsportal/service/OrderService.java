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
    public void setMessages(MessageContainer messages) {
        super.setMessages(messages);
    }

    @Autowired
    protected void setUserRepository(UserRepository userRepository) {
        super.setUserRepository(userRepository);
    }

    @Autowired
    protected void setRoleRepository(RoleRepository roleRepository) {
        super.setRoleRepository(roleRepository);
    }
}
