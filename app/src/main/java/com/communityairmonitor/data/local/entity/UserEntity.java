package com.communityairmonitor.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String community;
    private String profileImage;

    public UserEntity(@NonNull String id, String fullName, String email, String phone,
                      String role, String community, String profileImage) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.community = community;
        this.profileImage = profileImage;
    }

    @NonNull
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getCommunity() { return community; }
    public String getProfileImage() { return profileImage; }

    public void setId(@NonNull String id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(String role) { this.role = role; }
    public void setCommunity(String community) { this.community = community; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
