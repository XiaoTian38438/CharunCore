/*    */ package net.minecraft.world.level.block.entity;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.Spawner;
/*    */ import net.minecraft.world.level.block.TrialSpawnerBlock;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class TrialSpawnerBlockEntity extends BlockEntity implements Spawner, TrialSpawner.StateAccessor {
/* 24 */   private final TrialSpawner trialSpawner = createDefaultSpawner();
/*    */   
/*    */   public TrialSpawnerBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 27 */     super(BlockEntityType.TRIAL_SPAWNER, paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   private TrialSpawner createDefaultSpawner() {
/* 31 */     PlayerDetector playerDetector = SharedConstants.DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : PlayerDetector.NO_CREATIVE_PLAYERS;
/* 32 */     PlayerDetector.EntitySelector entitySelector = PlayerDetector.EntitySelector.SELECT_FROM_LEVEL;
/* 33 */     return new TrialSpawner(TrialSpawner.FullConfig.DEFAULT, this, playerDetector, entitySelector);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loadAdditional(ValueInput paramValueInput) {
/* 38 */     super.loadAdditional(paramValueInput);
/*    */     
/* 40 */     this.trialSpawner.load(paramValueInput);
/*    */     
/* 42 */     if (this.level != null) {
/* 43 */       markUpdated();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 49 */     super.saveAdditional(paramValueOutput);
/* 50 */     this.trialSpawner.store(paramValueOutput);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 55 */     return ClientboundBlockEntityDataPacket.create(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 60 */     return this.trialSpawner.getStateData().getUpdateTag((TrialSpawnerState)getBlockState().getValue((Property)TrialSpawnerBlock.STATE));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntityId(EntityType<?> paramEntityType, RandomSource paramRandomSource) {
/* 65 */     if (this.level == null) {
/* 66 */       Util.logAndPauseIfInIde("Expected non-null level");
/*    */       
/*    */       return;
/*    */     } 
/* 70 */     this.trialSpawner.overrideEntityToSpawn(paramEntityType, this.level);
/* 71 */     setChanged();
/*    */   }
/*    */   
/*    */   public TrialSpawner getTrialSpawner() {
/* 75 */     return this.trialSpawner;
/*    */   }
/*    */ 
/*    */   
/*    */   public TrialSpawnerState getState() {
/* 80 */     if (!getBlockState().hasProperty((Property)BlockStateProperties.TRIAL_SPAWNER_STATE)) {
/* 81 */       return TrialSpawnerState.INACTIVE;
/*    */     }
/* 83 */     return (TrialSpawnerState)getBlockState().getValue((Property)BlockStateProperties.TRIAL_SPAWNER_STATE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setState(Level paramLevel, TrialSpawnerState paramTrialSpawnerState) {
/* 88 */     setChanged();
/* 89 */     paramLevel.setBlockAndUpdate(this.worldPosition, (BlockState)getBlockState().setValue((Property)BlockStateProperties.TRIAL_SPAWNER_STATE, (Comparable)paramTrialSpawnerState));
/*    */   }
/*    */ 
/*    */   
/*    */   public void markUpdated() {
/* 94 */     setChanged();
/* 95 */     if (this.level != null)
/* 96 */       this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\TrialSpawnerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */