/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class StatType<T>
/*    */   implements Iterable<Stat<T>> {
/*    */   private final Registry<T> registry;
/* 15 */   private final Map<T, Stat<T>> map = new IdentityHashMap<>();
/*    */   
/*    */   private final Component displayName;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec;
/*    */   
/*    */   public StatType(Registry<T> paramRegistry, Component paramComponent) {
/* 21 */     this.registry = paramRegistry;
/* 22 */     this.displayName = paramComponent;
/* 23 */     this.streamCodec = ByteBufCodecs.registry(paramRegistry.key()).map(this::get, Stat::getValue);
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec() {
/* 27 */     return this.streamCodec;
/*    */   }
/*    */   
/*    */   public boolean contains(T paramT) {
/* 31 */     return this.map.containsKey(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stat<T> get(T paramT, StatFormatter paramStatFormatter) {
/* 36 */     return this.map.computeIfAbsent(paramT, paramObject -> new Stat(this, paramObject, paramStatFormatter));
/*    */   }
/*    */   
/*    */   public Registry<T> getRegistry() {
/* 40 */     return this.registry;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Stat<T>> iterator() {
/* 45 */     return this.map.values().iterator();
/*    */   }
/*    */   
/*    */   public Stat<T> get(T paramT) {
/* 49 */     return get(paramT, StatFormatter.DEFAULT);
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 53 */     return this.displayName;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\StatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */