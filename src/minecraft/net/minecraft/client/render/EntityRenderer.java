package net.minecraft.client.render;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityFX;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.MathHelper;

public final class EntityRenderer {
	public Minecraft mc;
	private float fogColorMultiplier = 1.0F;
	public boolean displayActive = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	public int entityRendererInt1;
	private Entity pointedEntity = null;
	public Random random = new Random();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;

	public EntityRenderer(Minecraft minecraft1) {
		this.mc = minecraft1;
		this.itemRenderer = new ItemRenderer(minecraft1);
	}

	private Vec3D orientCamera(float f1) {
		EntityPlayer entityPlayer4;
		float f2 = (entityPlayer4 = this.mc.thePlayer).prevPosX + (entityPlayer4.posX - entityPlayer4.prevPosX) * f1;
		float f3 = entityPlayer4.prevPosY + (entityPlayer4.posY - entityPlayer4.prevPosY) * f1;
		float f5 = entityPlayer4.prevPosZ + (entityPlayer4.posZ - entityPlayer4.prevPosZ) * f1;
		return new Vec3D(f2, f3, f5);
	}

	private void hurtCameraEffect(float f1) {
		EntityPlayer entityPlayer3;
		float f2 = (float)(entityPlayer3 = this.mc.thePlayer).hurtTime - f1;
		if(entityPlayer3.health <= 0) {
			f1 += (float)entityPlayer3.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f2 >= 0.0F) {
			float f10000 = f2 /= (float)entityPlayer3.maxHurtTime;
			f2 = MathHelper.sin(f10000 * f10000 * f2 * f2 * (float)Math.PI);
			f1 = entityPlayer3.attackedAtYaw;
			GL11.glRotatef(-entityPlayer3.attackedAtYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f2 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float f1) {
		EntityPlayer entityPlayer4;
		float f2 = (entityPlayer4 = this.mc.thePlayer).distanceWalkedModified - entityPlayer4.prevDistanceWalkedModified;
		f2 = entityPlayer4.distanceWalkedModified + f2 * f1;
		float f3 = entityPlayer4.prevCameraYaw + (entityPlayer4.cameraYaw - entityPlayer4.prevCameraYaw) * f1;
		float f5 = entityPlayer4.prevCameraPitch + (entityPlayer4.cameraPitch - entityPlayer4.prevCameraPitch) * f1;
		GL11.glTranslatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * (float)Math.PI) * f3), 0.0F);
		GL11.glRotatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(Math.abs(MathHelper.cos(f2 * (float)Math.PI + 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f5, 1.0F, 0.0F, 0.0F);
	}

	public final void updateCameraAndRender(float f1) {
		EntityRenderer entityRenderer15 = this;
		EntityPlayer entityPlayer17;
		float f18 = (entityPlayer17 = this.mc.thePlayer).prevRotationPitch + (entityPlayer17.rotationPitch - entityPlayer17.prevRotationPitch) * f1;
		float f19 = entityPlayer17.prevRotationYaw + (entityPlayer17.rotationYaw - entityPlayer17.prevRotationYaw) * f1;
		Vec3D vec3D8 = this.orientCamera(f1);
		float f7 = MathHelper.cos(-f19 * 0.017453292F - (float)Math.PI);
		float f9 = MathHelper.sin(-f19 * 0.017453292F - (float)Math.PI);
		float f10 = MathHelper.cos(-f18 * 0.017453292F);
		float f11 = MathHelper.sin(-f18 * 0.017453292F);
		float f12 = f9 * f10;
		float f13 = f7 * f10;
		float f14 = this.mc.playerController.getBlockReachDistance();
		Vec3D vec3D20 = vec3D8.addVector(f12 * f14, f11 * f14, f13 * f14);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(vec3D8, vec3D20);
		float f21 = f14;
		if(this.mc.objectMouseOver != null) {
			f21 = this.mc.objectMouseOver.hitVec.distanceTo(vec3D8);
		}

		vec3D8 = this.orientCamera(f1);
		if(this.mc.playerController instanceof PlayerControllerCreative) {
			f14 = 32.0F;
		} else {
			f14 = f21;
		}

		vec3D20 = vec3D8.addVector(f12 * f14, f11 * f14, f13 * f14);
		this.pointedEntity = null;
		List list22 = this.mc.theWorld.entityMap.getEntitiesWithinAABBExcludingEntity(entityPlayer17, entityPlayer17.boundingBox.addCoord(f12 * f14, f11 * f14, f13 * f14));
		float f23 = 0.0F;

		Entity entity70;
		float f109;
		for(int i24 = 0; i24 < list22.size(); ++i24) {
			if((entity70 = (Entity)list22.get(i24)).canBeCollidedWith()) {
				f21 = 0.1F;
				AxisAlignedBB axisAlignedBB10000 = entity70.boundingBox.expand(f21, f21, f21);
				Vec3D vec3D25 = null;
				AxisAlignedBB axisAlignedBB28 = axisAlignedBB10000;
				Vec3D vec3D93 = vec3D8.getIntermediateWithXValue(vec3D20, axisAlignedBB28.x0);
				Vec3D vec3D26 = vec3D8.getIntermediateWithXValue(vec3D20, axisAlignedBB28.x1);
				vec3D25 = vec3D8.getIntermediateWithYValue(vec3D20, axisAlignedBB28.y0);
				Vec3D vec3D27 = vec3D8.getIntermediateWithYValue(vec3D20, axisAlignedBB28.y1);
				Vec3D vec3D31 = vec3D8.getIntermediateWithZValue(vec3D20, axisAlignedBB28.z0);
				Vec3D vec3D30 = vec3D8.getIntermediateWithZValue(vec3D20, axisAlignedBB28.z1);
				if(!axisAlignedBB28.isVecInYZ(vec3D93)) {
					vec3D93 = null;
				}

				if(!axisAlignedBB28.isVecInYZ(vec3D26)) {
					vec3D26 = null;
				}

				if(!axisAlignedBB28.isVecInXZ(vec3D25)) {
					vec3D25 = null;
				}

				if(!axisAlignedBB28.isVecInXZ(vec3D27)) {
					vec3D27 = null;
				}

				if(!axisAlignedBB28.isVecInXY(vec3D31)) {
					vec3D31 = null;
				}

				if(!axisAlignedBB28.isVecInXY(vec3D30)) {
					vec3D30 = null;
				}

				Vec3D vec3D111 = null;
				if(vec3D93 != null) {
					vec3D111 = vec3D93;
				}

				if(vec3D26 != null && (vec3D111 == null || vec3D8.squaredDistanceTo(vec3D26) < vec3D8.squaredDistanceTo(vec3D111))) {
					vec3D111 = vec3D26;
				}

				if(vec3D25 != null && (vec3D111 == null || vec3D8.squaredDistanceTo(vec3D25) < vec3D8.squaredDistanceTo(vec3D111))) {
					vec3D111 = vec3D25;
				}

				if(vec3D27 != null && (vec3D111 == null || vec3D8.squaredDistanceTo(vec3D27) < vec3D8.squaredDistanceTo(vec3D111))) {
					vec3D111 = vec3D27;
				}

				if(vec3D31 != null && (vec3D111 == null || vec3D8.squaredDistanceTo(vec3D31) < vec3D8.squaredDistanceTo(vec3D111))) {
					vec3D111 = vec3D31;
				}

				if(vec3D30 != null && (vec3D111 == null || vec3D8.squaredDistanceTo(vec3D30) < vec3D8.squaredDistanceTo(vec3D111))) {
					vec3D111 = vec3D30;
				}

				MovingObjectPosition movingObjectPosition123;
				if(vec3D111 == null) {
					movingObjectPosition123 = null;
				} else {
					byte b32 = -1;
					if(vec3D111 == vec3D93) {
						b32 = 4;
					}

					if(vec3D111 == vec3D26) {
						b32 = 5;
					}

					if(vec3D111 == vec3D25) {
						b32 = 0;
					}

					if(vec3D111 == vec3D27) {
						b32 = 1;
					}

					if(vec3D111 == vec3D31) {
						b32 = 2;
					}

					if(vec3D111 == vec3D30) {
						b32 = 3;
					}

					movingObjectPosition123 = new MovingObjectPosition(0, 0, 0, b32, vec3D111);
				}

				MovingObjectPosition movingObjectPosition106 = movingObjectPosition123;
				if(movingObjectPosition123 != null && ((f109 = vec3D8.distanceTo(movingObjectPosition106.hitVec)) < f23 || f23 == 0.0F)) {
					entityRenderer15.pointedEntity = entity70;
					f23 = f109;
				}
			}
		}

		if(entityRenderer15.pointedEntity != null && !(entityRenderer15.mc.playerController instanceof PlayerControllerCreative)) {
			entityRenderer15.mc.objectMouseOver = new MovingObjectPosition(entityRenderer15.pointedEntity);
		}

		for(int i2 = 0; i2 < 2; ++i2) {
			if(this.mc.options.anaglyph) {
				if(i2 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayer entityPlayer3 = this.mc.thePlayer;
			World world4 = this.mc.theWorld;
			RenderGlobal renderGlobal5 = this.mc.renderGlobal;
			EffectRenderer effectRenderer6 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			World world16 = this.mc.theWorld;
			entityPlayer17 = this.mc.thePlayer;
			f18 = 1.0F / (float)(4 - this.mc.options.renderDistance);
			f18 = 1.0F - (float)Math.pow((double)f18, 0.25D);
			f19 = (float)(world16.skyColor >> 16 & 255) / 255.0F;
			float f52 = (float)(world16.skyColor >> 8 & 255) / 255.0F;
			f7 = (float)(world16.skyColor & 255) / 255.0F;
			this.fogColorRed = (float)(world16.fogColor >> 16 & 255) / 255.0F;
			this.fogColorGreen = (float)(world16.fogColor >> 8 & 255) / 255.0F;
			this.fogColorBlue = (float)(world16.fogColor & 255) / 255.0F;
			this.fogColorRed += (f19 - this.fogColorRed) * f18;
			this.fogColorGreen += (f52 - this.fogColorGreen) * f18;
			this.fogColorBlue += (f7 - this.fogColorBlue) * f18;
			this.fogColorRed *= this.fogColorMultiplier;
			this.fogColorGreen *= this.fogColorMultiplier;
			this.fogColorBlue *= this.fogColorMultiplier;
			Block block54;
			if((block54 = Block.blocksList[world16.getBlockId((int)entityPlayer17.posX, (int)(entityPlayer17.posY + 0.12F), (int)entityPlayer17.posZ)]) != null && block54.getMaterial() != Material.air) {
				Material material57;
				if((material57 = block54.getMaterial()) == Material.water) {
					this.fogColorRed = 0.02F;
					this.fogColorGreen = 0.02F;
					this.fogColorBlue = 0.2F;
				} else if(material57 == Material.lava) {
					this.fogColorRed = 0.6F;
					this.fogColorGreen = 0.1F;
					this.fogColorBlue = 0.0F;
				}
			}

			if(this.mc.options.anaglyph) {
				f10 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
				f11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
				f12 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
				this.fogColorRed = f10;
				this.fogColorGreen = f11;
				this.fogColorBlue = f12;
			}

			GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			this.fogColorMultiplier = 1.0F;
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			f18 = 0.07F;
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)(-((i2 << 1) - 1)) * f18, 0.0F, 0.0F);
			}

			EntityMap entityMap86 = null;
			EntityPlayer entityPlayer50 = this.mc.thePlayer;
			f9 = 70.0F;
			if(entityPlayer50.health <= 0) {
				f10 = (float)entityPlayer50.deathTime + f1;
				f9 /= (1.0F - 500.0F / (f10 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(f9, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((i2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(f1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(f1);
			}

			entityPlayer50 = this.mc.thePlayer;
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			GL11.glRotatef(entityPlayer50.prevRotationPitch + (entityPlayer50.rotationPitch - entityPlayer50.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityPlayer50.prevRotationYaw + (entityPlayer50.rotationYaw - entityPlayer50.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
			f9 = entityPlayer50.prevPosX + (entityPlayer50.posX - entityPlayer50.prevPosX) * f1;
			f10 = entityPlayer50.prevPosY + (entityPlayer50.posY - entityPlayer50.prevPosY) * f1;
			f11 = entityPlayer50.prevPosZ + (entityPlayer50.posZ - entityPlayer50.prevPosZ) * f1;
			GL11.glTranslatef(-f9, -f10, -f11);
			ClippingHelper clippingHelper51 = ClippingHelperImplementation.init();
			ClippingHelper clippingHelper74 = clippingHelper51;
			RenderGlobal renderGlobal71 = this.mc.renderGlobal;

			int i81;
			for(i81 = 0; i81 < renderGlobal71.worldRenderers.length; ++i81) {
				renderGlobal71.worldRenderers[i81].updateInFrustrum(clippingHelper74);
			}

			renderGlobal71 = this.mc.renderGlobal;
			Collections.sort(this.mc.renderGlobal.worldRenderersToUpdate, new RenderSorter(entityPlayer3));
			i81 = renderGlobal71.worldRenderersToUpdate.size() - 1;
			int i85;
			if((i85 = renderGlobal71.worldRenderersToUpdate.size()) > 3) {
				i85 = 3;
			}

			int i87;
			for(i87 = 0; i87 < i85; ++i87) {
				WorldRenderer worldRenderer53;
				(worldRenderer53 = (WorldRenderer)renderGlobal71.worldRenderersToUpdate.remove(i81 - i87)).updateRenderer();
				worldRenderer53.needsUpdate = false;
			}

			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			renderGlobal5.sortAndRender(entityPlayer3, 0);
			int i55;
			int i60;
			int i63;
			int i65;
			int i72;
			if(world4.isSolid(entityPlayer3.posX, entityPlayer3.posY, entityPlayer3.posZ, 0.1F)) {
				i55 = (int)entityPlayer3.posX;
				int i58 = (int)entityPlayer3.posY;
				i60 = (int)entityPlayer3.posZ;
				RenderBlocks renderBlocks62 = new RenderBlocks(Tessellator.instance, world4);

				for(i63 = i55 - 1; i63 <= i55 + 1; ++i63) {
					for(i65 = i58 - 1; i65 <= i58 + 1; ++i65) {
						for(i72 = i60 - 1; i72 <= i60 + 1; ++i72) {
							int i73;
							if((i73 = world4.getBlockId(i63, i65, i72)) > 0) {
								Block block76 = Block.blocksList[i73];
								renderBlocks62.flipTexture = true;
								renderBlocks62.renderBlockByRenderType(block76, i63, i65, i72);
								renderBlocks62.flipTexture = false;
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			Vec3D vec3D10001 = this.orientCamera(f1);
			f18 = f1;
			ClippingHelper clippingHelper83 = clippingHelper51;
			Vec3D vec3D78 = vec3D10001;
			renderGlobal71 = renderGlobal5;
			entityMap86 = renderGlobal5.worldObj.entityMap;

			int i48;
			List list68;
			float f89;
			float f104;
			float f125;
			for(i55 = 0; i55 < entityMap86.xSlot; ++i55) {
				f7 = (float)((i55 << 4) - 2);
				f9 = (float)((i55 + 1 << 4) + 2);

				for(i60 = 0; i60 < entityMap86.ySlot; ++i60) {
					f11 = (float)((i60 << 4) - 2);
					f12 = (float)((i60 + 1 << 4) + 2);

					for(i48 = 0; i48 < entityMap86.zSlot; ++i48) {
						if((list68 = entityMap86.entityGrid[(i48 * entityMap86.ySlot + i60) * entityMap86.xSlot + i55]).size() != 0) {
							f14 = (float)((i48 << 4) - 2);
							f89 = (float)((i48 + 1 << 4) + 2);
							boolean z124 = clippingHelper83.isBoundingBoxInFrustrum(f7, f11, f14, f9, f12, f89);
							boolean z99 = false;
							if(z124) {
								float f29 = f89;
								float f117 = f12;
								f109 = f9;
								float f107 = f14;
								float f105 = f11;
								f21 = f7;
								ClippingHelper clippingHelper77 = clippingHelper83;
								int i113 = 0;

								while(true) {
									if(i113 >= 6) {
										z124 = true;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f21 + clippingHelper77.frustrum[i113][1] * f105 + clippingHelper77.frustrum[i113][2] * f107 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f109 + clippingHelper77.frustrum[i113][1] * f105 + clippingHelper77.frustrum[i113][2] * f107 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f21 + clippingHelper77.frustrum[i113][1] * f117 + clippingHelper77.frustrum[i113][2] * f107 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f109 + clippingHelper77.frustrum[i113][1] * f117 + clippingHelper77.frustrum[i113][2] * f107 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f21 + clippingHelper77.frustrum[i113][1] * f105 + clippingHelper77.frustrum[i113][2] * f29 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f109 + clippingHelper77.frustrum[i113][1] * f105 + clippingHelper77.frustrum[i113][2] * f29 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f21 + clippingHelper77.frustrum[i113][1] * f117 + clippingHelper77.frustrum[i113][2] * f29 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									if(clippingHelper77.frustrum[i113][0] * f109 + clippingHelper77.frustrum[i113][1] * f117 + clippingHelper77.frustrum[i113][2] * f29 + clippingHelper77.frustrum[i113][3] <= 0.0F) {
										z124 = false;
										break;
									}

									++i113;
								}

								boolean z94 = z124;

								for(int i100 = 0; i100 < list68.size(); ++i100) {
									Entity entity101;
									f105 = (entity70 = entity101 = (Entity)list68.get(i100)).posX - vec3D78.xCoord;
									f107 = entity70.posY - vec3D78.yCoord;
									f109 = entity70.posZ - vec3D78.zCoord;
									f117 = f105 * f105 + f107 * f107 + f109 * f109;
									Object object112 = null;
									AxisAlignedBB axisAlignedBB110;
									f105 = (axisAlignedBB110 = entity70.boundingBox).x1 - axisAlignedBB110.x0;
									f109 = axisAlignedBB110.y1 - axisAlignedBB110.y0;
									float f115 = axisAlignedBB110.z1 - axisAlignedBB110.z0;
									f125 = (f105 + f109 + f115) / 3.0F;
									f21 = 0.0F;
									f21 = f125 * 64.0F;
									if(f117 < f21 * f21) {
										if(!z94) {
											AxisAlignedBB axisAlignedBB102 = entity101.boundingBox;
											if(!clippingHelper83.isBoundingBoxInFrustrum(entity101.boundingBox.x0, axisAlignedBB102.y0, axisAlignedBB102.z0, axisAlignedBB102.x1, axisAlignedBB102.y1, axisAlignedBB102.z1)) {
												continue;
											}
										}

										f107 = f18;
										RenderEngine renderEngine108 = renderGlobal71.renderEngine;
										RenderManager renderManager80 = renderGlobal71.renderManager;
										if(!(entity101 instanceof EntityPlayer)) {
											f109 = entity101.lastTickPosX + (entity101.posX - entity101.lastTickPosX) * f18;
											f117 = entity101.lastTickPosY + (entity101.posY - entity101.lastTickPosY) * f18;
											f29 = entity101.lastTickPosZ + (entity101.posZ - entity101.lastTickPosZ) * f18;
											f115 = f29;
											float f114 = f117;
											f104 = f109;
											RenderManager renderManager119 = renderManager80;
											GL11.glEnable(GL11.GL_BLEND);
											Object object33 = null;
											renderEngine108.clampTexture = true;
											int i118 = renderEngine108.getTexture("/shadow.png");
											GL11.glBindTexture(GL11.GL_TEXTURE_2D, i118);
											object33 = null;
											renderEngine108.clampTexture = false;
											GL11.glDepthMask(false);
											f89 = 0.5F;

											for(i118 = (int)(f109 - f89); i118 <= (int)(f104 + f89); ++i118) {
												for(int i34 = (int)(f114 - 2.0F); i34 <= (int)f114; ++i34) {
													for(int i35 = (int)(f115 - f89); i35 <= (int)(f115 + f89); ++i35) {
														int i36;
														if((i36 = renderManager119.worldObj.getBlockId(i118, i34 - 1, i35)) > 0 && renderManager119.worldObj.isHalfLit(i118, i34, i35)) {
															Block block121 = Block.blocksList[i36];
															Tessellator tessellator44 = Tessellator.instance;
															float f38;
															if((f38 = (1.0F - (f114 - (float)i34) / 2.0F) * 0.5F) >= 0.0F) {
																GL11.glColor4f(1.0F, 1.0F, 1.0F, f38);
																tessellator44.startDrawingQuads();
																f38 = (float)i118 + block121.minX;
																float f40 = (float)i118 + block121.maxX;
																float f41 = (float)i34 + block121.minY;
																float f45 = (float)i35 + block121.minZ;
																float f122 = (float)i35 + block121.maxZ;
																float f42 = (f104 - f38) / 2.0F / f89 + 0.5F;
																float f37 = (f104 - f40) / 2.0F / f89 + 0.5F;
																float f46 = (f115 - f45) / 2.0F / f89 + 0.5F;
																float f39 = (f115 - f122) / 2.0F / f89 + 0.5F;
																tessellator44.addVertexWithUV(f38, f41, f45, f42, f46);
																tessellator44.addVertexWithUV(f38, f41, f122, f42, f39);
																tessellator44.addVertexWithUV(f40, f41, f122, f37, f39);
																tessellator44.addVertexWithUV(f40, f41, f45, f37, f46);
																tessellator44.draw();
															}
														}
													}
												}
											}

											GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
											GL11.glDisable(GL11.GL_BLEND);
											GL11.glDepthMask(true);
											if(entity101 instanceof EntityLiving) {
												EntityLiving entityLiving116 = (EntityLiving)entity101;
												GL11.glPushMatrix();

												try {
													f107 = entityLiving116.prevRenderYawOffset + (entityLiving116.renderYawOffset - entityLiving116.prevRenderYawOffset) * f107;
													GL11.glTranslatef(f109, f117, f29);
													i118 = renderEngine108.getTexture("/cube-nes.png");
													GL11.glBindTexture(GL11.GL_TEXTURE_2D, i118);
													GL11.glRotatef(-f107 + 180.0F, 0.0F, 1.0F, 0.0F);
													f125 = f114 = renderManager80.worldObj.getBlockLightValue((int)f109, (int)f117, (int)f29);
													GL11.glColor3f(f125, f125, f114);
													f117 = 0.02F;
													GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
													GL11.glScalef(f117, -f117, f117);
													GL11.glEnable(GL11.GL_NORMALIZE);
													renderManager80.model[0].renderModelVertices(0, 0, 0.0F);
													GL11.glDisable(GL11.GL_NORMALIZE);
												} catch (Exception exception47) {
													exception47.printStackTrace();
												}

												GL11.glPopMatrix();
											} else {
												AxisAlignedBB axisAlignedBB120 = entity101.boundingBox;
												GL11.glDisable(GL11.GL_TEXTURE_2D);
												Tessellator tessellator90 = Tessellator.instance;
												GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
												tessellator90.startDrawingQuads();
												Tessellator.setNormal(0.0F, 0.0F, -1.0F);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z0);
												Tessellator.setNormal(0.0F, 0.0F, 1.0F);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z1);
												Tessellator.setNormal(0.0F, -1.0F, 0.0F);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z1);
												Tessellator.setNormal(0.0F, 1.0F, 0.0F);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z0);
												Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y1, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x0, axisAlignedBB120.y0, axisAlignedBB120.z0);
												Tessellator.setNormal(1.0F, 0.0F, 0.0F);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z0);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y1, axisAlignedBB120.z1);
												tessellator90.addVertex(axisAlignedBB120.x1, axisAlignedBB120.y0, axisAlignedBB120.z1);
												tessellator90.draw();
												GL11.glEnable(GL11.GL_TEXTURE_2D);
												GL11.glPushMatrix();
												GL11.glTranslatef(f109, f117, f29);
												f114 = 0.02F;
												GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
												GL11.glScalef(f114, -f114, f114);
												renderManager80.model[0].renderModelVertices(0, 0, f18);
												GL11.glPopMatrix();
											}
										}
									}
								}
							}
						}
					}
				}
			}

			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			float f84 = f1;
			EffectRenderer effectRenderer75 = effectRenderer6;
			f18 = -MathHelper.cos(entityPlayer3.rotationYaw * (float)Math.PI / 180.0F);
			f52 = -(f19 = -MathHelper.sin(entityPlayer3.rotationYaw * (float)Math.PI / 180.0F)) * MathHelper.sin(entityPlayer3.rotationPitch * (float)Math.PI / 180.0F);
			f7 = f18 * MathHelper.sin(entityPlayer3.rotationPitch * (float)Math.PI / 180.0F);
			f9 = MathHelper.cos(entityPlayer3.rotationPitch * (float)Math.PI / 180.0F);

			int i64;
			for(i60 = 0; i60 < 2; ++i60) {
				if(effectRenderer75.fxLayers[i60].size() != 0) {
					i64 = 0;
					if(i60 == 0) {
						i64 = effectRenderer75.renderEngine.getTexture("/particles.png");
					}

					if(i60 == 1) {
						i64 = effectRenderer75.renderEngine.getTexture("/terrain.png");
					}

					GL11.glBindTexture(GL11.GL_TEXTURE_2D, i64);
					Tessellator tessellator67 = Tessellator.instance;
					Tessellator.instance.startDrawingQuads();

					for(i48 = 0; i48 < effectRenderer75.fxLayers[i60].size(); ++i48) {
						EntityFX entityFX126 = (EntityFX)effectRenderer75.fxLayers[i60].get(i48);
						list68 = null;
						entityFX126.renderParticle(tessellator67, f84, f18, f9, f19, f52, f7);
					}

					tessellator67.draw();
				}
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderGlobal5.renderEngine.getTexture("/rock.png"));
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glCallList(renderGlobal5.glGenList);
			this.setupFog();
			renderGlobal71 = renderGlobal5;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderGlobal5.renderEngine.getTexture("/clouds.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			f84 = (float)(renderGlobal5.worldObj.cloudColor >> 16 & 255) / 255.0F;
			f18 = (float)(renderGlobal5.worldObj.cloudColor >> 8 & 255) / 255.0F;
			f19 = (float)(renderGlobal5.worldObj.cloudColor & 255) / 255.0F;
			if(renderGlobal5.mc.options.anaglyph) {
				f52 = (f84 * 30.0F + f18 * 59.0F + f19 * 11.0F) / 100.0F;
				f7 = (f84 * 30.0F + f18 * 70.0F) / 100.0F;
				f9 = (f84 * 30.0F + f19 * 70.0F) / 100.0F;
				f84 = f52;
				f18 = f7;
				f19 = f9;
			}

			Tessellator tessellator59 = Tessellator.instance;
			f10 = 0.0F;
			f11 = 4.8828125E-4F;
			f10 = (float)(renderGlobal5.worldObj.height + 2);
			f12 = ((float)renderGlobal5.cloudOffsetX + f1) * f11 * 0.03F;
			float f49 = 0.0F;
			tessellator59.startDrawingQuads();
			tessellator59.setColorOpaque_F(f84, f18, f19);

			for(i65 = -2048; i65 < renderGlobal71.worldObj.width + 2048; i65 += 512) {
				for(i72 = -2048; i72 < renderGlobal71.worldObj.length + 2048; i72 += 512) {
					tessellator59.addVertexWithUV((float)i65, f10, (float)(i72 + 512), (float)i65 * f11 + f12, (float)(i72 + 512) * f11);
					tessellator59.addVertexWithUV((float)(i65 + 512), f10, (float)(i72 + 512), (float)(i65 + 512) * f11 + f12, (float)(i72 + 512) * f11);
					tessellator59.addVertexWithUV((float)(i65 + 512), f10, (float)i72, (float)(i65 + 512) * f11 + f12, (float)i72 * f11);
					tessellator59.addVertexWithUV((float)i65, f10, (float)i72, (float)i65 * f11 + f12, (float)i72 * f11);
					tessellator59.addVertexWithUV((float)i65, f10, (float)i72, (float)i65 * f11 + f12, (float)i72 * f11);
					tessellator59.addVertexWithUV((float)(i65 + 512), f10, (float)i72, (float)(i65 + 512) * f11 + f12, (float)i72 * f11);
					tessellator59.addVertexWithUV((float)(i65 + 512), f10, (float)(i72 + 512), (float)(i65 + 512) * f11 + f12, (float)(i72 + 512) * f11);
					tessellator59.addVertexWithUV((float)i65, f10, (float)(i72 + 512), (float)i65 * f11 + f12, (float)(i72 + 512) * f11);
				}
			}

			tessellator59.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator59.startDrawingQuads();
			f12 = (float)(renderGlobal71.worldObj.skyColor >> 16 & 255) / 255.0F;
			f49 = (float)(renderGlobal71.worldObj.skyColor >> 8 & 255) / 255.0F;
			f13 = (float)(renderGlobal71.worldObj.skyColor & 255) / 255.0F;
			if(renderGlobal71.mc.options.anaglyph) {
				f14 = (f12 * 30.0F + f49 * 59.0F + f13 * 11.0F) / 100.0F;
				f89 = (f12 * 30.0F + f49 * 70.0F) / 100.0F;
				f21 = (f12 * 30.0F + f13 * 70.0F) / 100.0F;
				f12 = f14;
				f49 = f89;
				f13 = f21;
			}

			tessellator59.setColorOpaque_F(f12, f49, f13);
			f10 = (float)(renderGlobal71.worldObj.height + 10);

			for(i72 = -2048; i72 < renderGlobal71.worldObj.width + 2048; i72 += 512) {
				for(int i91 = -2048; i91 < renderGlobal71.worldObj.length + 2048; i91 += 512) {
					tessellator59.addVertex((float)i72, f10, (float)i91);
					tessellator59.addVertex((float)(i72 + 512), f10, (float)i91);
					tessellator59.addVertex((float)(i72 + 512), f10, (float)(i91 + 512));
					tessellator59.addVertex((float)i72, f10, (float)(i91 + 512));
				}
			}

			tessellator59.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.setupFog();
			int i56;
			if(this.mc.objectMouseOver != null) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				boolean z92 = false;
				boolean z88 = false;
				MovingObjectPosition movingObjectPosition79 = this.mc.objectMouseOver;
				Tessellator tessellator95 = Tessellator.instance;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
				if(renderGlobal5.damagePartialTime > 0.0F) {
					GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
					i55 = renderGlobal5.renderEngine.getTexture("/terrain.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, i55);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
					GL11.glPushMatrix();
					block54 = (i56 = renderGlobal5.worldObj.getBlockId(movingObjectPosition79.blockX, movingObjectPosition79.blockY, movingObjectPosition79.blockZ)) > 0 ? Block.blocksList[i56] : null;
					tessellator95.startDrawingQuads();
					tessellator95.disableColor();
					if(block54 == null) {
						block54 = Block.stone;
					}

					i72 = 240 + (int)(renderGlobal5.damagePartialTime * 10.0F);
					i65 = movingObjectPosition79.blockZ;
					i48 = movingObjectPosition79.blockY;
					i63 = movingObjectPosition79.blockX;
					RenderBlocks renderBlocks69 = renderGlobal5.globalRenderBlocks;
					renderGlobal5.globalRenderBlocks.overrideBlockTexture = i72;
					renderBlocks69.renderBlockByRenderType(block54, i63, i48, i65);
					renderBlocks69.overrideBlockTexture = -1;
					tessellator95.draw();
					GL11.glDepthMask(true);
					GL11.glPopMatrix();
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				movingObjectPosition79 = this.mc.objectMouseOver;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
				GL11.glLineWidth(2.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDepthMask(false);
				f18 = 0.002F;
				if((i87 = renderGlobal5.worldObj.getBlockId(movingObjectPosition79.blockX, movingObjectPosition79.blockY, movingObjectPosition79.blockZ)) > 0) {
					AxisAlignedBB axisAlignedBB61 = Block.blocksList[i87].getSelectedBoundingBoxFromPool(movingObjectPosition79.blockX, movingObjectPosition79.blockY, movingObjectPosition79.blockZ).expand(f18, f18, f18);
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y0, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y0, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y0, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y0, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y0, axisAlignedBB61.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y1, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y1, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y1, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y1, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y1, axisAlignedBB61.z0);
					GL11.glEnd();
					GL11.glBegin(GL11.GL_LINES);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y0, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y1, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y0, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y1, axisAlignedBB61.z0);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y0, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x1, axisAlignedBB61.y1, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y0, axisAlignedBB61.z1);
					GL11.glVertex3f(axisAlignedBB61.x0, axisAlignedBB61.y1, axisAlignedBB61.z1);
					GL11.glEnd();
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderGlobal5.renderEngine.getTexture("/water.png"));
			GL11.glCallList(renderGlobal5.glGenList + 1);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColorMask(false, false, false, false);
			i55 = renderGlobal5.sortAndRender(entityPlayer3, 1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.options.anaglyph) {
				if(i2 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(i55 > 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderGlobal5.renderEngine.getTexture("/terrain.png"));
				GL11.glCallLists(renderGlobal5.renderIntBuffer);
			}

			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);
			if(this.mc.thirdPersonView) {
				float f82 = f1;
				entityRenderer15 = this;
				entityPlayer17 = this.mc.thePlayer;
				World world96 = this.mc.theWorld;
				i87 = (int)entityPlayer17.posX;
				i55 = (int)entityPlayer17.posY;
				i56 = (int)entityPlayer17.posZ;
				Tessellator tessellator66 = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
				i64 = i87 - 5;

				while(true) {
					if(i64 > i87 + 5) {
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						break;
					}

					for(i63 = i56 - 5; i63 <= i56 + 5; ++i63) {
						i48 = world96.getMapHeight(i64, i63);
						i65 = i55 - 5;
						i72 = i55 + 5;
						if(i65 < i48) {
							i65 = i48;
						}

						if(i72 < i48) {
							i72 = i48;
						}

						if(i65 != i72) {
							f21 = ((float)((entityRenderer15.entityRendererInt1 + i64 * 3121 + i63 * 418711) % 32) + f82) / 32.0F;
							float f97 = (float)i64 + 0.5F - entityPlayer17.posX;
							f23 = (float)i63 + 0.5F - entityPlayer17.posZ;
							f104 = MathHelper.sqrt_float(f97 * f97 + f23 * f23) / (float)5;
							GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f104 * f104) * 0.7F);
							tessellator66.startDrawingQuads();
							tessellator66.addVertexWithUV((float)i64, (float)i65, (float)i63, 0.0F, (float)i65 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)(i64 + 1), (float)i65, (float)(i63 + 1), 2.0F, (float)i65 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)(i64 + 1), (float)i72, (float)(i63 + 1), 2.0F, (float)i72 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)i64, (float)i72, (float)i63, 0.0F, (float)i72 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)i64, (float)i65, (float)(i63 + 1), 0.0F, (float)i65 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)(i64 + 1), (float)i65, (float)i63, 2.0F, (float)i65 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)(i64 + 1), (float)i72, (float)i63, 2.0F, (float)i72 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.addVertexWithUV((float)i64, (float)i72, (float)(i63 + 1), 0.0F, (float)i72 * 2.0F / 8.0F + f21 * 2.0F);
							tessellator66.draw();
						}
					}

					++i64;
				}
			}

			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((i2 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(f1);
			if(this.mc.options.viewBobbing) {
				this.setupViewBobbing(f1);
			}

			ItemRenderer itemRenderer98 = this.itemRenderer;
			f52 = this.itemRenderer.prevEquippedProgress + (itemRenderer98.equippedProgress - itemRenderer98.prevEquippedProgress) * f1;
			entityPlayer50 = itemRenderer98.minecraft.thePlayer;
			GL11.glPushMatrix();
			GL11.glRotatef(entityPlayer50.prevRotationPitch + (entityPlayer50.rotationPitch - entityPlayer50.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityPlayer50.prevRotationYaw + (entityPlayer50.rotationYaw - entityPlayer50.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			f9 = 0.8F;
			if(itemRenderer98.itemSwingState) {
				f11 = MathHelper.sin((f10 = ((float)itemRenderer98.swingProgress + f1) / 7.0F) * (float)Math.PI);
				GL11.glTranslatef(-MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI) * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI * 2.0F) * 0.2F, -f11 * 0.2F);
			}

			GL11.glTranslatef(0.7F * f9, -0.65F * f9 - (1.0F - f52) * 0.6F, -0.9F * f9);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(itemRenderer98.itemSwingState) {
				f125 = f10 = ((float)itemRenderer98.swingProgress + f1) / 7.0F;
				f11 = MathHelper.sin(f125 * f125 * (float)Math.PI);
				GL11.glRotatef(MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI) * 80.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-f11 * 20.0F, 1.0F, 0.0F, 0.0F);
			}

			f125 = f10 = itemRenderer98.minecraft.theWorld.getBlockLightValue((int)entityPlayer50.posX, (int)entityPlayer50.posY, (int)entityPlayer50.posZ);
			GL11.glColor4f(f125, f125, f10, 1.0F);
			if(itemRenderer98.itemToRender != null) {
				f11 = 0.4F;
				GL11.glScalef(0.4F, 0.4F, f11);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, itemRenderer98.minecraft.renderEngine.getTexture("/terrain.png"));
				itemRenderer98.renderBlocksInstance.renderBlockOnInventory(itemRenderer98.itemToRender);
			} else {
				EntityPlayer.setupSkinImage(itemRenderer98.minecraft.renderEngine);
				GL11.glScalef(1.0F, -1.0F, -1.0F);
				GL11.glTranslatef(0.0F, 0.2F, 0.0F);
				GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				f11 = 0.0F;
			}

			GL11.glDisable(GL11.GL_NORMALIZE);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			if(!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void setupOverlayRendering() {
		int i1 = this.mc.displayWidth * 240 / this.mc.displayHeight;
		int i2 = this.mc.displayHeight * 240 / this.mc.displayHeight;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)i1, (double)i2, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
	}

	private void setupFog() {
		World world1 = this.mc.theWorld;
		EntityPlayer entityPlayer2 = this.mc.thePlayer;
		float f3 = 0.0F;
		float f6 = this.fogColorBlue;
		float f5 = this.fogColorGreen;
		float f4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(f4).put(f5).put(f6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(GL11.GL_FOG_COLOR, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block block8;
		if((block8 = Block.blocksList[world1.getBlockId((int)entityPlayer2.posX, (int)(entityPlayer2.posY + 0.12F), (int)entityPlayer2.posZ)]) != null && block8.getMaterial() != Material.air) {
			Material material7 = block8.getMaterial();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(material7 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if(material7 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}