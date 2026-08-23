package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.RandomSource;

interface Formula {
  int calculateNewCount(RandomSource paramRandomSource, int paramInt1, int paramInt2);
  
  ApplyBonusCount.FormulaType getType();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$Formula.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */