package com.raven.api.users.entity;

import com.raven.api.users.enums.GroupRole;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_user_id_seq")
    @SequenceGenerator(name = "group_user_id_seq", sequenceName = "group_user_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupRole role;
}
