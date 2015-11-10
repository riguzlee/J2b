package com.riguz.j2b.model.entity;

import com.jfinal.log.Logger;
import com.riguz.j2b.model.Entity;

public class Token extends Entity<Token> {
    private static final long serialVersionUID = 5042104630344942964L;
    private static Logger     logger           = Logger.getLogger(Token.class.getName());
    public static final Token dao              = new Token();

    public Token() {
        super();
    }

    public enum TOKEN_TYPE {
        ACTIVATE_CODE(0), VALIDATE_CODE(1);
        int type = 0;

        TOKEN_TYPE(int t) {
            this.type = t;
        }
    }
}
