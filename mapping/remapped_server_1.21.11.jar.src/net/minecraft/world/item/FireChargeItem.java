/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FireChargeItem extends Item implements ProjectileItem {
/*    */   public FireChargeItem(Item.Properties paramProperties) {
/* 28 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 33 */     Level level = paramUseOnContext.getLevel();
/* 34 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/* 35 */     BlockState blockState = level.getBlockState(blockPos);
/* 36 */     boolean bool = false;
/*    */     
/* 38 */     if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
/* 39 */       playSound(level, blockPos);
/* 40 */       level.setBlockAndUpdate(blockPos, (BlockState)blockState.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 41 */       level.gameEvent((Entity)paramUseOnContext.getPlayer(), (Holder)GameEvent.BLOCK_CHANGE, blockPos);
/* 42 */       bool = true;
/*    */     } else {
/* 44 */       blockPos = blockPos.relative(paramUseOnContext.getClickedFace());
/* 45 */       if (BaseFireBlock.canBePlacedAt(level, blockPos, paramUseOnContext.getHorizontalDirection())) {
/* 46 */         playSound(level, blockPos);
/* 47 */         level.setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)level, blockPos));
/* 48 */         level.gameEvent((Entity)paramUseOnContext.getPlayer(), (Holder)GameEvent.BLOCK_PLACE, blockPos);
/* 49 */         bool = true;
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     if (bool) {
/* 54 */       paramUseOnContext.getItemInHand().shrink(1);
/* 55 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 58 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */   
/*    */   private void playSound(Level paramLevel, BlockPos paramBlockPos) {
/* 62 */     RandomSource randomSource = paramLevel.getRandom();
/* 63 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (randomSource.nextFloat() - randomSource.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 68 */     RandomSource randomSource = paramLevel.getRandom();
/* 69 */     double d1 = randomSource.triangle(paramDirection.getStepX(), 0.11485000000000001D);
/* 70 */     double d2 = randomSource.triangle(paramDirection.getStepY(), 0.11485000000000001D);
/* 71 */     double d3 = randomSource.triangle(paramDirection.getStepZ(), 0.11485000000000001D);
/* 72 */     Vec3 vec3 = new Vec3(d1, d2, d3);
/* 73 */     SmallFireball smallFireball = new SmallFireball(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), vec3.normalize());
/* 74 */     smallFireball.setItem(paramItemStack);
/* 75 */     return (Projectile)smallFireball;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void shoot(Projectile paramProjectile, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 85 */     return ProjectileItem.DispenseConfig.builder()
/* 86 */       .positionFunction((paramBlockSource, paramDirection) -> DispenserBlock.getDispensePosition(paramBlockSource, 1.0D, Vec3.ZERO))
/* 87 */       .uncertainty(6.6666665F)
/* 88 */       .power(1.0F)
/* 89 */       .overrideDispenseEvent(1018)
/* 90 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\FireChargeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */