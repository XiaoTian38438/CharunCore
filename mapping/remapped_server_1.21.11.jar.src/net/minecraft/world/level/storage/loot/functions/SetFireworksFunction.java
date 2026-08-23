/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworksFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ListOperation.StandAlone.<T>codec(FireworkExplosion.CODEC, 256).optionalFieldOf("explosions").forGetter(()), (App)ExtraCodecs.UNSIGNED_BYTE.optionalFieldOf("flight_duration").forGetter(()))).apply((Applicative)paramInstance, SetFireworksFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetFireworksFunction> CODEC;
/* 22 */   public static final Fireworks DEFAULT_VALUE = new Fireworks(0, List.of());
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<FireworkExplosion>> explosions;
/*    */   private final Optional<Integer> flightDuration;
/*    */   
/*    */   protected SetFireworksFunction(List<LootItemCondition> paramList, Optional<ListOperation.StandAlone<FireworkExplosion>> paramOptional, Optional<Integer> paramOptional1) {
/* 28 */     super(paramList);
/* 29 */     this.explosions = paramOptional;
/* 30 */     this.flightDuration = paramOptional1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 35 */     paramItemStack.update(DataComponents.FIREWORKS, DEFAULT_VALUE, this::apply);
/* 36 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private Fireworks apply(Fireworks paramFireworks) {
/* 41 */     Objects.requireNonNull(paramFireworks); return new Fireworks(((Integer)this.flightDuration.orElseGet(paramFireworks::flightDuration)).intValue(), this.explosions
/* 42 */         .<List>map(paramStandAlone -> paramStandAlone.apply(paramFireworks.explosions())).orElse(paramFireworks.explosions()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetFireworksFunction> getType() {
/* 48 */     return LootItemFunctions.SET_FIREWORKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworksFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */