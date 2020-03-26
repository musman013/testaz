package com.nfinity.ll.testaz.domain.model; 
 
import org.hibernate.validator.constraints.Length; 
import org.springframework.data.redis.core.RedisHash; 
import org.springframework.data.redis.core.index.Indexed; 
import javax.persistence.*; 
import javax.validation.constraints.Email; 
import javax.validation.constraints.NotNull; 
import java.io.Serializable; 

@RedisHash("JwtEntity") 
public class JwtEntity implements Serializable { 
 
    private Long id; 
    private String userName; 
    private @Indexed String token; 
    
    @Id 
    @Column(name = "Id", nullable = false) 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { 
        return id; 
    } 
    public void setId(Long id) { 
        this.id = id; 
    } 
 
    @Basic 
    @Column(name = "UserName", nullable = false, length = 32) 
    @NotNull 
    @Length(max = 32, message = "Username must be less than 32 characters")
    public String getUserName() { 
        return userName; 
    } 
    public void setUserName(String userName) { 
        this.userName = userName; 
    } 
 
    @Basic 
    @Column(name = "Token", nullable = false, length=10485760) 
    @NotNull 
    @Length(max = 10485760, message = "Token must be less than 10485760 characters")
    public String getToken() { 
        return token; 
    } 
    public void setToken(String token) { 
        this.token = token; 
    } 
 
    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (!(o instanceof JwtEntity)) return false; 
        JwtEntity jwt = (JwtEntity) o; 
        return id != null && id.equals(jwt.id); 
    } 
 
    @Override 
    public int hashCode() { 
        return 31; 
    } 
 
} 