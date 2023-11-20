package com.essay.zaloapp.domain.models;

import com.essay.zaloapp.domain.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String phoneNumber;

    private String fullName;

    private String password;

    private Date birthDay;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Lob
    private byte[] imageAvatarUrl;

    @Lob
    private byte[] imageCoverPhotoUrl;

    private Date createdAt;

    private Date updatedAt;

    private Date lastActive;

    private Boolean isLocked;

    private Boolean isConfirmed;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CategoryUser category;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private OTPCode otpCode;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserOptionPoll userOptionPoll;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MessageChat> messageChatList;

    public User(String phoneNumber, String password, Date birthDay, Sex sex, byte[] imageAvatarUrl, byte[] imageCoverPhotoUrl, Date createdAt, Date updatedAt, Date lastActive, Boolean isLocked, Boolean isConfirmed, Set<Role> roles, CategoryUser category, UserOptionPoll userOptionPoll, List<MessageChat> messageChatList) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.birthDay = birthDay;
        this.sex = sex;
        this.imageAvatarUrl = imageAvatarUrl;
        this.imageCoverPhotoUrl = imageCoverPhotoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastActive = lastActive;
        this.isLocked = isLocked;
        this.isConfirmed = isConfirmed;
        this.roles = roles;
        this.category = category;
        this.userOptionPoll = userOptionPoll;
        this.messageChatList = messageChatList;
    }

    public User(String phoneNumber, String fullName, String password, HashSet<Role> roleList){
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
        this.createdAt = new Date();
        this.isConfirmed = false;
        this.roles = roleList;
    }
}
