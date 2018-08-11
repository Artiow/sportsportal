package ru.vldf.sportsportal.service.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface IdentifiedUserDetails extends UserDetails {

    int getId();
}
