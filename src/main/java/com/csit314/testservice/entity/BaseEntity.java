package com.csit314.testservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;

/*BaseEntity*/
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    private UUID id;
    @NotNull
    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private UUID createdBy;

    @NotNull
    @Column(name = "created_on")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private Timestamp createdOn;

    @NotNull
    @Column(name = "updated_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private UUID updatedBy;

    @NotNull
    @Column(name = "updated_on")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private Timestamp updatedOn;

    @NotNull
    @Column(name = "is_deleted")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private Boolean isDeleted = false;

    @PrePersist
    protected void preparePersist() {
        id = UUID.randomUUID();
        //hardcoded
        createdBy = UUID.randomUUID();
        updatedBy = UUID.randomUUID();
        createdOn = Timestamp.from(Instant.now());
        updatedOn = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void prepareUpdate() {
        //hardcoded
        updatedBy = UUID.randomUUID();
        updatedOn = Timestamp.from(Instant.now());
    }

}
