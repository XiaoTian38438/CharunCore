/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class SandBlock extends ColoredFallingBlock {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, SandBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SandBlock> CODEC;
/*    */   
/*    */   public MapCodec<SandBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SandBlock(ColorRGBA paramColorRGBA, BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramColorRGBA, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 29 */     super.animateTick(paramBlockState, paramLevel, paramBlockPos, paramRandomSource);
/* 30 */     AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(paramLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */