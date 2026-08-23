/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
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
/*    */ class null
/*    */   implements PlayerDetector.EntitySelector
/*    */ {
/*    */   public List<ServerPlayer> getPlayers(ServerLevel paramServerLevel, Predicate<? super Player> paramPredicate) {
/* 58 */     return paramServerLevel.getPlayers(paramPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends Entity> List<T> getEntities(ServerLevel paramServerLevel, EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate) {
/* 63 */     return paramServerLevel.getEntities(paramEntityTypeTest, paramAABB, paramPredicate);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */