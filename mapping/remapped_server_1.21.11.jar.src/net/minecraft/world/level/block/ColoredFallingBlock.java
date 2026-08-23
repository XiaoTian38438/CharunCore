/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class ColoredFallingBlock extends FallingBlock {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, ColoredFallingBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ColoredFallingBlock> CODEC;
/*    */   protected final ColorRGBA dustColor;
/*    */   
/*    */   public MapCodec<? extends ColoredFallingBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ColoredFallingBlock(ColorRGBA paramColorRGBA, BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramProperties);
/* 25 */     this.dustColor = paramColorRGBA;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 30 */     return this.dustColor.rgba();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ColoredFallingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */