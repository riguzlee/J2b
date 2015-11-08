package com.riguz.j2b.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.riguz.j2b.model.bean.MenuItem;

public class MenuFactory {

	public static List<MenuItem> getRoleMenu(String roleIdent) {
		Prop p = PropKit.use("menu.properties");
		return getRoleMenu(p, roleIdent, "");
	}

	private static List<MenuItem> getRoleMenu(Prop p, String roleIdent, String level) {
		List<MenuItem> menu = new ArrayList<MenuItem>();
		int i = 1;
		while (true) {
			String key = roleIdent + "." + i;
			if (!Strings.isNullOrEmpty(level))
				key = roleIdent + "." + level + "." + i;
			System.out.println(key);
			String pattern = p.get(key);

			if (Strings.isNullOrEmpty(pattern))
				break;
			String[] arr = pattern.split(";");
			MenuItem item = new MenuItem(arr[0], arr[1], arr[2]);
			String nextLevel = level + "." + i;
			if (Strings.isNullOrEmpty(level))
				nextLevel = i + "";
			List<MenuItem> m = getRoleMenu(p, roleIdent, nextLevel);
			item.setSubMenuList(m);
			menu.add(item);

			i += 1;
		}
		return menu;
	}
}
