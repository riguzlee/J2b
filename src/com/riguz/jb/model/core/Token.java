package com.riguz.jb.model.core;

import com.riguz.jb.model.core.base.BaseToken;

import com.riguz.jb.model.ext.sqlinxml.SqlKit;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Token extends BaseToken<Token> {
	public static final Token dao = new Token();
	public enum FIELD {
		TOKEN_ID("TOKEN_ID"),
		TOKEN("TOKEN"),
		TOKEN_TYPE("TOKEN_TYPE"),
		FROM_DATE("FROM_DATE"),
		THUR_DATE("THUR_DATE"),
		CREATED_DATE("CREATED_DATE"),
		LAST_UPDATED_DATE("LAST_UPDATED_DATE"),
		EXPIRES_IN("EXPIRES_IN"),
		REMARK("REMARK");

		final String field;

		private FIELD(String field) {
			this.field = field;
		}
	}
    /// {
    public enum TOKEN_TYPE {
        ACTIVATE_CODE("A"), VALIDATE_CODE("V");
        final String tokenType;

        private TOKEN_TYPE(String t) {
            this.tokenType = t;
        }
    }

    public Token findToken(String token) {
        return Token.dao.findFirst(SqlKit.sql("core.getToken"), token);
    }
    /// }
}
