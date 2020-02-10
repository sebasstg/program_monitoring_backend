package com.sagatechs.generics.persistence;


import com.sagatechs.generics.persistence.model.BaseEntity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.List;

/**
 * Dao Genérico
 * @param <T>
 * @param <PK>
 */
public abstract class GenericDaoJpa<T extends BaseEntity, PK extends Serializable> {

    @PersistenceContext(unitName = "main-persistence-unit")
    protected EntityManager entityManager;

    private Class<T> entityClass;
    private Class<PK> entityPKClass;

    public GenericDaoJpa(Class<T> entityClass, Class<PK> entityPKClass) {
        this.entityClass = entityClass;
        this.entityPKClass = entityPKClass;
    }

    @SuppressWarnings("unused")
    private Class<PK> getPrimaryKeyClass() {

        return this.entityPKClass;
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }
    /*
     * public GenericDaoJpa(Class<T> entityClass, Class<PK> entityPKClass) {
     * this.entityClass = entityClass; this.entityPKClass = entityPKClass; }
     */

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public T save(T t) {

        this.getEntityManager().persist(t);
        return t;
    }

    public T find(PK id) {
        return this.entityManager.find(entityClass, id);
    }

    public T update(T t) {
        return this.entityManager.merge(t);
    }

    public void remove(T t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }


    public List<T> findAll() {
        CriteriaQuery<T> criteria = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        criteria = criteria.select(criteria.from(getEntityClass()));
        return getEntityManager().createQuery(criteria).getResultList();
    }

    public List<T> findAll(int start, int limit) {
        CriteriaQuery<T> criteria = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        criteria = criteria.select(criteria.from(getEntityClass()));
        TypedQuery<T> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(limit);
        return typedQuery.getResultList();
    }

    public long countAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(getEntityClass())));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public void removeAll() {
        String jpql = String.format("delete from %s", getEntityClass());
        Query query = getEntityManager().createQuery(jpql);
        query.executeUpdate();
    }


    /**
     * Returns the table name for a given entity type in the {@link EntityManager}.
     * @return
     */
    public String getTableName() {
        /*
         * Check if the specified class is present in the metamodel. Throws
         * IllegalArgumentException if not.
         */
        Metamodel meta = getEntityManager().getMetamodel();
        EntityType<T> entityType = meta.entity(getEntityClass());

        // Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null) ? entityType.getName().toUpperCase() : t.name();


        return tableName;
    }

    /**
     *
     *
     * @return Returns the schema name for a given entity type in the {@link EntityManager}.
     */
    public String getSchemaName() {
        /*
         * Check if the specified class is present in the metamodel. Throws
         * IllegalArgumentException if not.
         */
        //Metamodel meta = getEntityManager().getMetamodel();
        //EntityType<T> entityType = meta.entity(getEntityClass());

        // Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null) ? null : t.schema();


        return tableName;
    }

}
