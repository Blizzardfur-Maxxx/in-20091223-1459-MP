package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.level.World;

public final class EffectRenderer {
	public World worldObj;
	public List[] fxLayers = new List[2];
	public RenderEngine renderEngine;
	public Random rand = new Random();

	public EffectRenderer(World world1, RenderEngine renderEngine2) {
		if(world1 != null) {
			this.worldObj = world1;
		}

		this.renderEngine = renderEngine2;

		for(int i3 = 0; i3 < 2; ++i3) {
			this.fxLayers[i3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX entityFX1) {
		int i2 = entityFX1.getFXLayer();
		this.fxLayers[i2].add(entityFX1);
	}
}