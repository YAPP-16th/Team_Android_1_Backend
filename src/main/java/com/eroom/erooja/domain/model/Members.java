package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Members extends AuditProperties {
    @Id
    private String uid;

    private String nickname;

    private String imagePath;

    @OneToOne
    private MemberAuth memberAuth;
}
