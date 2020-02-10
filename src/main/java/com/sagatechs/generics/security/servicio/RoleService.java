package com.sagatechs.generics.security.servicio;

import com.sagatechs.generics.security.dao.RoleDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

@Singleton
public class RoleService {


    @Inject
    RoleDao roleDao;

    @SuppressWarnings("unused")
    public List<Role> findAll() {
        return this.roleDao.findAll();
    }

    public Role findByRoleType(RoleType roleType) {
        return this.roleDao.findByRoleType(roleType);
    }
}
