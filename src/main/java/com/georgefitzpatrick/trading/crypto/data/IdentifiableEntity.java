package com.georgefitzpatrick.trading.crypto.data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * @author George Fitzpatrick
 */
@MappedSuperclass
public class IdentifiableEntity {

    /* ----- Fields ----- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ----- Methods ----- */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPersisted() {
        return id != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        return this.id != null && obj instanceof IdentifiableEntity that && this.id.equals(that.id);
    }

}
