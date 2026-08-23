/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FillPlayerHead extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(())).apply((Applicative)paramInstance, FillPlayerHead::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<FillPlayerHead> CODEC;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public FillPlayerHead(List<LootItemCondition> paramList, LootContext.EntityTarget paramEntityTarget) {
/* 25 */     super(paramList);
/* 26 */     this.entityTarget = paramEntityTarget;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<FillPlayerHead> getType() {
/* 31 */     return LootItemFunctions.FILL_PLAYER_HEAD;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 36 */     return Set.of(this.entityTarget.contextParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 41 */     if (paramItemStack.is(Items.PLAYER_HEAD)) {
/* 42 */       Object object = paramLootContext.getOptionalParameter(this.entityTarget.contextParam()); if (object instanceof Player) { Player player = (Player)object;
/* 43 */         paramItemStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile())); }
/*    */     
/*    */     } 
/* 46 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> fillPlayerHead(LootContext.EntityTarget paramEntityTarget) {
/* 50 */     return simpleBuilder(paramList -> new FillPlayerHead(paramList, paramEntityTarget));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\FillPlayerHead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */