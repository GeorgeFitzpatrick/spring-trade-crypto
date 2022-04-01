package com.georgefitzpatrick.trading.crypto.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author George Fitzpatrick
 */
public class IdentifiableEntityService<T extends IdentifiableEntity> {

    /* ----- Fields ----- */

    private final JpaRepository<T, Long> repository;

    /* ----- Constructors ----- */

    public IdentifiableEntityService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    public List<T> findAll() {
        return repository.findAll();
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public void saveAll(Iterable<T> entities) {
        repository.saveAll(entities);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteAll(Iterable<T> entities) {
        repository.deleteAll(entities);
    }

}
