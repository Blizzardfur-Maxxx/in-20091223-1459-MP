package net.minecraft.client;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.File;
import java.nio.IntBuffer;
import javax.swing.JOptionPane;

import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityDiggingFX;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.player.MovementInputFromKeys;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.MathHelper;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerCreative(this);
	private boolean fullScreen = false;
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayer thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	private Canvas appletCanvas;
	public boolean appletMode = false;
	public volatile boolean isGamePaused = false;
	private Cursor cursor;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	private GuiScreen currentScreen = null;
	private LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private Robot mouseRobot;
	private GuiIngame ingameGUI;
	public MovingObjectPosition objectMouseOver;
	public GameSettings options;
	String serverIp;
	public volatile boolean running;
	public String debug;
	private boolean inventoryScreen;
	private int prevFrameTime;
	public boolean thirdPersonView;

	public Minecraft(Canvas canvas1, int i2, int i3, boolean z4) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
		this.serverIp = null;
		this.running = false;
		this.debug = "";
		this.inventoryScreen = false;
		this.prevFrameTime = 0;
		this.thirdPersonView = false;
		new ThreadSleepForever();
		this.appletCanvas = canvas1;
		this.displayWidth = i2;
		this.displayHeight = i3;
		this.fullScreen = z4;
		if(canvas1 != null) {
			try {
				this.mouseRobot = new Robot();
				return;
			} catch (AWTException aWTException5) {
				aWTException5.printStackTrace();
			}
		}

	}

	public final void displayGuiScreen(GuiScreen guiScreen1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onClose();
			}

			if(guiScreen1 == null && this.thePlayer.health <= 0) {
				guiScreen1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)guiScreen1;
			if(guiScreen1 != null) {
				if(this.inventoryScreen) {
					this.thePlayer.resetKeyState();
					this.inventoryScreen = false;
					if(this.appletMode) {
						try {
							Mouse.setNativeCursor((Cursor)null);
						} catch (LWJGLException lWJGLException4) {
							lWJGLException4.printStackTrace();
						}
					} else {
						Mouse.setGrabbed(false);
					}
				}

				int i2 = this.displayWidth * 240 / this.displayHeight;
				int i3 = this.displayHeight * 240 / this.displayHeight;
				((GuiScreen)guiScreen1).setWorldAndResolution(this, i2, i3);
			} else {
				this.setIngameFocus();
			}
		}
	}

	private static void checkGLError(String string0) {
		int i1;
		if((i1 = GL11.glGetError()) != 0) {
			String string2 = GLU.gluErrorString(i1);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string0);
			System.out.println(i1 + ": " + string2);
			System.exit(0);
		}

	}

	public final void shutdownMinecraftApplet() {
		try {
			if(this.downloadResourcesThread != null) {
				this.downloadResourcesThread.closing = true;
			}
		} catch (Exception exception1) {
		}

		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public final void run() {
		this.running = true;

		Point point10;
		try {
			Minecraft minecraft4 = this;
			if(this.appletCanvas != null) {
				Display.setParent(this.appletCanvas);
			} else if(this.fullScreen) {
				Display.setFullscreen(true);
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
			}

			Display.setTitle("Minecraft 0.31");

			try {
				Display.create();
			} catch (LWJGLException lWJGLException31) {
				lWJGLException31.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException interruptedException30) {
				}

				Display.create();
			}

			Keyboard.create();
			Mouse.create();

			try {
				Controllers.create();
			} catch (Exception exception29) {
				exception29.printStackTrace();
			}

			checkGLError("Pre startup");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			checkGLError("Startup");
			String string12 = "minecraft";
			Object object11 = null;
			String string13 = System.getProperty("user.home", ".");
			File file14;
			String string16;
			switch(EnumOSHelper.osList[((string16 = System.getProperty("os.name").toLowerCase()).contains("win") ? EnumOS.windows : (string16.contains("mac") ? EnumOS.macos : (string16.contains("solaris") ? EnumOS.solaris : (string16.contains("sunos") ? EnumOS.solaris : (string16.contains("linux") ? EnumOS.linux : (string16.contains("unix") ? EnumOS.linux : EnumOS.unknown)))))).ordinal()]) {
			case 1:
			case 2:
				file14 = new File(string13, '.' + string12 + '/');
				break;
			case 3:
				String string15;
				if((string15 = System.getenv("APPDATA")) != null) {
					file14 = new File(string15, "." + string12 + '/');
				} else {
					file14 = new File(string13, '.' + string12 + '/');
				}
				break;
			case 4:
				file14 = new File(string13, "Library/Application Support/" + string12);
				break;
			default:
				file14 = new File(string13, string12 + '/');
			}

			if(!file14.exists() && !file14.mkdirs()) {
				throw new RuntimeException("The working directory could not be created: " + file14);
			}

			File file7 = file14;
			this.options = new GameSettings(this, file14);
			this.renderEngine = new RenderEngine(this.options);
			this.renderEngine.registerTextureFX(new TextureLavaFX());
			this.renderEngine.registerTextureFX(new TextureWaterFX());
			this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
			this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
			IntBuffer intBuffer8;
			(intBuffer8 = BufferUtils.createIntBuffer(256)).clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
			if(this.serverIp != null && this.session != null) {
				World world43;
				(world43 = new World()).generate(8, 8, 8, new byte[512]);
				this.setLevel(world43);
			} else {
				boolean z9 = false;
				point10 = null;
				if(this.theWorld == null) {
					this.generateLevel(0);
				}
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
			if(this.appletMode) {
				try {
					minecraft4.cursor = new Cursor(16, 16, 0, 0, 1, intBuffer8, (IntBuffer)null);
				} catch (LWJGLException lWJGLException28) {
					lWJGLException28.printStackTrace();
				}
			}

			try {
				minecraft4.downloadResourcesThread = new ThreadDownloadResources(file7, minecraft4);
				minecraft4.downloadResourcesThread.start();
			} catch (Exception exception27) {
			}

			checkGLError("Post startup");
			this.ingameGUI = new GuiIngame(this, this.displayWidth, this.displayHeight);
			(new ThreadDownloadSkin(this)).start();
		} catch (Exception exception36) {
			exception36.printStackTrace();
			JOptionPane.showMessageDialog((Component)null, exception36.toString(), "Failed to start Minecraft", 0);
			return;
		}

		long j1 = System.currentTimeMillis();
		int i3 = 0;

		try {
			while(this.running) {
				if(this.isGamePaused) {
					Thread.sleep(100L);
				} else {
					if(this.appletCanvas == null && Display.isCloseRequested()) {
						this.running = false;
					}

					try {
						Timer timer37 = this.timer;
						long j40;
						long j44 = (j40 = System.currentTimeMillis()) - timer37.lastSyncSysClock;
						long j47 = System.nanoTime() / 1000000L;
						double d54;
						if(j44 > 1000L) {
							long j51 = j47 - timer37.lastSyncHRClock;
							d54 = (double)j44 / (double)j51;
							timer37.timeSyncAdjustment += (d54 - timer37.timeSyncAdjustment) * (double)0.2F;
							timer37.lastSyncSysClock = j40;
							timer37.lastSyncHRClock = j47;
						}

						if(j44 < 0L) {
							timer37.lastSyncSysClock = j40;
							timer37.lastSyncHRClock = j47;
						}

						double d52;
						d54 = ((d52 = (double)j47 / 1000.0D) - timer37.lastHRTime) * timer37.timeSyncAdjustment;
						timer37.lastHRTime = d52;
						if(d54 < 0.0D) {
							d54 = 0.0D;
						}

						if(d54 > 1.0D) {
							d54 = 1.0D;
						}

						timer37.elapsedPartialTicks = (float)((double)timer37.elapsedPartialTicks + d54 * (double)timer37.timerSpeed * (double)timer37.ticksPerSecond);
						timer37.elapsedTicks = (int)timer37.elapsedPartialTicks;
						if(timer37.elapsedTicks > 100) {
							timer37.elapsedTicks = 100;
						}

						timer37.elapsedPartialTicks -= (float)timer37.elapsedTicks;
						timer37.renderPartialTicks = timer37.elapsedPartialTicks;

						for(int i38 = 0; i38 < this.timer.elapsedTicks; ++i38) {
							++this.ticksRan;
							this.runTick();
						}

						checkGLError("Pre render");
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						this.playerController.setPartialTime(this.timer.renderPartialTicks);
						float f41 = this.timer.renderPartialTicks;
						EntityRenderer entityRenderer39 = this.entityRenderer;
						if(this.entityRenderer.displayActive && !Display.isActive()) {
							entityRenderer39.mc.displayInGameMenu();
						}

						entityRenderer39.displayActive = Display.isActive();
						int i42;
						int i45;
						int i49;
						if(entityRenderer39.mc.inventoryScreen) {
							i42 = 0;
							i45 = 0;
							if(entityRenderer39.mc.appletMode) {
								if(entityRenderer39.mc.appletCanvas != null) {
									i49 = (point10 = entityRenderer39.mc.appletCanvas.getLocationOnScreen()).x + entityRenderer39.mc.displayWidth / 2;
									int i50 = point10.y + entityRenderer39.mc.displayHeight / 2;
									Point point53;
									i42 = (point53 = MouseInfo.getPointerInfo().getLocation()).x - i49;
									i45 = -(point53.y - i50);
									entityRenderer39.mc.mouseRobot.mouseMove(i49, i50);
								} else {
									Mouse.setCursorPosition(entityRenderer39.mc.displayWidth / 2, entityRenderer39.mc.displayHeight / 2);
								}
							} else {
								i42 = Mouse.getDX();
								i45 = Mouse.getDY();
							}

							byte b46 = 1;
							if(entityRenderer39.mc.options.invertMouse) {
								b46 = -1;
							}

							float f10001 = (float)i42;
							float f56 = (float)(i45 * b46);
							float f57 = f10001;
							EntityPlayer entityPlayer55 = entityRenderer39.mc.thePlayer;
							float f5 = entityRenderer39.mc.thePlayer.rotationPitch;
							float f6 = entityPlayer55.rotationYaw;
							entityPlayer55.rotationYaw = (float)((double)entityPlayer55.rotationYaw + (double)f57 * 0.15D);
							entityPlayer55.rotationPitch = (float)((double)entityPlayer55.rotationPitch - (double)f56 * 0.15D);
							if(entityPlayer55.rotationPitch < -90.0F) {
								entityPlayer55.rotationPitch = -90.0F;
							}

							if(entityPlayer55.rotationPitch > 90.0F) {
								entityPlayer55.rotationPitch = 90.0F;
							}

							entityPlayer55.prevRotationPitch += entityPlayer55.rotationPitch - f5;
							entityPlayer55.prevRotationYaw += entityPlayer55.rotationYaw - f6;
						}

						i42 = entityRenderer39.mc.displayWidth * 240 / entityRenderer39.mc.displayHeight;
						i45 = entityRenderer39.mc.displayHeight * 240 / entityRenderer39.mc.displayHeight;
						int i48 = Mouse.getX() * i42 / entityRenderer39.mc.displayWidth;
						i49 = i45 - Mouse.getY() * i45 / entityRenderer39.mc.displayHeight - 1;
						if(entityRenderer39.mc.theWorld != null) {
							entityRenderer39.updateCameraAndRender(f41);
							entityRenderer39.mc.ingameGUI.renderGameOverlay(f41);
						} else {
							GL11.glViewport(0, 0, entityRenderer39.mc.displayWidth, entityRenderer39.mc.displayHeight);
							GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
							GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
							GL11.glMatrixMode(GL11.GL_PROJECTION);
							GL11.glLoadIdentity();
							GL11.glMatrixMode(GL11.GL_MODELVIEW);
							GL11.glLoadIdentity();
							entityRenderer39.setupOverlayRendering();
						}

						if(entityRenderer39.mc.currentScreen != null) {
							entityRenderer39.mc.currentScreen.drawScreen(i48, i49);
						}

						Thread.yield();
						Display.update();
						if(this.options.limitFramerate) {
							Thread.sleep(5L);
						}

						checkGLError("Post render");
						++i3;
					} catch (Exception exception32) {
						this.displayGuiScreen(new GuiErrorScreen("Client error", "The game broke! [" + exception32 + "]"));
						exception32.printStackTrace();
					}

					while(System.currentTimeMillis() >= j1 + 1000L) {
						this.debug = i3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						j1 += 1000L;
						i3 = 0;
					}
				}
			}

			return;
		} catch (MinecraftError minecraftError33) {
		} catch (Exception exception34) {
			exception34.printStackTrace();
			return;
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public final void setIngameFocus() {
		if(!this.inventoryScreen) {
			this.inventoryScreen = true;
			if(this.appletMode) {
				try {
					Mouse.setNativeCursor(this.cursor);
					Mouse.setCursorPosition(this.displayWidth / 2, this.displayHeight / 2);
				} catch (LWJGLException lWJGLException1) {
					lWJGLException1.printStackTrace();
				}
			} else {
				Mouse.setGrabbed(true);
			}

			this.displayGuiScreen((GuiScreen)null);
			this.prevFrameTime = this.ticksRan + 10000;
		}
	}

	private void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void clickMouse(int i1) {
		if(i1 != 0 || this.leftClickCounter <= 0) {
			ItemRenderer itemRenderer5;
			if(i1 == 0) {
				itemRenderer5 = this.entityRenderer.itemRenderer;
				this.entityRenderer.itemRenderer.swingProgress = -1;
				itemRenderer5.itemSwingState = true;
			}

			int i2;
			if(i1 == 1 && (i2 = this.thePlayer.inventory.getCurrentItem()) > 0 && this.playerController.sendUseItem(this.thePlayer, i2)) {
				itemRenderer5 = null;
				this.entityRenderer.itemRenderer.equippedProgress = 0.0F;
			} else if(this.objectMouseOver == null) {
				if(i1 == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
					this.leftClickCounter = 10;
				}

			} else {
				if(this.objectMouseOver.typeOfHit == 1) {
					if(i1 == 0) {
						this.objectMouseOver.entityHit.attackEntityFrom(this.thePlayer, 4);
						return;
					}
				} else if(this.objectMouseOver.typeOfHit == 0) {
					i2 = this.objectMouseOver.blockX;
					int i3 = this.objectMouseOver.blockY;
					int i4 = this.objectMouseOver.blockZ;
					if(i1 != 0) {
						if(this.objectMouseOver.sideHit == 0) {
							--i3;
						}

						if(this.objectMouseOver.sideHit == 1) {
							++i3;
						}

						if(this.objectMouseOver.sideHit == 2) {
							--i4;
						}

						if(this.objectMouseOver.sideHit == 3) {
							++i4;
						}

						if(this.objectMouseOver.sideHit == 4) {
							--i2;
						}

						if(this.objectMouseOver.sideHit == 5) {
							++i2;
						}
					}

					Block block9 = Block.blocksList[this.theWorld.getBlockId(i2, i3, i4)];
					if(i1 == 0) {
						if(block9 != Block.bedrock) {
							this.playerController.clickBlock(i2, i3, i4);
							return;
						}
					} else {
						if((i1 = this.thePlayer.inventory.getCurrentItem()) <= 0) {
							return;
						}

						if((block9 = Block.blocksList[this.theWorld.getBlockId(i2, i3, i4)]) == null || block9 == Block.waterMoving || block9 == Block.waterStill || block9 == Block.lavaMoving || block9 == Block.lavaStill) {
							AxisAlignedBB axisAlignedBB10;
							if((axisAlignedBB10 = Block.blocksList[i1].getCollisionBoundingBoxFromPool(i2, i3, i4)) != null) {
								AxisAlignedBB axisAlignedBB7 = this.thePlayer.boundingBox;
								if(!((axisAlignedBB10.x1 > axisAlignedBB7.x0 && axisAlignedBB10.x0 < axisAlignedBB7.x1 ? (axisAlignedBB10.y1 > axisAlignedBB7.y0 && axisAlignedBB10.y0 < axisAlignedBB7.y1 ? axisAlignedBB10.z1 > axisAlignedBB7.z0 && axisAlignedBB10.z0 < axisAlignedBB7.z1 : false) : false) ? false : this.theWorld.checkIfAABBIsClear(axisAlignedBB10))) {
									return;
								}
							}

							if(!this.playerController.canPlace(i1)) {
								return;
							}

							this.theWorld.setBlockWithNotify(i2, i3, i4, i1);
							this.entityRenderer.itemRenderer.equippedProgress = 0.0F;
							Block.blocksList[i1].onBlockPlaced(this.theWorld, i2, i3, i4);
						}
					}
				}

			}
		}
	}

	private void runTick() {
		this.playerController.onUpdate();
		GuiIngame guiIngame1 = this.ingameGUI;
		++this.ingameGUI.updateCounter;

		int i2;
		for(i2 = 0; i2 < guiIngame1.chatMessageList.size(); ++i2) {
			++((ChatLine)guiIngame1.chatMessageList.get(i2)).updateCounter;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		RenderEngine renderEngine13 = this.renderEngine;

		TextureFX textureFX3;
		for(i2 = 0; i2 < renderEngine13.textureList.size(); ++i2) {
			(textureFX3 = (TextureFX)renderEngine13.textureList.get(i2)).anaglyphEnabled = renderEngine13.options.anaglyph;
			textureFX3.onTick();
			renderEngine13.imageData.clear();
			renderEngine13.imageData.put(textureFX3.imageData);
			renderEngine13.imageData.position(0).limit(textureFX3.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, textureFX3.iconIndex % 16 << 4, textureFX3.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, renderEngine13.imageData);
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		int i4;
		int i6;
		int i14;
		EffectRenderer effectRenderer16;
		int i21;
		int i24;
		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label351:
			while(true) {
				if(!Mouse.next()) {
					if(this.leftClickCounter > 0) {
						--this.leftClickCounter;
					}

					while(true) {
						do {
							do {
								if(!Keyboard.next()) {
									if(this.currentScreen == null) {
										if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(0);
											this.prevFrameTime = this.ticksRan;
										}

										if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(1);
											this.prevFrameTime = this.ticksRan;
										}
									}

									boolean z20 = this.currentScreen == null && Mouse.isButtonDown(0) && this.inventoryScreen;
									if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
										if(z20 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
											i4 = this.objectMouseOver.blockX;
											i24 = this.objectMouseOver.blockY;
											i6 = this.objectMouseOver.blockZ;
											this.playerController.sendBlockRemoving(i4, i24, i6);
											int i10001 = i4;
											int i10002 = i24;
											i24 = this.objectMouseOver.sideHit;
											i4 = i6;
											i21 = i10002;
											i2 = i10001;
											effectRenderer16 = this.effectRenderer;
											if((i6 = this.effectRenderer.worldObj.getBlockId(i2, i21, i6)) != 0) {
												Block block28 = Block.blocksList[i6];
												float f7 = 0.1F;
												float f8 = (float)i2 + effectRenderer16.rand.nextFloat() * (block28.maxX - block28.minX - f7 * 2.0F) + f7 + block28.minX;
												float f9 = (float)i21 + effectRenderer16.rand.nextFloat() * (block28.maxY - block28.minY - f7 * 2.0F) + f7 + block28.minY;
												float f10 = (float)i4 + effectRenderer16.rand.nextFloat() * (block28.maxZ - block28.minZ - f7 * 2.0F) + f7 + block28.minZ;
												if(i24 == 0) {
													f9 = (float)i21 + block28.minY - f7;
												}

												if(i24 == 1) {
													f9 = (float)i21 + block28.maxY + f7;
												}

												if(i24 == 2) {
													f10 = (float)i4 + block28.minZ - f7;
												}

												if(i24 == 3) {
													f10 = (float)i4 + block28.maxZ + f7;
												}

												if(i24 == 4) {
													f8 = (float)i2 + block28.minX - f7;
												}

												if(i24 == 5) {
													f8 = (float)i2 + block28.maxX + f7;
												}

												effectRenderer16.addEffect((new EntityDiggingFX(effectRenderer16.worldObj, f8, f9, f10, 0.0F, 0.0F, 0.0F, block28)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
											}
										} else {
											this.playerController.resetBlockRemoving();
										}
									}
									break label351;
								}

								this.thePlayer.checkKeyForMovementInput(Keyboard.getEventKey(), Keyboard.getEventKeyState());
							} while(!Keyboard.getEventKeyState());

							if(this.currentScreen != null) {
								this.currentScreen.handleKeyboardInput();
							}

							if(this.currentScreen == null) {
								if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
									this.displayInGameMenu();
								}

								if(this.playerController instanceof PlayerControllerCreative) {
									if(Keyboard.getEventKey() == this.options.keyBindLoad.keyCode) {
										this.thePlayer.preparePlayerToSpawn();
									}

									if(Keyboard.getEventKey() == this.options.keyBindSave.keyCode) {
										this.theWorld.setSpawnLocation((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ, this.thePlayer.rotationYaw);
										this.thePlayer.preparePlayerToSpawn();
									}
								}

								if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
									this.thirdPersonView = !this.thirdPersonView;
								}

								if(Keyboard.getEventKey() == this.options.keyBindInventory.keyCode) {
									this.playerController.displayInventoryGUI();
								}
							}

							for(i14 = 0; i14 < 9; ++i14) {
								if(Keyboard.getEventKey() == i14 + Keyboard.KEY_1) {
									this.thePlayer.inventory.currentItem = i14;
								}
							}
						} while(Keyboard.getEventKey() != this.options.keyBindToggleFog.keyCode);

						this.options.setOptionValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
					}
				}

				if((i14 = Mouse.getEventDWheel()) != 0) {
					i2 = i14;
					InventoryPlayer inventoryPlayer15 = this.thePlayer.inventory;
					if(i14 > 0) {
						i2 = 1;
					}

					if(i2 < 0) {
						i2 = -1;
					}

					for(inventoryPlayer15.currentItem -= i2; inventoryPlayer15.currentItem < 0; inventoryPlayer15.currentItem += inventoryPlayer15.mainInventory.length) {
					}

					while(inventoryPlayer15.currentItem >= inventoryPlayer15.mainInventory.length) {
						inventoryPlayer15.currentItem -= inventoryPlayer15.mainInventory.length;
					}
				}

				if(this.currentScreen == null) {
					if(!this.inventoryScreen && Mouse.getEventButtonState()) {
						this.setIngameFocus();
					} else {
						if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
							this.clickMouse(0);
							this.prevFrameTime = this.ticksRan;
						}

						if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
							this.clickMouse(1);
							this.prevFrameTime = this.ticksRan;
						}

						if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.objectMouseOver != null) {
							if((i2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ)) == Block.grass.blockID) {
								i2 = Block.dirt.blockID;
							}

							if(i2 == Block.stairDouble.blockID) {
								i2 = Block.stairSingle.blockID;
							}

							if(i2 == Block.bedrock.blockID) {
								i2 = Block.stone.blockID;
							}

							boolean z5 = this.playerController instanceof PlayerControllerCreative;
							InventoryPlayer inventoryPlayer18 = this.thePlayer.inventory;
							if((i6 = this.thePlayer.inventory.getInventorySlotContainItem(i2)) >= 0) {
								inventoryPlayer18.currentItem = i6;
							} else if(z5 && i2 > 0 && Session.allowedBlocks.contains(Block.blocksList[i2])) {
								inventoryPlayer18.replaceSlot(Block.blocksList[i2]);
							}
						}
					}
				}

				if(this.currentScreen != null) {
					this.currentScreen.handleMouseInput();
				}
			}
		}

		if(this.currentScreen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			GuiScreen guiScreen17 = this.currentScreen;

			while(Mouse.next()) {
				guiScreen17.handleMouseInput();
			}

			while(Keyboard.next()) {
				guiScreen17.handleKeyboardInput();
			}

			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
			EntityRenderer entityRenderer19 = this.entityRenderer;
			++this.entityRenderer.entityRendererInt1;
			ItemRenderer itemRenderer31 = entityRenderer19.itemRenderer;
			entityRenderer19.itemRenderer.prevEquippedProgress = entityRenderer19.itemRenderer.equippedProgress;
			if(itemRenderer31.itemSwingState) {
				++itemRenderer31.swingProgress;
				if(itemRenderer31.swingProgress == 7) {
					itemRenderer31.swingProgress = 0;
					itemRenderer31.itemSwingState = false;
				}
			}

			textureFX3 = null;
			i4 = itemRenderer31.minecraft.thePlayer.inventory.getCurrentItem();
			Block block26 = null;
			if(i4 > 0) {
				block26 = Block.blocksList[i4];
			}

			float f29 = 0.4F;
			float f10000 = block26 == itemRenderer31.itemToRender ? 1.0F : 0.0F;
			float f22 = 0.0F;
			if((f22 = f10000 - itemRenderer31.equippedProgress) < -f29) {
				f22 = -f29;
			}

			if(f22 > f29) {
				f22 = f29;
			}

			itemRenderer31.equippedProgress += f22;
			if(itemRenderer31.equippedProgress < 0.1F) {
				itemRenderer31.itemToRender = block26;
			}

			if(entityRenderer19.mc.thirdPersonView) {
				EntityRenderer entityRenderer32 = entityRenderer19;
				EntityPlayer entityPlayer27 = entityRenderer19.mc.thePlayer;
				World world23 = entityRenderer19.mc.theWorld;
				i24 = (int)entityPlayer27.posX;
				i6 = (int)entityPlayer27.posY;
				i21 = (int)entityPlayer27.posZ;

				for(i14 = 0; i14 < 50; ++i14) {
					int i30 = i24 + entityRenderer32.random.nextInt(9) - 4;
					int i33 = i21 + entityRenderer32.random.nextInt(9) - 4;
					int i34 = world23.getMapHeight(i30, i33);
					int i35 = world23.getBlockId(i30, i34 - 1, i33);
					if(i34 <= i6 + 4 && i34 >= i6 - 4) {
						float f11 = entityRenderer32.random.nextFloat();
						float f12 = entityRenderer32.random.nextFloat();
						if(i35 > 0) {
							entityRenderer32.mc.effectRenderer.addEffect(new EntityRainFX(world23, (float)i30 + f11, (float)i34 + 0.1F - Block.blocksList[i35].minY, (float)i33 + f12));
						}
					}
				}
			}

			guiIngame1 = null;
			++this.renderGlobal.cloudOffsetX;
			this.theWorld.updateEntities();
			this.theWorld.tick();
			effectRenderer16 = this.effectRenderer;

			for(i2 = 0; i2 < 2; ++i2) {
				for(i21 = 0; i21 < effectRenderer16.fxLayers[i2].size(); ++i21) {
					EntityFX entityFX25;
					(entityFX25 = (EntityFX)effectRenderer16.fxLayers[i2].get(i21)).onEntityUpdate();
					if(entityFX25.isDead) {
						effectRenderer16.fxLayers[i2].remove(i21--);
					}
				}
			}
		}

	}

	public final void generateLevel(int i1) {
		String string2 = this.session != null ? this.session.username : "anonymous";
		LevelGenerator levelGenerator10000 = new LevelGenerator(this.loadingScreen);
		int i10002 = 128 << i1;
		int i10003 = 128 << i1;
		boolean z5 = true;
		int i4 = i10003;
		int i3 = i10002;
		LevelGenerator levelGenerator31 = levelGenerator10000;
		String string7 = "Generating level";
		LoadingScreenRenderer loadingScreenRenderer6 = levelGenerator10000.progressBar;
		if(!levelGenerator10000.progressBar.minecraft.running) {
			throw new MinecraftError();
		} else {
			loadingScreenRenderer6.title = string7;
			int i8 = loadingScreenRenderer6.minecraft.displayWidth * 240 / loadingScreenRenderer6.minecraft.displayHeight;
			int i9 = loadingScreenRenderer6.minecraft.displayHeight * 240 / loadingScreenRenderer6.minecraft.displayHeight;
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)i8, (double)i9, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
			levelGenerator31.width = i3;
			levelGenerator31.depth = i4;
			levelGenerator31.height = 64;
			levelGenerator31.waterLevel = 32;
			levelGenerator31.blocksByteArray = new byte[i3 * i4 << 6];
			levelGenerator31.progressBar.displayProgressMessage("Raising..");
			LevelGenerator levelGenerator35 = levelGenerator31;
			NoiseGeneratorDistort noiseGeneratorDistort36 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(levelGenerator31.rand, 8), new NoiseGeneratorOctaves(levelGenerator31.rand, 8));
			NoiseGeneratorDistort noiseGeneratorDistort38 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(levelGenerator31.rand, 8), new NoiseGeneratorOctaves(levelGenerator31.rand, 8));
			NoiseGeneratorOctaves noiseGeneratorOctaves41 = new NoiseGeneratorOctaves(levelGenerator31.rand, 6);
			int[] i10 = new int[levelGenerator31.width * levelGenerator31.depth];
			float f11 = 1.3F;

			int i12;
			int i17;
			for(i17 = 0; i17 < levelGenerator35.width; ++i17) {
				levelGenerator35.setNextPhase(i17 * 100 / (levelGenerator35.width - 1));

				for(i12 = 0; i12 < levelGenerator35.depth; ++i12) {
					double d19 = noiseGeneratorDistort36.generateNoise((double)((float)i17 * f11), (double)((float)i12 * f11)) / 6.0D + (double)-4;
					double d21 = noiseGeneratorDistort38.generateNoise((double)((float)i17 * f11), (double)((float)i12 * f11)) / 5.0D + 10.0D + (double)-4;
					double d67 = noiseGeneratorOctaves41.generateNoise((double)i17, (double)i12) / 8.0D;
					double d23 = 0.0D;
					if(d67 > 0.0D) {
						d21 = d19;
					}

					double d25;
					if((d25 = Math.max(d19, d21) / 2.0D) < 0.0D) {
						d25 *= 0.8D;
					}

					i10[i17 + i12 * levelGenerator35.width] = (int)d25;
				}
			}

			levelGenerator31.progressBar.displayProgressMessage("Eroding..");
			int[] i37 = i10;
			levelGenerator35 = levelGenerator31;
			noiseGeneratorDistort38 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(levelGenerator31.rand, 8), new NoiseGeneratorOctaves(levelGenerator31.rand, 8));
			NoiseGeneratorDistort noiseGeneratorDistort43 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(levelGenerator31.rand, 8), new NoiseGeneratorOctaves(levelGenerator31.rand, 8));

			int i45;
			int i47;
			boolean z52;
			int i53;
			for(i45 = 0; i45 < levelGenerator35.width; ++i45) {
				levelGenerator35.setNextPhase(i45 * 100 / (levelGenerator35.width - 1));

				for(i47 = 0; i47 < levelGenerator35.depth; ++i47) {
					double d16 = noiseGeneratorDistort38.generateNoise((double)(i45 << 1), (double)(i47 << 1)) / 8.0D;
					i12 = noiseGeneratorDistort43.generateNoise((double)(i45 << 1), (double)(i47 << 1)) > 0.0D ? 1 : 0;
					if(d16 > 2.0D) {
						int i68 = i37[i45 + i47 * levelGenerator35.width];
						z52 = false;
						i53 = ((i68 - i12) / 2 << 1) + i12;
						i37[i45 + i47 * levelGenerator35.width] = i53;
					}
				}
			}

			levelGenerator31.progressBar.displayProgressMessage("Soiling..");
			i37 = i10;
			levelGenerator35 = levelGenerator31;
			i8 = levelGenerator31.width;
			i9 = levelGenerator31.depth;
			i45 = levelGenerator31.height;
			NoiseGeneratorOctaves noiseGeneratorOctaves48 = new NoiseGeneratorOctaves(levelGenerator31.rand, 8);

			int i20;
			int i22;
			int i50;
			int i57;
			int i59;
			for(i50 = 0; i50 < i8; ++i50) {
				levelGenerator35.setNextPhase(i50 * 100 / (levelGenerator35.width - 1));

				for(i17 = 0; i17 < i9; ++i17) {
					i12 = (int)(noiseGeneratorOctaves48.generateNoise((double)i50, (double)i17) / 24.0D) - 4;
					i20 = (i53 = i37[i50 + i17 * i8] + levelGenerator35.waterLevel) + i12;
					i37[i50 + i17 * i8] = Math.max(i53, i20);
					if(i37[i50 + i17 * i8] > i45 - 2) {
						i37[i50 + i17 * i8] = i45 - 2;
					}

					if(i37[i50 + i17 * i8] < 1) {
						i37[i50 + i17 * i8] = 1;
					}

					for(i57 = 0; i57 < i45; ++i57) {
						i22 = (i57 * levelGenerator35.depth + i17) * levelGenerator35.width + i50;
						i59 = 0;
						if(i57 <= i53) {
							i59 = Block.dirt.blockID;
						}

						if(i57 <= i20) {
							i59 = Block.stone.blockID;
						}

						if(i57 == 0) {
							i59 = Block.lavaMoving.blockID;
						}

						levelGenerator35.blocksByteArray[i22] = (byte)i59;
					}
				}
			}

			levelGenerator31.progressBar.displayProgressMessage("Carving..");
			boolean z39 = false;
			levelGenerator35 = levelGenerator31;
			i9 = levelGenerator31.width;
			i45 = levelGenerator31.depth;
			i47 = levelGenerator31.height;
			i50 = i9 * i45 * i47 / 256 / 64 << 1;

			for(i17 = 0; i17 < i50; ++i17) {
				levelGenerator35.setNextPhase(i17 * 100 / (i50 - 1) / 4);
				float f49 = levelGenerator35.rand.nextFloat() * (float)i9;
				float f54 = levelGenerator35.rand.nextFloat() * (float)i47;
				float f55 = levelGenerator35.rand.nextFloat() * (float)i45;
				i57 = (int)((levelGenerator35.rand.nextFloat() + levelGenerator35.rand.nextFloat()) * 200.0F);
				float f58 = levelGenerator35.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f61 = 0.0F;
				float f24 = levelGenerator35.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f62 = 0.0F;
				float f26 = levelGenerator35.rand.nextFloat() * levelGenerator35.rand.nextFloat();

				for(int i33 = 0; i33 < i57; ++i33) {
					f49 += MathHelper.sin(f58) * MathHelper.cos(f24);
					f55 += MathHelper.cos(f58) * MathHelper.cos(f24);
					f54 += MathHelper.sin(f24);
					f58 += f61 * 0.2F;
					f61 = f61 * 0.9F + (levelGenerator35.rand.nextFloat() - levelGenerator35.rand.nextFloat());
					f24 = (f24 + f62 * 0.5F) * 0.5F;
					f62 = f62 * 0.75F + (levelGenerator35.rand.nextFloat() - levelGenerator35.rand.nextFloat());
					if(levelGenerator35.rand.nextFloat() >= 0.25F) {
						float f40 = f49 + (levelGenerator35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float f42 = f54 + (levelGenerator35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float f13 = f55 + (levelGenerator35.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
						float f14 = ((float)levelGenerator35.height - f42) / (float)levelGenerator35.height;
						f14 = 1.2F + (f14 * 3.5F + 1.0F) * f26;
						f14 = MathHelper.sin((float)i33 * (float)Math.PI / (float)i57) * f14;

						for(int i15 = (int)(f40 - f14); i15 <= (int)(f40 + f14); ++i15) {
							for(int i18 = (int)(f42 - f14); i18 <= (int)(f42 + f14); ++i18) {
								for(int i27 = (int)(f13 - f14); i27 <= (int)(f13 + f14); ++i27) {
									float f28 = (float)i15 - f40;
									float f29 = (float)i18 - f42;
									float f30 = (float)i27 - f13;
									if(f28 * f28 + f29 * f29 * 2.0F + f30 * f30 < f14 * f14 && i15 >= 1 && i18 >= 1 && i27 >= 1 && i15 < levelGenerator35.width - 1 && i18 < levelGenerator35.height - 1 && i27 < levelGenerator35.depth - 1) {
										int i65 = (i18 * levelGenerator35.depth + i27) * levelGenerator35.width + i15;
										if(levelGenerator35.blocksByteArray[i65] == Block.stone.blockID) {
											levelGenerator35.blocksByteArray[i65] = 0;
										}
									}
								}
							}
						}
					}
				}
			}

			levelGenerator31.populateOre(Block.oreCoal.blockID, 90, 1, 4);
			levelGenerator31.populateOre(Block.oreIron.blockID, 70, 2, 4);
			levelGenerator31.populateOre(Block.oreGold.blockID, 50, 3, 4);
			levelGenerator31.progressBar.displayProgressMessage("Watering..");
			levelGenerator35 = levelGenerator31;
			i45 = Block.waterStill.blockID;
			levelGenerator31.setNextPhase(0);

			for(i47 = 0; i47 < levelGenerator35.width; ++i47) {
				levelGenerator35.floodFill(i47, levelGenerator35.height / 2 - 1, 0, 0, i45);
				levelGenerator35.floodFill(i47, levelGenerator35.height / 2 - 1, levelGenerator35.depth - 1, 0, i45);
			}

			for(i47 = 0; i47 < levelGenerator35.depth; ++i47) {
				levelGenerator35.floodFill(0, levelGenerator35.height / 2 - 1, i47, 0, i45);
				levelGenerator35.floodFill(levelGenerator35.width - 1, levelGenerator35.height / 2 - 1, i47, 0, i45);
			}

			i47 = levelGenerator35.width * levelGenerator35.depth / 8000;

			for(i50 = 0; i50 < i47; ++i50) {
				if(i50 % 100 == 0) {
					levelGenerator35.setNextPhase(i50 * 100 / (i47 - 1));
				}

				i17 = levelGenerator35.rand.nextInt(levelGenerator35.width);
				i12 = levelGenerator35.waterLevel - 1 - levelGenerator35.rand.nextInt(2);
				i53 = levelGenerator35.rand.nextInt(levelGenerator35.depth);
				if(levelGenerator35.blocksByteArray[(i12 * levelGenerator35.depth + i53) * levelGenerator35.width + i17] == 0) {
					levelGenerator35.floodFill(i17, i12, i53, 0, i45);
				}
			}

			levelGenerator35.setNextPhase(100);
			levelGenerator31.progressBar.displayProgressMessage("Melting..");
			levelGenerator35 = levelGenerator31;
			i8 = levelGenerator31.width * levelGenerator31.depth * levelGenerator31.height / 20000;

			for(i9 = 0; i9 < i8; ++i9) {
				if(i9 % 100 == 0) {
					levelGenerator35.setNextPhase(i9 * 100 / (i8 - 1));
				}

				i45 = levelGenerator35.rand.nextInt(levelGenerator35.width);
				i47 = (int)(levelGenerator35.rand.nextFloat() * levelGenerator35.rand.nextFloat() * (float)(levelGenerator35.waterLevel - 3));
				i50 = levelGenerator35.rand.nextInt(levelGenerator35.depth);
				if(levelGenerator35.blocksByteArray[(i47 * levelGenerator35.depth + i50) * levelGenerator35.width + i45] == 0) {
					levelGenerator35.floodFill(i45, i47, i50, 0, Block.lavaStill.blockID);
				}
			}

			levelGenerator35.setNextPhase(100);
			levelGenerator31.progressBar.displayProgressMessage("Growing..");
			i37 = i10;
			levelGenerator35 = levelGenerator31;
			i8 = levelGenerator31.width;
			i9 = levelGenerator31.depth;
			i45 = levelGenerator31.height;
			noiseGeneratorOctaves48 = new NoiseGeneratorOctaves(levelGenerator31.rand, 8);
			NoiseGeneratorOctaves noiseGeneratorOctaves51 = new NoiseGeneratorOctaves(levelGenerator31.rand, 8);

			int i60;
			for(i17 = 0; i17 < i8; ++i17) {
				levelGenerator35.setNextPhase(i17 * 100 / (levelGenerator35.width - 1));

				for(i12 = 0; i12 < i9; ++i12) {
					z52 = noiseGeneratorOctaves48.generateNoise((double)i17, (double)i12) > 8.0D;
					boolean z56 = noiseGeneratorOctaves51.generateNoise((double)i17, (double)i12) > 12.0D;
					i22 = ((i57 = i37[i17 + i12 * i8]) * levelGenerator35.depth + i12) * levelGenerator35.width + i17;
					if(((i59 = levelGenerator35.blocksByteArray[((i57 + 1) * levelGenerator35.depth + i12) * levelGenerator35.width + i17] & 255) == Block.waterMoving.blockID || i59 == Block.waterStill.blockID) && i57 <= i45 / 2 - 1 && z56) {
						levelGenerator35.blocksByteArray[i22] = (byte)Block.gravel.blockID;
					}

					if(i59 == 0) {
						i60 = Block.grass.blockID;
						if(i57 <= i45 / 2 - 1 && z52) {
							i60 = Block.sand.blockID;
						}

						levelGenerator35.blocksByteArray[i22] = (byte)i60;
					}
				}
			}

			levelGenerator31.progressBar.displayProgressMessage("Planting..");
			i37 = i10;
			levelGenerator35 = levelGenerator31;
			i8 = levelGenerator31.width;
			i9 = levelGenerator31.width * levelGenerator31.depth / 3000;

			for(i45 = 0; i45 < i9; ++i45) {
				i47 = levelGenerator35.rand.nextInt(2);
				levelGenerator35.setNextPhase(i45 * 50 / (i9 - 1));
				i50 = levelGenerator35.rand.nextInt(levelGenerator35.width);
				i17 = levelGenerator35.rand.nextInt(levelGenerator35.depth);

				for(i12 = 0; i12 < 10; ++i12) {
					i53 = i50;
					i20 = i17;

					for(i57 = 0; i57 < 5; ++i57) {
						i53 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						i20 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						if((i47 < 2 || levelGenerator35.rand.nextInt(4) == 0) && i53 >= 0 && i20 >= 0 && i53 < levelGenerator35.width && i20 < levelGenerator35.depth) {
							i22 = i37[i53 + i20 * i8] + 1;
							boolean z69 = (levelGenerator35.blocksByteArray[(i22 * levelGenerator35.depth + i20) * levelGenerator35.width + i53] & 255) == 0;
							boolean z63 = false;
							if(z69) {
								i60 = (i22 * levelGenerator35.depth + i20) * levelGenerator35.width + i53;
								if((levelGenerator35.blocksByteArray[((i22 - 1) * levelGenerator35.depth + i20) * levelGenerator35.width + i53] & 255) == Block.grass.blockID) {
									if(i47 == 0) {
										levelGenerator35.blocksByteArray[i60] = (byte)Block.plantYellow.blockID;
									} else if(i47 == 1) {
										levelGenerator35.blocksByteArray[i60] = (byte)Block.plantRed.blockID;
									}
								}
							}
						}
					}
				}
			}

			i37 = i10;
			levelGenerator35 = levelGenerator31;
			i8 = levelGenerator31.width;
			i45 = levelGenerator31.width * levelGenerator31.depth * levelGenerator31.height / 2000;

			for(i47 = 0; i47 < i45; ++i47) {
				i50 = levelGenerator35.rand.nextInt(2);
				levelGenerator35.setNextPhase(i47 * 50 / (i45 - 1) + 50);
				i17 = levelGenerator35.rand.nextInt(levelGenerator35.width);
				i12 = levelGenerator35.rand.nextInt(levelGenerator35.height);
				i53 = levelGenerator35.rand.nextInt(levelGenerator35.depth);

				for(i20 = 0; i20 < 20; ++i20) {
					i57 = i17;
					i22 = i12;
					i59 = i53;

					for(i60 = 0; i60 < 5; ++i60) {
						i57 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						i22 += levelGenerator35.rand.nextInt(2) - levelGenerator35.rand.nextInt(2);
						i59 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						if((i50 < 2 || levelGenerator35.rand.nextInt(4) == 0) && i57 >= 0 && i59 >= 0 && i22 >= 1 && i57 < levelGenerator35.width && i59 < levelGenerator35.depth && i22 < i37[i57 + i59 * i8] - 1 && (levelGenerator35.blocksByteArray[(i22 * levelGenerator35.depth + i59) * levelGenerator35.width + i57] & 255) == 0) {
							int i64 = (i22 * levelGenerator35.depth + i59) * levelGenerator35.width + i57;
							if((levelGenerator35.blocksByteArray[((i22 - 1) * levelGenerator35.depth + i59) * levelGenerator35.width + i57] & 255) == Block.stone.blockID) {
								if(i50 == 0) {
									levelGenerator35.blocksByteArray[i64] = (byte)Block.mushroomBrown.blockID;
								} else if(i50 == 1) {
									levelGenerator35.blocksByteArray[i64] = (byte)Block.mushroomRed.blockID;
								}
							}
						}
					}
				}
			}

			World world34;
			(world34 = new World()).waterLevel = levelGenerator31.waterLevel;
			world34.generate(i3, 64, i4, levelGenerator31.blocksByteArray);
			System.currentTimeMillis();
			int[] i46 = i10;
			World world44 = world34;
			levelGenerator35 = levelGenerator31;
			i9 = levelGenerator31.width;
			i45 = levelGenerator31.width * levelGenerator31.depth / 4000;

			for(i47 = 0; i47 < i45; ++i47) {
				levelGenerator35.setNextPhase(i47 * 50 / (i45 - 1) + 50);
				i50 = levelGenerator35.rand.nextInt(levelGenerator35.width);
				i17 = levelGenerator35.rand.nextInt(levelGenerator35.depth);

				for(i12 = 0; i12 < 20; ++i12) {
					i53 = i50;
					i20 = i17;

					for(i57 = 0; i57 < 20; ++i57) {
						i53 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						i20 += levelGenerator35.rand.nextInt(6) - levelGenerator35.rand.nextInt(6);
						if(i53 >= 0 && i20 >= 0 && i53 < levelGenerator35.width && i20 < levelGenerator35.depth) {
							i22 = i46[i53 + i20 * i9] + 1;
							if(levelGenerator35.rand.nextInt(4) == 0) {
								world44.growTrees(i53, i22, i20);
							}
						}
					}
				}
			}

			this.setLevel(world34);
		}
	}

	private void setLevel(World world1) {
		this.theWorld = world1;
		if(world1 != null) {
			world1.load();
			this.playerController.onWorldChange(world1);
			this.thePlayer = (EntityPlayer)world1.findSubclassOf(EntityPlayer.class);
		}

		if(this.thePlayer == null) {
			this.thePlayer = new EntityPlayer(world1);
			this.thePlayer.preparePlayerToSpawn();
			this.playerController.preparePlayer(this.thePlayer);
			if(world1 != null) {
				world1.playerEntity = this.thePlayer;
			}
		}

		if(this.thePlayer != null) {
			this.thePlayer.movementInput = new MovementInputFromKeys(this.options);
			this.playerController.flipPlayer(this.thePlayer);
		}

		if(this.renderGlobal != null) {
			RenderGlobal renderGlobal2 = this.renderGlobal;
			if(this.renderGlobal.worldObj != null) {
				renderGlobal2.worldObj.removeRenderer(renderGlobal2);
			}

			renderGlobal2.renderManager.worldObj = world1;
			renderGlobal2.worldObj = world1;
			renderGlobal2.globalRenderBlocks = new RenderBlocks(Tessellator.instance, world1);
			if(world1 != null) {
				world1.addRenderer(renderGlobal2);
				renderGlobal2.loadRenderers();
			}
		}

		if(this.effectRenderer != null) {
			EffectRenderer effectRenderer5;
			(effectRenderer5 = this.effectRenderer).worldObj = world1;

			for(int i6 = 0; i6 < 2; ++i6) {
				effectRenderer5.fxLayers[i6].clear();
			}
		}

		System.gc();
	}
}