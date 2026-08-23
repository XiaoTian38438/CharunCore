/*    */ package net.minecraft.world.entity.animal.parrot;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.TagValueOutput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class ShoulderRidingEntity extends TamableAnimal {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private static final int RIDE_COOLDOWN = 100;
/*    */   private int rideCooldownCounter;
/*    */   
/*    */   protected ShoulderRidingEntity(EntityType<? extends ShoulderRidingEntity> paramEntityType, Level paramLevel) {
/* 20 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public boolean setEntityOnShoulder(ServerPlayer paramServerPlayer) {
/* 24 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 25 */     try { TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, (HolderLookup.Provider)registryAccess());
/* 26 */       saveWithoutId((ValueOutput)tagValueOutput);
/* 27 */       tagValueOutput.putString("id", getEncodeId());
/*    */       
/* 29 */       if (paramServerPlayer.setEntityOnShoulder(tagValueOutput.buildResult()))
/* 30 */       { discard();
/* 31 */         boolean bool = true;
/*    */         
/* 33 */         scopedCollector.close(); return bool; }  scopedCollector.close(); } catch (Throwable throwable) { try { scopedCollector.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 35 */      return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 40 */     this.rideCooldownCounter++;
/* 41 */     super.tick();
/*    */   }
/*    */   
/*    */   public boolean canSitOnShoulder() {
/* 45 */     return (this.rideCooldownCounter > 100);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\parrot\ShoulderRidingEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */