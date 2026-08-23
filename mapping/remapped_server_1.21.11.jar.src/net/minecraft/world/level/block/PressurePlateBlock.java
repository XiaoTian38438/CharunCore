/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class PressurePlateBlock extends BasePressurePlateBlock {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, PressurePlateBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<PressurePlateBlock> CODEC;
/*    */   
/*    */   public MapCodec<PressurePlateBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   
/*    */   protected PressurePlateBlock(BlockSetType paramBlockSetType, BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties, paramBlockSetType);
/* 31 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalForState(BlockState paramBlockState) {
/* 36 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState setSignalForState(BlockState paramBlockState, int paramInt) {
/* 41 */     return (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf((paramInt > 0)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSignalStrength(Level paramLevel, BlockPos paramBlockPos) {
/* 46 */     switch (this.type.pressurePlateSensitivity()) { default: throw new MatchException(null, null);
/*    */       case EVERYTHING: 
/* 48 */       case MOBS: break; }  Class<LivingEntity> clazz = LivingEntity.class;
/*    */     
/* 50 */     return (getEntityCount(paramLevel, TOUCH_AABB.move(paramBlockPos), (Class)clazz) > 0) ? 15 : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 55 */     paramBuilder.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PressurePlateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */