package cn.whitetown.authea.modo;

import io.jsonwebtoken.RequiredTypeException;
import io.swagger.models.auth.In;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * UserDetail实现类
 * @author taixian
 * @date 2020/07/29
 **/
public class WhiteSecurityUser extends User {

    private Object userId;

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getUserId() {
        return userId;
    }

    public <T> T getUserId(Class<T> claz) {
        if (!claz.isInstance(userId)) {
            throw new RequiredTypeException("Expected value to be of type: " + userId + ", but was " + userId.getClass());
        } else {
            return claz.cast(userId);
        }
    }

    private Collection<? extends GrantedAuthority> authorities;

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    public WhiteSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, new ArrayList<>());
        this.authorities = authorities;
    }

    public WhiteSecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
