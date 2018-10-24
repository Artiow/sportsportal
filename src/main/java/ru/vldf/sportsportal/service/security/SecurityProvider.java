package ru.vldf.sportsportal.service.security;

import ru.vldf.sportsportal.service.security.userdetails.IdentifiedUserDetails;

public interface SecurityProvider {

    IdentifiedUserDetails authentication(String token);
}
