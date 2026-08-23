/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class WeightedPressurePlateBlock extends BasePressurePlateBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.intRange(1, 1024).fieldOf("max_weight").forGetter(()), (App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, WeightedPressurePlateBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeightedPressurePlateBlock> CODEC;
/*    */ 
/*    */   
/*    */   public MapCodec<WeightedPressurePlateBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*    */   
/*    */   private final int maxWeight;
/*    */   
/*    */   protected WeightedPressurePlateBlock(int paramInt, BlockSetType paramBlockSetType, BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties, paramBlockSetType);
/* 36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0)));
/* 37 */     this.maxWeight = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getSignalStrength(Level paramLevel, BlockPos paramBlockPos) {
/* 43 */     int i = Math.min(getEntityCount(paramLevel, TOUCH_AABB.move(paramBlockPos), Entity.class), this.maxWeight);
/* 44 */     if (i > 0) {
/* 45 */       float f = Math.min(this.maxWeight, i) / this.maxWeight;
/* 46 */       return Mth.ceil(f * 15.0F);
/*    */     } 
/*    */     
/* 49 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalForState(BlockState paramBlockState) {
/* 54 */     return ((Integer)paramBlockState.getValue((Property)POWER)).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState setSignalForState(BlockState paramBlockState, int paramInt) {
/* 59 */     return (BlockState)paramBlockState.setValue((Property)POWER, Integer.valueOf(paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getPressedTime() {
/* 64 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 69 */     paramBuilder.add(new Property[] { (Property)POWER });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeightedPressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */