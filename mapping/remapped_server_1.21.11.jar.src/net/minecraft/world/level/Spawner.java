/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.component.TypedEntityData;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ 
/*    */ 
/*    */ public interface Spawner
/*    */ {
/*    */   static void appendHoverText(TypedEntityData<BlockEntityType<?>> paramTypedEntityData, Consumer<Component> paramConsumer, String paramString) {
/* 19 */     Component component = getSpawnEntityDisplayName(paramTypedEntityData, paramString);
/*    */     
/* 21 */     if (component != null) {
/* 22 */       paramConsumer.accept(component);
/*    */     } else {
/* 24 */       paramConsumer.accept(CommonComponents.EMPTY);
/* 25 */       paramConsumer.accept(Component.translatable("block.minecraft.spawner.desc1").withStyle(ChatFormatting.GRAY));
/* 26 */       paramConsumer.accept(CommonComponents.space().append((Component)Component.translatable("block.minecraft.spawner.desc2").withStyle(ChatFormatting.BLUE)));
/*    */     } 
/*    */   }
/*    */   
/*    */   static Component getSpawnEntityDisplayName(TypedEntityData<BlockEntityType<?>> paramTypedEntityData, String paramString) {
/* 31 */     if (paramTypedEntityData == null) {
/* 32 */       return null;
/*    */     }
/* 34 */     return paramTypedEntityData.getUnsafe().getCompound(paramString)
/* 35 */       .flatMap(paramCompoundTag -> paramCompoundTag.getCompound("entity"))
/* 36 */       .flatMap(paramCompoundTag -> paramCompoundTag.read("id", EntityType.CODEC))
/* 37 */       .map(paramEntityType -> Component.translatable(paramEntityType.getDescriptionId()).withStyle(ChatFormatting.GRAY))
/* 38 */       .orElse(null);
/*    */   }
/*    */   
/*    */   void setEntityId(EntityType<?> paramEntityType, RandomSource paramRandomSource);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\Spawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */