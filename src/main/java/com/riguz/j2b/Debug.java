package com.riguz.j2b;

import com.jfinal.core.JFinal;

public class Debug {

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8001, "/", 5);
	}
}
