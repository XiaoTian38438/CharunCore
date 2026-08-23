/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class Stat<T>
/*    */   extends ObjectiveCriteria
/*    */ {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, Stat<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.STAT_TYPE).dispatch(Stat::getType, StatType::streamCodec);
/*    */   
/*    */   private final StatFormatter formatter;
/*    */   private final T value;
/*    */   private final StatType<T> type;
/*    */   
/*    */   protected Stat(StatType<T> paramStatType, T paramT, StatFormatter paramStatFormatter) {
/* 22 */     super(buildName(paramStatType, paramT));
/* 23 */     this.type = paramStatType;
/* 24 */     this.formatter = paramStatFormatter;
/* 25 */     this.value = paramT;
/*    */   }
/*    */   
/*    */   public static <T> String buildName(StatType<T> paramStatType, T paramT) {
/* 29 */     return locationToKey(BuiltInRegistries.STAT_TYPE.getKey(paramStatType)) + ":" + locationToKey(BuiltInRegistries.STAT_TYPE.getKey(paramStatType));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String locationToKey(Identifier paramIdentifier) {
/* 35 */     return paramIdentifier.toString().replace(':', '.');
/*    */   }
/*    */   
/*    */   public StatType<T> getType() {
/* 39 */     return this.type;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 43 */     return this.value;
/*    */   }
/*    */   
/*    */   public String format(int paramInt) {
/* 47 */     return this.formatter.format(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 52 */     return (this == paramObject || (paramObject instanceof Stat && Objects.equals(getName(), ((Stat)paramObject).getName())));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return getName().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "Stat{name=" + getName() + ", formatter=" + String.valueOf(this.formatter) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\Stat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */