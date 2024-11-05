package net.minecraft.client;

enum EnumOS {
	linux,
	solaris,
	windows,
	macos,
	unknown;

	private static final EnumOS[] osList;

	public static EnumOS[] getOsList() {
		return EnumOS.osList.clone();
	}

	static {
		osList = new EnumOS[] { EnumOS.linux, EnumOS.solaris, EnumOS.windows, EnumOS.macos, EnumOS.unknown };
	}
}
