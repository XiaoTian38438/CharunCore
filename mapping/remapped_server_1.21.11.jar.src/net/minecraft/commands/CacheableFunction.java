/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public class CacheableFunction {
/* 11 */   public static final Codec<CacheableFunction> CODEC = Identifier.CODEC.xmap(CacheableFunction::new, CacheableFunction::getId);
/*    */   
/*    */   private final Identifier id;
/*    */   private boolean resolved;
/* 15 */   private Optional<CommandFunction<CommandSourceStack>> function = Optional.empty();
/*    */   
/*    */   public CacheableFunction(Identifier paramIdentifier) {
/* 18 */     this.id = paramIdentifier;
/*    */   }
/*    */   
/*    */   public Optional<CommandFunction<CommandSourceStack>> get(ServerFunctionManager paramServerFunctionManager) {
/* 22 */     if (!this.resolved) {
/* 23 */       this.function = paramServerFunctionManager.get(this.id);
/* 24 */       this.resolved = true;
/*    */     } 
/* 26 */     return this.function;
/*    */   }
/*    */   
/*    */   public Identifier getId() {
/* 30 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 35 */     if (paramObject == this) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (paramObject instanceof CacheableFunction) { CacheableFunction cacheableFunction = (CacheableFunction)paramObject; if (getId().equals(cacheableFunction.getId())); }  return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\CacheableFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */