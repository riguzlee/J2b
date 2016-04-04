package com.riguz.jb.model.core.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRole<M extends BaseRole<M>> extends Model<M> implements IBean {

	public void setRoleId(java.lang.String roleId) {
		set("ROLE_ID", roleId);
	}

	public java.lang.String getRoleId() {
		return get("ROLE_ID");
	}

	public void setIdent(java.lang.String ident) {
		set("IDENT", ident);
	}

	public java.lang.String getIdent() {
		return get("IDENT");
	}

	public void setName(java.lang.String name) {
		set("NAME", name);
	}

	public java.lang.String getName() {
		return get("NAME");
	}

	public void setFromDate(java.util.Date fromDate) {
		set("FROM_DATE", fromDate);
	}

	public java.util.Date getFromDate() {
		return get("FROM_DATE");
	}

	public void setThruDate(java.util.Date thruDate) {
		set("THRU_DATE", thruDate);
	}

	public java.util.Date getThruDate() {
		return get("THRU_DATE");
	}

	public void setCreatedDate(java.util.Date createdDate) {
		set("CREATED_DATE", createdDate);
	}

	public java.util.Date getCreatedDate() {
		return get("CREATED_DATE");
	}

	public void setLastUpdatedDate(java.util.Date lastUpdatedDate) {
		set("LAST_UPDATED_DATE", lastUpdatedDate);
	}

	public java.util.Date getLastUpdatedDate() {
		return get("LAST_UPDATED_DATE");
	}

	public void setRoleStatus(java.lang.String roleStatus) {
		set("ROLE_STATUS", roleStatus);
	}

	public java.lang.String getRoleStatus() {
		return get("ROLE_STATUS");
	}

	public void setRemark(java.lang.String remark) {
		set("REMARK", remark);
	}

	public java.lang.String getRemark() {
		return get("REMARK");
	}

	public void setReserve(java.lang.String reserve) {
		set("RESERVE", reserve);
	}

	public java.lang.String getReserve() {
		return get("RESERVE");
	}

	public String getTableName() {
		return "role";
	}

}