/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TorchBlock extends BaseTorchBlock {
/*    */   static {
/* 20 */     PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(paramParticleType -> { SimpleParticleType simpleParticleType = (SimpleParticleType)paramParticleType; return (Function)((paramParticleType instanceof SimpleParticleType) ? DataResult.success(simpleParticleType) : DataResult.error(())); }paramSimpleParticleType -> paramSimpleParticleType).fieldOf("particle_options");
/*    */     
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)PARTICLE_OPTIONS_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, TorchBlock::new));
/*    */   }
/*    */   protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD;
/*    */   public static final MapCodec<TorchBlock> CODEC;
/*    */   protected final SimpleParticleType flameParticle;
/*    */   
/*    */   public MapCodec<? extends TorchBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TorchBlock(SimpleParticleType paramSimpleParticleType, BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties);
/* 36 */     this.flameParticle = paramSimpleParticleType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 41 */     double d1 = paramBlockPos.getX() + 0.5D;
/* 42 */     double d2 = paramBlockPos.getY() + 0.7D;
/* 43 */     double d3 = paramBlockPos.getZ() + 0.5D;
/* 44 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/* 45 */     paramLevel.addParticle((ParticleOptions)this.flameParticle, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */