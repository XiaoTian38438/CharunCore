/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class WoolCarpetBlock extends CarpetBlock {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(WoolCarpetBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, WoolCarpetBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<WoolCarpetBlock> CODEC;
/*    */   private final DyeColor color;
/*    */   
/*    */   public MapCodec<WoolCarpetBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WoolCarpetBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/* 22 */     this.color = paramDyeColor;
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 26 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WoolCarpetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */