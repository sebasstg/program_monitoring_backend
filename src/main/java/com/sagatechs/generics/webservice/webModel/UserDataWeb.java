package com.sagatechs.generics.webservice.webModel;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import org.unhcr.programMonitoring.webServices.model.ProjectImplementerWeb;

import java.util.ArrayList;
import java.util.List;

public class UserDataWeb {
	private Long id;
	private String username;
	
	private String nombre;
	
	private String correo;
	
	private String telefono;

	private State state;

	private List<RoleType> roles=new ArrayList<>();


	private ProjectImplementerWeb projectImplementer;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProjectImplementerWeb getProjectImplementer() {
		return projectImplementer;
	}

	public void setProjectImplementer(ProjectImplementerWeb projectImplementer) {
		this.projectImplementer = projectImplementer;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<RoleType> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleType> roles) {
		this.roles = roles;
	}
}
