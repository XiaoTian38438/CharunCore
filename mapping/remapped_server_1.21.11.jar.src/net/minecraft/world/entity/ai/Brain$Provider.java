/*    */ package net.minecraft.world.entity.ai;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ import net.minecraft.world.entity.ai.sensing.SensorType;
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
/*    */ public final class Provider<E extends LivingEntity>
/*    */ {
/*    */   private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*    */   private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
/*    */   private final Codec<Brain<E>> codec;
/*    */   
/*    */   Provider(Collection<? extends MemoryModuleType<?>> paramCollection, Collection<? extends SensorType<? extends Sensor<? super E>>> paramCollection1) {
/* 60 */     this.memoryTypes = paramCollection;
/* 61 */     this.sensorTypes = paramCollection1;
/* 62 */     this.codec = Brain.codec(paramCollection, paramCollection1);
/*    */   }
/*    */   
/*    */   public Brain<E> makeBrain(Dynamic<?> paramDynamic) {
/* 66 */     Objects.requireNonNull(Brain.LOGGER); return this.codec.parse(paramDynamic).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain<>(this.memoryTypes, this.sensorTypes, ImmutableList.of(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\Brain$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */