package ru.vldf.sportsportal.service.security.userdetails.model;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Namednev Artem
 */
public interface IdentifiedUserDetails extends UserDetails {

    int getId();
}
