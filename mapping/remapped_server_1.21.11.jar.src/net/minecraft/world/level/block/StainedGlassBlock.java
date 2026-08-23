/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class StainedGlassBlock extends TransparentBlock implements BeaconBeamBlock {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, StainedGlassBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<StainedGlassBlock> CODEC;
/*    */   private final DyeColor color;
/*    */   
/*    */   public MapCodec<StainedGlassBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StainedGlassBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties);
/* 21 */     this.color = paramDyeColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 26 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StainedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */