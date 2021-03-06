package com.riguz.jb.model.core;

import com.riguz.jb.model.core.base.BaseFilter;

import java.util.List;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Filter extends BaseFilter<Filter> {
	public static final Filter dao = new Filter();
	public enum FIELD {
		FILTER_ID("FILTER_ID"),
		NAME("NAME"),
		ORDER("ORDER"),
		URL("URL"),
		RULES("RULES"),
		FROM_DATE("FROM_DATE"),
		THRU_DATE("THRU_DATE"),
		CREATED_DATE("CREATED_DATE"),
		LAST_UPDATED_DATE("LAST_UPDATED_DATE"),
		FILTER_STATUS("FILTER_STATUS"),
		REMARK("REMARK");

		final String field;

		private FIELD(String field) {
			this.field = field;
		}
	}
    /// {
    public List<Filter> getAllFilters() {
        return Filter.dao.find(SqlKit.sql("core.getAllFilters"));
    }
    /// }
}
