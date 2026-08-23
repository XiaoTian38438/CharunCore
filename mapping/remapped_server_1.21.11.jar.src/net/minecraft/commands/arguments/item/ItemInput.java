/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.TypedDataComponent;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemInput {
/*    */   static {
/* 25 */     ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("arguments.item.overstacked", new Object[] { paramObject1, paramObject2 }));
/*    */   }
/*    */   private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG; private final Holder<Item> item;
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   public ItemInput(Holder<Item> paramHolder, DataComponentPatch paramDataComponentPatch) {
/* 31 */     this.item = paramHolder;
/* 32 */     this.components = paramDataComponentPatch;
/*    */   }
/*    */   
/*    */   public Item getItem() {
/* 36 */     return (Item)this.item.value();
/*    */   }
/*    */   
/*    */   public ItemStack createItemStack(int paramInt, boolean paramBoolean) throws CommandSyntaxException {
/* 40 */     ItemStack itemStack = new ItemStack(this.item, paramInt);
/* 41 */     itemStack.applyComponents(this.components);
/* 42 */     if (paramBoolean && paramInt > itemStack.getMaxStackSize()) {
/* 43 */       throw ERROR_STACK_TOO_BIG.create(getItemName(), Integer.valueOf(itemStack.getMaxStackSize()));
/*    */     }
/* 45 */     return itemStack;
/*    */   }
/*    */   
/*    */   public String serialize(HolderLookup.Provider paramProvider) {
/* 49 */     StringBuilder stringBuilder = new StringBuilder(getItemName());
/* 50 */     String str = serializeComponents(paramProvider);
/* 51 */     if (!str.isEmpty()) {
/* 52 */       stringBuilder.append('[');
/* 53 */       stringBuilder.append(str);
/* 54 */       stringBuilder.append(']');
/*    */     } 
/* 56 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   private String serializeComponents(HolderLookup.Provider paramProvider) {
/* 60 */     RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 61 */     return this.components.entrySet().stream()
/* 62 */       .flatMap(paramEntry -> {
/*    */           DataComponentType dataComponentType = (DataComponentType)paramEntry.getKey();
/*    */           
/*    */           Identifier identifier = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(dataComponentType);
/*    */           
/*    */           if (identifier == null) {
/*    */             return Stream.empty();
/*    */           }
/*    */           
/*    */           Optional optional = (Optional)paramEntry.getValue();
/*    */           
/*    */           if (optional.isPresent()) {
/*    */             TypedDataComponent typedDataComponent = TypedDataComponent.createUnchecked(dataComponentType, optional.get());
/*    */             return typedDataComponent.encodeValue(paramDynamicOps).result().stream().map(());
/*    */           } 
/*    */           return Stream.of("!" + identifier.toString());
/* 78 */         }).collect(Collectors.joining(String.valueOf(',')));
/*    */   }
/*    */   
/*    */   private String getItemName() {
/* 82 */     return this.item.unwrapKey().map(ResourceKey::identifier).orElseGet(() -> "unknown[" + String.valueOf(this.item) + "]").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\item\ItemInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */