/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class ValidateNearbyPoi
/*    */ {
/*    */   public static BehaviorControl<LivingEntity> create(Predicate<Holder<PoiType>> paramPredicate, MemoryModuleType<GlobalPos> paramMemoryModuleType) {
/* 28 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final int MAX_DISTANCE = 16;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean bedIsOccupied(ServerLevel paramServerLevel, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/* 54 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/* 55 */     return (blockState.is(BlockTags.BEDS) && ((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue() && !paramLivingEntity.isSleeping());
/*    */   }
/*    */   
/*    */   private static boolean bedIsOccupiedByVillager(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 59 */     List list = paramServerLevel.getEntitiesOfClass(Villager.class, new AABB(paramBlockPos), LivingEntity::isSleeping);
/* 60 */     return !list.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\ValidateNearbyPoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */