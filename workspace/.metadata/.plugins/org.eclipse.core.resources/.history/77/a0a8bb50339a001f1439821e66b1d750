package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

final class EntityMapSlot {
	int xSlot;
	int ySlot;
	int zSlot;
	private EntityMap entityMap;

	private EntityMapSlot(EntityMap entityMap1) {
		this.entityMap = entityMap1;
	}

	public final EntityMapSlot init(float f1, float f2, float f3) {
		this.xSlot = (int)(f1 / 16.0F);
		this.ySlot = (int)(f2 / 16.0F);
		this.zSlot = (int)(f3 / 16.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		if(this.xSlot >= this.entityMap.xSlot) {
			this.xSlot = this.entityMap.xSlot - 1;
		}

		if(this.ySlot >= this.entityMap.ySlot) {
			this.ySlot = this.entityMap.ySlot - 1;
		}

		if(this.zSlot >= this.entityMap.zSlot) {
			this.zSlot = this.entityMap.zSlot - 1;
		}

		return this;
	}

	public final void add(Entity entity1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.ySlot + this.ySlot) * this.entityMap.xSlot + this.xSlot].add(entity1);
		}

	}

	public final void remove(Entity entity1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.entityMap.entityGrid[(this.zSlot * this.entityMap.ySlot + this.ySlot) * this.entityMap.xSlot + this.xSlot].remove(entity1);
		}

	}

	EntityMapSlot(EntityMap entityMap1, Unused unused2) {
		this(entityMap1);
	}
}