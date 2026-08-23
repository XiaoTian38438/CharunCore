/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WebBlock extends Block {
/* 14 */   public static final MapCodec<WebBlock> CODEC = simpleCodec(WebBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WebBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WebBlock(BlockBehaviour.Properties paramProperties) {
/* 22 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 27 */     Vec3 vec3 = new Vec3(0.25D, 0.05000000074505806D, 0.25D);
/* 28 */     if (paramEntity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)paramEntity; if (livingEntity.hasEffect(MobEffects.WEAVING))
/* 29 */         vec3 = new Vec3(0.5D, 0.25D, 0.5D);  }
/*    */     
/* 31 */     paramEntity.makeStuckInBlock(paramBlockState, vec3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WebBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */