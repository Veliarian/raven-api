package com.raven.api.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_pictures")
public class ProfilePicture {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_picture_id_seq")
    @SequenceGenerator(name = "profile_picture_id_seq", sequenceName = "profile_picture_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
