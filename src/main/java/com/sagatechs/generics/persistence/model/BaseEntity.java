package com.sagatechs.generics.persistence.model;

import java.io.Serializable;

public abstract class BaseEntity<PK extends Serializable> {

	/**
	 * @return This method should return the primary key.
	 */
	public abstract PK getId();


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity<?> other = (BaseEntity<?>) obj;
		if (this.getId() == null) {
			return other.getId() == null;
		} else return this.getId().equals(other.getId());
	}
	
	@Override
	public String toString() {
		return "BaseEntity [id=" + this.getId() + "]";
	}
}