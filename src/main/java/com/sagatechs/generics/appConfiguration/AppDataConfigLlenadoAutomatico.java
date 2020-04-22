package com.sagatechs.generics.appConfiguration;


import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.RoleAssigmentDao;
import com.sagatechs.generics.security.dao.RoleDao;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleAssigment;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.SecurityUtils;
import org.jboss.logging.Logger;
import org.unhcr.programMonitoring.daos.ProjectImplementerDao;
import org.unhcr.programMonitoring.model.ProjectImplementer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;


@SuppressWarnings("WeakerAccess")
@Singleton
@Startup
public class AppDataConfigLlenadoAutomatico {
    private static final Logger LOGGER = Logger.getLogger(AppDataConfigLlenadoAutomatico.class);

    @Inject
    RoleDao roleDao;

    @Inject
    RoleAssigmentDao roleAssigmentDao;

    @Inject
    UserDao userDao;


    @Inject
    SecurityUtils securityUtils;

    @Inject
    AppConfigurationDao appConfigurationDao;

    @Inject
    ProjectImplementerDao projectImplementerDao;


    private Role roleSuperAdministrador;
    private Role roleAdministrador;
    private Role roleMonitorProgramas;
    private Role roleEjecutorProyectos;

    ProjectImplementer p1;

    ProjectImplementer p2;



    @PostConstruct
    private void init() {
        LOGGER.debug("Iniciando llenado automatico");
       /*this.createAppConfigs();
        this.cargarRoles();
        this.cargarUsuarios();*/
        LOGGER.debug("Terminado llenado automático");
    }


    @SuppressWarnings("unused")
    private void createAppConfigs() {
        LOGGER.info("createAppConfigs");


        instantiateConfigurationValues("Configuración de correo electrónico. Autenticación SMTP",
                "True si requiere autemticación smtp, false caso contrario",
                AppConfigurationKey.EMAIL_SMTP, "true");
        instantiateConfigurationValues("Configuración de correo electrónico. Requiere tls",
                "True si requiere tls smtp, false caso contrario",
                AppConfigurationKey.EMAIL_TLS, "true");
        instantiateConfigurationValues("Configuración de correo electrónico. Dirección servidor SMTP",
                "Dirección del servidor smtp",
                AppConfigurationKey.EMAIL_SMTP_HOST, "smtp.gmail.com");
        instantiateConfigurationValues("Configuración de correo electrónico. Puerto SMTP",
                "Puerto del servicio  smtp",
                AppConfigurationKey.EMAIL_SMTP_PORT, "465");
        instantiateConfigurationValues("Configuración de correo electrónico. Nombre de usuario",
                "Nombre de usuario de correo electrónico",
                AppConfigurationKey.EMAIL_USERNAME, "care.ecuador.proyectos@gmail.com");
        instantiateConfigurationValues("Configuración de correo electrónico. Contraseña",
                "Contraseña de correo",
                AppConfigurationKey.EMAIL_PASSOWRD, "Sagatechs2019");
        instantiateConfigurationValues("Configuración de correo electrónico. Dirrección de correo electrónico",
                "Dirrección de correo electrónico",
                AppConfigurationKey.EMAIL_ADDRES, "care.ecuador.proyectos@gmail.com");
    }

    @SuppressWarnings("unused")
    private void cargarUsuarios() {

        p1= new ProjectImplementer();
        p1.setDescription("Implementador 1");
        p1.setCode("001");
        p1.setState(State.ACTIVE);

        projectImplementerDao.save(p1);


        p2= new ProjectImplementer();
        p2.setDescription("UNHCR");
        p2.setCode("000");
        p2.setState(State.ACTIVE);

        projectImplementerDao.save(p2);


        byte[] pass = this.securityUtils.hashPasswordByte("1234", UserService.salt);
        User superAdministrador = new User();
        superAdministrador.setUsername("superadministrador");
        superAdministrador.setName("nombre super");
        superAdministrador.setPassword(pass);
        superAdministrador.setState(State.ACTIVE);
        superAdministrador.setEmail("superadministrador@yopmail.com");
        superAdministrador.addRole(roleSuperAdministrador);
        //superAdministrador.setProjectImplementer(p2);

        this.instantiateUser(superAdministrador);

        User administrador = new User();
        administrador.setUsername("administrador");
        administrador.setName("name administrador");
        administrador.setPassword(pass);
        administrador.setState(State.ACTIVE);
        administrador.setEmail("administrador@yopmail.com");
        administrador.addRole(roleAdministrador);
        this.instantiateUser(administrador);

        User monitorProgramas = new User();
        monitorProgramas.setUsername("monitor");
        monitorProgramas.setName("name administrador");
        monitorProgramas.setPassword(pass);
        monitorProgramas.setState(State.ACTIVE);
        monitorProgramas.setEmail("monitor@yopmail.com");
        monitorProgramas.addRole(roleMonitorProgramas);
        this.instantiateUser(monitorProgramas);

        User ejecutorProyectos = new User();
        ejecutorProyectos.setUsername("ejecutor");
        ejecutorProyectos.setName("name ejecutorProyectos");
        ejecutorProyectos.setPassword(pass);
        ejecutorProyectos.setState(State.ACTIVE);
        ejecutorProyectos.setEmail("ejecutor@yopmail.com");
        ejecutorProyectos.addRole(roleEjecutorProyectos);
        ejecutorProyectos.setProjectImplementer(p2);
        this.instantiateUser(ejecutorProyectos);
    }


    @SuppressWarnings("unused")
    private void cargarRoles() {
        roleSuperAdministrador = new Role(RoleType.SUPER_ADMINISTRADOR, State.ACTIVE);
        this.instantiateRole(roleSuperAdministrador);
        roleAdministrador = new Role(RoleType.ADMINISTRADOR, State.ACTIVE);
        this.instantiateRole(roleAdministrador);
        roleMonitorProgramas = new Role(RoleType.MONITOR_DE_PROGRAMAS, State.ACTIVE);
        this.instantiateRole(roleMonitorProgramas);
        roleEjecutorProyectos = new Role(RoleType.EJECUTOR_PROYECTOS, State.ACTIVE);
        this.instantiateRole(roleEjecutorProyectos);

    }

    @SuppressWarnings("UnusedReturnValue")
    private Role instantiateRole(Role role) {
        Role roleTmp = this.roleDao.findByRoleType(role.getRoleType());
        if (roleTmp == null) {
            return this.roleDao.save(role);
        } else {
            return role;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private User instantiateUser(User user) {
        User userTmp = this.userDao.findByUserNameWithRole(user.getUsername());
        if (userTmp == null) {
            user = this.userDao.save(user);
            for (RoleAssigment roleAssigmentD : user.getRoleAssigments()) {
                this.roleAssigmentDao.save(roleAssigmentD);
            }
            return user;
        } else {
            return user;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void instantiateConfigurationValues(String nombre, String descripcion, AppConfigurationKey key,
                                                String valor) {
        AppConfiguration conf = this.appConfigurationDao.findByKey(key);
        if (conf == null) {
            AppConfiguration appConfigAppURL = new AppConfiguration(nombre, descripcion, key, valor);
            appConfigurationDao.save(appConfigAppURL);
        }
    }


}
