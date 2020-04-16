package com.eroom.erooja.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class AuditProperties {
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    @Setter
    @UpdateTimestamp
    private LocalDateTime updateDt;
}
