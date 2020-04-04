package com.eroom.erooja.domain.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class AuditProperties {
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    @Setter
    @UpdateTimestamp
    private LocalDateTime updateDt;
}
