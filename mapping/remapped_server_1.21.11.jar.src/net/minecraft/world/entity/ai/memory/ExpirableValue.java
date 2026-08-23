/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpirableValue<T>
/*    */ {
/*    */   private final T value;
/*    */   private long timeToLive;
/*    */   
/*    */   public ExpirableValue(T paramT, long paramLong) {
/* 18 */     this.value = paramT;
/* 19 */     this.timeToLive = paramLong;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     if (canExpire()) {
/* 24 */       this.timeToLive--;
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T paramT) {
/* 29 */     return new ExpirableValue<>(paramT, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T paramT, long paramLong) {
/* 33 */     return new ExpirableValue<>(paramT, paramLong);
/*    */   }
/*    */   
/*    */   public long getTimeToLive() {
/* 37 */     return this.timeToLive;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 45 */     return (this.timeToLive <= 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return String.valueOf(this.value) + String.valueOf(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForDebug
/*    */   public boolean canExpire() {
/* 56 */     return (this.timeToLive != Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static <T> Codec<ExpirableValue<T>> codec(Codec<T> paramCodec) {
/* 60 */     return RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)paramCodec.fieldOf("value").forGetter(()), (App)Codec.LONG.lenientOptionalFieldOf("ttl").forGetter(())).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\memory\ExpirableValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */