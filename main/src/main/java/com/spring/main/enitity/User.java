package com.spring.main.enitity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
// import jakarta.persistence.Temporal;
// import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})

public class User implements UserDetails{

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Email(message = "email\'s format is wrong")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should have min 8 characters")
    private String password;

    private String profilePath;

    @PrePersist
    public void prePersist() {
        if (this.profilePath == null) {
            this.profilePath = "/user/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector.jpg";
        }
    }

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private Date emailVerifiedAt;

    private String randomCode;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Note> notes;

    // @Temporal(TemporalType.TIMESTAMP)
    // private Date createdAt;

    // @Temporal(TemporalType.TIMESTAMP)
    // private Date updatedAt;

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = new Date();
    //     updatedAt = new Date();
    // }

    // @PreUpdate
    // protected void onUpdate() {
    //     updatedAt = new Date();
    // }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(email));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
    @Override
	public boolean isEnabled() {
		return true;
	}

}
