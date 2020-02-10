package com.sagatechs.generics.security;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("unused")
@ApplicationScoped
public class RolesIdentityStore implements IdentityStore {

    @Inject
    UserService userService;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {

        return this.userService.getRolesNamesByUsername(validationResult.getCallerPrincipal().getName(), State.ACTIVE);
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.PROVIDE_GROUPS);
    }

}