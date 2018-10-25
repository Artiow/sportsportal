package ru.vldf.sportsportal.service.security.keykeeper;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

@Service
public class KeyService implements KeyProvider {

    @Override
    @Transactional
    public Pair<Payload, Payload> authentication(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public IdentifiedUserDetails authorization(Payload accessKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public Pair<Payload, Payload> refresh(Payload refreshKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void logout(Payload accessKey) {
        throw new UnsupportedOperationException();
    }
}
