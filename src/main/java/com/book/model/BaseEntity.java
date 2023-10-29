package com.book.model;

import com.book.common.utils.LocalDateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    //private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    public String getCreatedDate() {
        return LocalDateUtils.getDate(created);
    }

    public String getCreatedDateTime() {
        return LocalDateUtils.getDateTime(created);
    }

    public String getUpdatedDate() {
        return LocalDateUtils.getDate(updated);
    }

    public String getUpdatedDateTime() {
        return LocalDateUtils.getDateTime(updated);
    }

}
