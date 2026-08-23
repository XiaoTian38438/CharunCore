/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DropperBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DropperBlock extends DispenserBlock {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 25 */   public static final MapCodec<DropperBlock> CODEC = simpleCodec(DropperBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<DropperBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = (DispenseItemBehavior)new DefaultDispenseItemBehavior();
/*    */   
/*    */   public DropperBlock(BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected DispenseItemBehavior getDispenseMethod(Level paramLevel, ItemStack paramItemStack) {
/* 40 */     return DISPENSE_BEHAVIOUR;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 45 */     return (BlockEntity)new DropperBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   protected void dispenseFrom(ServerLevel paramServerLevel, BlockState paramBlockState, BlockPos paramBlockPos) {
/*    */     ItemStack itemStack2;
/* 50 */     DispenserBlockEntity dispenserBlockEntity = paramServerLevel.getBlockEntity(paramBlockPos, BlockEntityType.DROPPER).orElse(null);
/* 51 */     if (dispenserBlockEntity == null) {
/* 52 */       LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", paramBlockPos);
/*    */       return;
/*    */     } 
/* 55 */     BlockSource blockSource = new BlockSource(paramServerLevel, paramBlockPos, paramBlockState, dispenserBlockEntity);
/*    */     
/* 57 */     int i = dispenserBlockEntity.getRandomSlot(paramServerLevel.random);
/* 58 */     if (i < 0) {
/* 59 */       paramServerLevel.levelEvent(1001, paramBlockPos, 0);
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     ItemStack itemStack1 = dispenserBlockEntity.getItem(i);
/* 64 */     if (itemStack1.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 68 */     Direction direction = (Direction)paramServerLevel.getBlockState(paramBlockPos).getValue((Property)FACING);
/* 69 */     Container container = HopperBlockEntity.getContainerAt((Level)paramServerLevel, paramBlockPos.relative(direction));
/*    */ 
/*    */     
/* 72 */     if (container == null) {
/* 73 */       itemStack2 = DISPENSE_BEHAVIOUR.dispense(blockSource, itemStack1);
/*    */     } else {
/* 75 */       itemStack2 = HopperBlockEntity.addItem((Container)dispenserBlockEntity, container, itemStack1.copyWithCount(1), direction.getOpposite());
/*    */       
/* 77 */       if (itemStack2.isEmpty()) {
/* 78 */         itemStack2 = itemStack1.copy();
/* 79 */         itemStack2.shrink(1);
/*    */       } else {
/*    */         
/* 82 */         itemStack2 = itemStack1.copy();
/*    */       } 
/*    */     } 
/*    */     
/* 86 */     dispenserBlockEntity.setItem(i, itemStack2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DropperBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */