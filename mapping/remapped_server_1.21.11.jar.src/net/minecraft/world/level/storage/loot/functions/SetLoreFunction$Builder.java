/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
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
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<SetLoreFunction.Builder>
/*    */ {
/* 65 */   private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/* 66 */   private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/* 67 */   private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */   
/*    */   public Builder setMode(ListOperation paramListOperation) {
/* 70 */     this.mode = paramListOperation;
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setResolutionContext(LootContext.EntityTarget paramEntityTarget) {
/* 75 */     this.resolutionContext = Optional.of(paramEntityTarget);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addLine(Component paramComponent) {
/* 80 */     this.lore.add(paramComponent);
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 86 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 91 */     return new SetLoreFunction(getConditions(), (List<Component>)this.lore.build(), this.mode, this.resolutionContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */