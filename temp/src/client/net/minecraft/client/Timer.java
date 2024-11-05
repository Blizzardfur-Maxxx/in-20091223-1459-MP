package net.minecraft.client;

public final class Timer {
	float ticksPerSecond;
	double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	public float timerSpeed = 1.0F;
	public float elapsedPartialTicks = 0.0F;
	long lastSyncSysClock;
	long lastSyncHRClock;
	double timeSyncAdjustment = 1.0D;

	public Timer(float f1) {
		this.ticksPerSecond = f1;
		this.lastSyncSysClock = System.currentTimeMillis();
		this.lastSyncHRClock = System.nanoTime() / 1000000L;
	}
}