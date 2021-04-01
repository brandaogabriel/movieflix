package com.devgabriel.movieflix.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LogFields implements Serializable {

  @CreatedDate
  @Column(name = "create_moment", updatable = false)
  @JsonProperty("create_moment")
  private Instant createMoment = Instant.now();

  @LastModifiedDate
  @Column(name = "update_moment", insertable = false)
  @JsonProperty("update_moment")
  private Instant updateDate = Instant.now();

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Boolean status = true;

  protected LogFields() {
  }

  public Instant getCreateMoment() {
    return createMoment;
  }

  public void setCreateMoment(Instant createMoment) {
    this.createMoment = createMoment;
  }

  public Instant getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Instant updateDate) {
    this.updateDate = updateDate;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
