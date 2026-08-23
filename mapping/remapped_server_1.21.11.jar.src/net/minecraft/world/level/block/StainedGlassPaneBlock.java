/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassPaneBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, StainedGlassPaneBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<StainedGlassPaneBlock> CODEC;
/*    */   private final DyeColor color;
/*    */   
/*    */   public MapCodec<StainedGlassPaneBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StainedGlassPaneBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/* 22 */     this.color = paramDyeColor;
/* 23 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 28 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StainedGlassPaneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */