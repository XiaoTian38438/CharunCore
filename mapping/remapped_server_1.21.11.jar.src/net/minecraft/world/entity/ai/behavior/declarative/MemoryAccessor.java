/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MemoryAccessor<F extends K1, Value>
/*    */ {
/*    */   private final Brain<?> brain;
/*    */   private final MemoryModuleType<Value> memoryType;
/*    */   private final App<F, Value> value;
/*    */   
/*    */   public MemoryAccessor(Brain<?> paramBrain, MemoryModuleType<Value> paramMemoryModuleType, App<F, Value> paramApp) {
/* 20 */     this.brain = paramBrain;
/* 21 */     this.memoryType = paramMemoryModuleType;
/* 22 */     this.value = paramApp;
/*    */   }
/*    */   
/*    */   public App<F, Value> value() {
/* 26 */     return this.value;
/*    */   }
/*    */   
/*    */   public void set(Value paramValue) {
/* 30 */     this.brain.setMemory(this.memoryType, Optional.of(paramValue));
/*    */   }
/*    */   
/*    */   public void setOrErase(Optional<Value> paramOptional) {
/* 34 */     this.brain.setMemory(this.memoryType, paramOptional);
/*    */   }
/*    */   
/*    */   public void setWithExpiry(Value paramValue, long paramLong) {
/* 38 */     this.brain.setMemoryWithExpiry(this.memoryType, paramValue, paramLong);
/*    */   }
/*    */   
/*    */   public void erase() {
/* 42 */     this.brain.eraseMemory(this.memoryType);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\declarative\MemoryAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */