/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends DefaultDispenseItemBehavior
/*     */ {
/*     */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 122 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 123 */     BlockPos blockPos = paramBlockSource.pos().relative(direction);
/* 124 */     ServerLevel serverLevel = paramBlockSource.level();
/* 125 */     Consumer consumer = EntityType.appendDefaultStackConfig(paramArmorStand -> paramArmorStand.setYRot(paramDirection.toYRot()), (Level)serverLevel, paramItemStack, null);
/* 126 */     ArmorStand armorStand = (ArmorStand)EntityType.ARMOR_STAND.spawn(serverLevel, consumer, blockPos, EntitySpawnReason.DISPENSER, false, false);
/* 127 */     if (armorStand != null) {
/* 128 */       paramItemStack.shrink(1);
/*     */     }
/* 130 */     return paramItemStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */