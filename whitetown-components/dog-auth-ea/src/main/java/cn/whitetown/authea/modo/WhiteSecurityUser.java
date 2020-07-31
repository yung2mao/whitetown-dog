package cn.whitetown.authea.modo;

import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * UserDetail实现类
 * @author taixian
 * @date 2020/07/29
 **/
public class WhiteSecurityUser extends User {

    private Object userId;

    private Collection<GrantedAuthority> authorities;

    private Authentication authentication;

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getUserId() {
        return userId;
    }

    public <T> T getUserId(Class<T> claz) {
        if (!claz.isInstance(userId)) {
            throw new ClassCastException("Expected value to be of type: " + userId + ", but was " + userId.getClass());
        } else {
            return claz.cast(userId);
        }
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public WhiteSecurityUser(String username, String password) {
        super(username, password, new ArrayList<>());
    }

    public WhiteSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, new ArrayList<>());
        this.setAuthorities(authorities);
    }

    public WhiteSecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
