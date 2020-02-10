package com.sagatechs.generics.security.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "Usuario", schema = "security",
        indexes = {@Index(name = "index_user_username",  columnList="username", unique = true)})
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity<Long> {

    public User() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NaturalId
    @NotEmpty(message = "El nombre de usuario es un dato obligatorio")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Email(message = "El correo no es una dirección válida")
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password")
    private byte[] password;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "app_user_id", columnDefinition = "TEXT")
    private String appUserId;

    @Column(name = "creation_token_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTokenDate;

    @NotNull(message = "El estado es un dato obligatorio")
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "user")
    private Set<RoleAssigment> roleAssigments = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @SuppressWarnings("unused")
    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    @SuppressWarnings("unused")
    public Date getCreationTokenDate() {
        return creationTokenDate;
    }

    @SuppressWarnings("unused")
    public void setCreationTokenDate(Date creationTokenDate) {
        this.creationTokenDate = creationTokenDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<RoleAssigment> getRoleAssigments() {
        return roleAssigments;
    }

    @SuppressWarnings("unused")
    public void setRoleAssigments(Set<RoleAssigment> roleAssigments) {
        this.roleAssigments = roleAssigments;
    }

    public void addRole(Role role){


        // busco si setá incativo, y los activo
        for (RoleAssigment roleAssigment : roleAssigments) {
            if (roleAssigment.getUser().equals(this) && roleAssigment.getRole().equals(role)) {
                roleAssigment.setState(State.ACTIVE);
                return;
            }
        }


        RoleAssigment roleAssigment = new RoleAssigment(this, role);
        this.roleAssigments.add(roleAssigment);
        role.getRoleAssigments().add(roleAssigment);
    }

    @SuppressWarnings("unused")
    public void removeRole(Role role){

        for (RoleAssigment roleAssigment : roleAssigments) {
            if (roleAssigment.getUser().equals(this) && roleAssigment.getRole().equals(role)) {
                roleAssigment.setState(State.INACTIVE);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, user.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", state=" + state +
                '}';
    }
}
