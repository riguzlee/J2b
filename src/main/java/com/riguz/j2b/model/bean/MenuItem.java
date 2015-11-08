package com.riguz.j2b.model.bean;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
	String url;
	String text;
	String icon;
	List<MenuItem> subMenuList = new ArrayList<MenuItem>();

	public MenuItem(String url, String text, String icon) {
		this.url = url;
		this.text = text;
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<MenuItem> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<MenuItem> subMenuList) {
		this.subMenuList = subMenuList;
	}
}
