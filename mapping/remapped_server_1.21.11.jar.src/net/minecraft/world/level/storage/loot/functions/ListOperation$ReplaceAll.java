/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
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
/*    */ public class ReplaceAll
/*    */   implements ListOperation
/*    */ {
/* 68 */   public static final ReplaceAll INSTANCE = new ReplaceAll();
/* 69 */   public static final MapCodec<ReplaceAll> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ListOperation.Type mode() {
/* 76 */     return ListOperation.Type.REPLACE_ALL;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> List<T> apply(List<T> paramList1, List<T> paramList2, int paramInt) {
/* 81 */     return paramList2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$ReplaceAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */