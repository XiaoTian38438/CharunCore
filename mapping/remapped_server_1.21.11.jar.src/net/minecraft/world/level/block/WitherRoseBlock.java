/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WitherRoseBlock extends FlowerBlock {
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), (App)propertiesCodec()).apply((Applicative)paramInstance, WitherRoseBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WitherRoseBlock> CODEC;
/*    */   
/*    */   public MapCodec<WitherRoseBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(Holder<MobEffect> paramHolder, float paramFloat, BlockBehaviour.Properties paramProperties) {
/* 38 */     this(makeEffectList(paramHolder, paramFloat), paramProperties);
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(SuspiciousStewEffects paramSuspiciousStewEffects, BlockBehaviour.Properties paramProperties) {
/* 42 */     super(paramSuspiciousStewEffects, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 47 */     return (super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos) || paramBlockState.is(Blocks.NETHERRACK) || paramBlockState.is(Blocks.SOUL_SAND) || paramBlockState.is(Blocks.SOUL_SOIL));
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 52 */     VoxelShape voxelShape = getShape(paramBlockState, (BlockGetter)paramLevel, paramBlockPos, CollisionContext.empty());
/* 53 */     Vec3 vec3 = voxelShape.bounds().getCenter();
/* 54 */     double d1 = paramBlockPos.getX() + vec3.x;
/* 55 */     double d2 = paramBlockPos.getZ() + vec3.z;
/* 56 */     for (byte b = 0; b < 3; b++) {
/* 57 */       if (paramRandomSource.nextBoolean()) {
/* 58 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1 + paramRandomSource.nextDouble() / 5.0D, paramBlockPos.getY() + 0.5D - paramRandomSource.nextDouble(), d2 + paramRandomSource.nextDouble() / 5.0D, 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 65 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramLevel
/* 66 */         .getDifficulty() != Difficulty.PEACEFUL && paramEntity instanceof LivingEntity) {
/* 67 */         LivingEntity livingEntity = (LivingEntity)paramEntity;
/* 68 */         if (!livingEntity.isInvulnerableTo(serverLevel, paramLevel.damageSources().wither()))
/* 69 */           livingEntity.addEffect(getBeeInteractionEffect()); 
/*    */       }  }
/*    */   
/*    */   }
/*    */   
/*    */   public MobEffectInstance getBeeInteractionEffect() {
/* 75 */     return new MobEffectInstance(MobEffects.WITHER, 40);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WitherRoseBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */