/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FlowerBlock extends VegetationBlock implements SuspiciousEffectHolder {
/* 21 */   protected static final MapCodec<SuspiciousStewEffects> EFFECTS_FIELD = SuspiciousStewEffects.CODEC.fieldOf("suspicious_stew_effects");
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), (App)propertiesCodec()).apply((Applicative)paramInstance, FlowerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FlowerBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends FlowerBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 10.0D);
/*    */   
/*    */   private final SuspiciousStewEffects suspiciousStewEffects;
/*    */   
/*    */   public FlowerBlock(Holder<MobEffect> paramHolder, float paramFloat, BlockBehaviour.Properties paramProperties) {
/* 38 */     this(makeEffectList(paramHolder, paramFloat), paramProperties);
/*    */   }
/*    */   
/*    */   public FlowerBlock(SuspiciousStewEffects paramSuspiciousStewEffects, BlockBehaviour.Properties paramProperties) {
/* 42 */     super(paramProperties);
/* 43 */     this.suspiciousStewEffects = paramSuspiciousStewEffects;
/*    */   }
/*    */   
/*    */   protected static SuspiciousStewEffects makeEffectList(Holder<MobEffect> paramHolder, float paramFloat) {
/* 47 */     return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(paramHolder, 
/* 48 */             Mth.floor(paramFloat * 20.0F))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 54 */     return SHAPE.move(paramBlockState.getOffset(paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public SuspiciousStewEffects getSuspiciousEffects() {
/* 59 */     return this.suspiciousStewEffects;
/*    */   }
/*    */   
/*    */   public MobEffectInstance getBeeInteractionEffect() {
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */