package com.georgefitzpatrick.trading.crypto.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author George Fitzpatrick
 */
public class SingletonIdentifiableEntityService<T extends IdentifiableEntity> {

    /* ----- Fields ----- */

    private static final long ID = 0;

    private final JpaRepository<T, Long> repository;

    /* ----- Constructors ----- */

    public SingletonIdentifiableEntityService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    public Optional<T> find() {
        return repository.findById(ID);
    }

    public void save(T entity) {
        entity.setId(ID);
        repository.save(entity);
    }

    public void delete() {
        repository.deleteById(ID);
    }

}
