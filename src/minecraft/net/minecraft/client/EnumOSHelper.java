package net.minecraft.client;

final class EnumOSHelper {
	static final int[] osList = new int[EnumOS.getOsList().length];

	static {
		try {
			osList[EnumOS.linux.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError3) {
		}

		try {
			osList[EnumOS.solaris.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError2) {
		}

		try {
			osList[EnumOS.windows.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError1) {
		}

		try {
			osList[EnumOS.macos.ordinal()] = 4;
		} catch (NoSuchFieldError noSuchFieldError0) {
		}
	}
}