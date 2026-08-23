/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VariantUtils
/*    */ {
/*    */   public static final String TAG_VARIANT = "variant";
/*    */   
/*    */   public static <T> Holder<T> getDefaultOrAny(RegistryAccess paramRegistryAccess, ResourceKey<T> paramResourceKey) {
/* 23 */     Registry registry = paramRegistryAccess.lookupOrThrow(paramResourceKey.registryKey());
/* 24 */     Objects.requireNonNull(registry); return registry.get(paramResourceKey).or(registry::getAny).orElseThrow();
/*    */   }
/*    */   
/*    */   public static <T> Holder<T> getAny(RegistryAccess paramRegistryAccess, ResourceKey<? extends Registry<T>> paramResourceKey) {
/* 28 */     return paramRegistryAccess.lookupOrThrow(paramResourceKey).getAny().orElseThrow();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> void writeVariant(ValueOutput paramValueOutput, Holder<T> paramHolder) {
/* 35 */     paramHolder.unwrapKey().ifPresent(paramResourceKey -> paramValueOutput.store("variant", Identifier.CODEC, paramResourceKey.identifier()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Optional<Holder<T>> readVariant(ValueInput paramValueInput, ResourceKey<? extends Registry<T>> paramResourceKey) {
/* 44 */     Objects.requireNonNull(paramValueInput.lookup()); return paramValueInput.read("variant", Identifier.CODEC).map(paramIdentifier -> ResourceKey.create(paramResourceKey, paramIdentifier)).flatMap(paramValueInput.lookup()::get);
/*    */   }
/*    */   
/*    */   public static <T extends PriorityProvider<SpawnContext, ?>> Optional<Holder.Reference<T>> selectVariantToSpawn(SpawnContext paramSpawnContext, ResourceKey<Registry<T>> paramResourceKey) {
/* 48 */     ServerLevelAccessor serverLevelAccessor = paramSpawnContext.level();
/* 49 */     Stream<Holder.Reference<T>> stream = serverLevelAccessor.registryAccess().lookupOrThrow(paramResourceKey).listElements();
/* 50 */     return PriorityProvider.pick(stream, Holder::value, serverLevelAccessor.getRandom(), paramSpawnContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\variant\VariantUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */