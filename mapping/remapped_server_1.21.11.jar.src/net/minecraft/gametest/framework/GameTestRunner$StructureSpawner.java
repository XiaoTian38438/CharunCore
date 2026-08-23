/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
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
/*    */ public interface StructureSpawner
/*    */ {
/*    */   public static final StructureSpawner IN_PLACE;
/*    */   
/*    */   default void onBatchStart(ServerLevel paramServerLevel) {}
/*    */   
/*    */   static {
/* 47 */     IN_PLACE = (paramGameTestInfo -> Optional.<GameTestInfo>ofNullable(paramGameTestInfo.prepareTestStructure()).map(()));
/*    */   }
/*    */   
/*    */   public static final StructureSpawner NOT_SET = paramGameTestInfo -> Optional.empty();
/*    */   
/*    */   Optional<GameTestInfo> spawnStructure(GameTestInfo paramGameTestInfo);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestRunner$StructureSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */