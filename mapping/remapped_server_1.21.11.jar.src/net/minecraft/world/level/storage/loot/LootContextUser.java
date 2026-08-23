/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ public interface LootContextUser
/*    */ {
/*    */   default Set<ContextKey<?>> getReferencedContextParams() {
/*  9 */     return Set.of();
/*    */   }
/*    */   
/*    */   default void validate(ValidationContext paramValidationContext) {
/* 13 */     paramValidationContext.validateContextUsage(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootContextUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */