package com.sagatechs.generics.facesConverters;

import com.sagatechs.generics.persistence.model.BaseEntity;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

//@RequestScoped
@SuppressWarnings("rawtypes")
@FacesValidator("uniqueColumnValidator")
public class UniqueColumnValidator implements Validator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	protected EntityManager em;

	/**
	 * generic unique constraint validator for AbstractBaseEntity entities<br />
	 * requires the following additional attributes on the form element
	 * ("<f:attribute>"):<br />
	 * - "currentEntity" the entity instance (used for getting the class and
	 * guid)<br />
	 * - "uniqueColumn" the column where the new value will be checked for
	 * uniqueness
	 */
	@Override
	public void validate(final FacesContext context, final UIComponent comp, final Object newValue)
			throws ValidatorException {

		BaseEntity currentEntity = (BaseEntity) comp.getAttributes().get("currentEntity");
		String uniqueColumn = (String) comp.getAttributes().get("uniqueColumn");

		boolean isValid = false;
		try {
			em.createQuery("FROM " + currentEntity.getClass().getSimpleName() + " o WHERE " + uniqueColumn + " = :value"
					+ " AND o.id <> :id", currentEntity.getClass()).setParameter("value", newValue)
					.setParameter("id", currentEntity.getId()).getSingleResult();
		} catch (NoResultException ex) {
			isValid = true; // good! no result means unique validation was OK!
		}

		if (!isValid) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un registro con este valor", "Ya existe un registro con este valor");
			context.addMessage(comp.getClientId(context), msg);
			throw new ValidatorException(msg);
		}
	}
}