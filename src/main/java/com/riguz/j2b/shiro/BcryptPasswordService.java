package com.riguz.j2b.shiro;

import org.apache.shiro.authc.credential.PasswordService;
import org.mindrot.jbcrypt.BCrypt;

/**
 * http://blog.csdn.net/zavens/article/details/7747815 //
 * https://issues.apache.org/jira/browse/SHIRO-290
 * 应用BCrypt的密码验证。Shiro默认是按明文密码进行匹配的，无法适用
 * 
 * @author solever
 *
 */
public class BcryptPasswordService implements PasswordService {

    @Override
    public String encryptPassword(Object plaintextPassword) throws IllegalArgumentException {
        return BCrypt.hashpw(plaintextPassword.toString(), BCrypt.gensalt(12));
    }

    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        return BCrypt.checkpw(this.toString(submittedPlaintext), encrypted);
    }

    private String toString(Object o) {
        if (o == null) {
            String msg = "Argument for String conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (o instanceof byte[]) {
            return toString((byte[]) o);
        }
        else if (o instanceof char[]) {
            return new String((char[]) o);
        }
        else if (o instanceof String) {
            return (String) o;
        }
        return o.toString();
    }
}
