/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class DefaultedMappedRegistry<T>
/*    */   extends MappedRegistry<T>
/*    */   implements DefaultedRegistry<T> {
/*    */   private final Identifier defaultKey;
/*    */   private Holder.Reference<T> defaultValue;
/*    */   
/*    */   public DefaultedMappedRegistry(String paramString, ResourceKey<? extends Registry<T>> paramResourceKey, Lifecycle paramLifecycle, boolean paramBoolean) {
/* 16 */     super(paramResourceKey, paramLifecycle, paramBoolean);
/* 17 */     this.defaultKey = Identifier.parse(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, RegistrationInfo paramRegistrationInfo) {
/* 22 */     Holder.Reference<T> reference = super.register(paramResourceKey, paramT, paramRegistrationInfo);
/* 23 */     if (this.defaultKey.equals(paramResourceKey.identifier())) {
/* 24 */       this.defaultValue = reference;
/*    */     }
/* 26 */     return reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(T paramT) {
/* 31 */     int i = super.getId(paramT);
/* 32 */     return (i == -1) ? super.getId(this.defaultValue.value()) : i;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getKey(T paramT) {
/* 37 */     Identifier identifier = super.getKey(paramT);
/* 38 */     return (identifier == null) ? this.defaultKey : identifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getValue(Identifier paramIdentifier) {
/* 43 */     T t = super.getValue(paramIdentifier);
/* 44 */     return (t == null) ? this.defaultValue.value() : t;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> getOptional(Identifier paramIdentifier) {
/* 49 */     return Optional.ofNullable(super.getValue(paramIdentifier));
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Holder.Reference<T>> getAny() {
/* 54 */     return Optional.ofNullable(this.defaultValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public T byId(int paramInt) {
/* 59 */     T t = super.byId(paramInt);
/* 60 */     return (t == null) ? this.defaultValue.value() : t;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Holder.Reference<T>> getRandom(RandomSource paramRandomSource) {
/* 65 */     return super.getRandom(paramRandomSource).or(() -> Optional.of(this.defaultValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getDefaultKey() {
/* 70 */     return this.defaultKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\DefaultedMappedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */