/*
 * This file is part of the vianifixes project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * vianifixes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * vianifixes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with vianifixes.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.vianifixes;

import java.io.*;
import java.util.Properties;

public class VianiFixesConfig
{
	public static int polymerHandshakeTimeout = 670;
	
	public static void load(File file)
	{
		if (!file.exists()) {
			save(file);
			return;
		}
		
		try {
			Properties props = new Properties();
			props.load(new FileReader(file));
			polymerHandshakeTimeout = Integer.parseInt(props.getProperty("polymerHandshakeTimeout", "670"));
		} catch (Exception e) {
		}
	}
	
	public static void save(File file)
	{
		try {
			file.getParentFile().mkdirs();
			FileWriter w = new FileWriter(file);
			w.write("# timeout in ms\n");
			w.write("polymerHandshakeTimeout=" + polymerHandshakeTimeout + "\n");
			w.close();
		} catch (Exception ignored) {}
	}
}
