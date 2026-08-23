/*     */ package net.minecraft.advancements;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class DisplayInfo {
/*     */   static {
/*  15 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ItemStack.STRICT_CODEC.fieldOf("icon").forGetter(DisplayInfo::getIcon), (App)ComponentSerialization.CODEC.fieldOf("title").forGetter(DisplayInfo::getTitle), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(DisplayInfo::getDescription), (App)ClientAsset.ResourceTexture.CODEC.optionalFieldOf("background").forGetter(DisplayInfo::getBackground), (App)AdvancementType.CODEC.optionalFieldOf("frame", AdvancementType.TASK).forGetter(DisplayInfo::getType), (App)Codec.BOOL.optionalFieldOf("show_toast", Boolean.valueOf(true)).forGetter(DisplayInfo::shouldShowToast), (App)Codec.BOOL.optionalFieldOf("announce_to_chat", Boolean.valueOf(true)).forGetter(DisplayInfo::shouldAnnounceChat), (App)Codec.BOOL.optionalFieldOf("hidden", Boolean.valueOf(false)).forGetter(DisplayInfo::isHidden)).apply((Applicative)paramInstance, DisplayInfo::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<DisplayInfo> CODEC;
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, DisplayInfo> STREAM_CODEC = StreamCodec.ofMember(DisplayInfo::serializeToNetwork, DisplayInfo::fromNetwork);
/*     */   
/*     */   private final Component title;
/*     */   private final Component description;
/*     */   private final ItemStack icon;
/*     */   private final Optional<ClientAsset.ResourceTexture> background;
/*     */   private final AdvancementType type;
/*     */   private final boolean showToast;
/*     */   private final boolean announceChat;
/*     */   private final boolean hidden;
/*     */   private float x;
/*     */   private float y;
/*     */   
/*     */   public DisplayInfo(ItemStack paramItemStack, Component paramComponent1, Component paramComponent2, Optional<ClientAsset.ResourceTexture> paramOptional, AdvancementType paramAdvancementType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  40 */     this.title = paramComponent1;
/*  41 */     this.description = paramComponent2;
/*  42 */     this.icon = paramItemStack;
/*  43 */     this.background = paramOptional;
/*  44 */     this.type = paramAdvancementType;
/*  45 */     this.showToast = paramBoolean1;
/*  46 */     this.announceChat = paramBoolean2;
/*  47 */     this.hidden = paramBoolean3;
/*     */   }
/*     */   
/*     */   public void setLocation(float paramFloat1, float paramFloat2) {
/*  51 */     this.x = paramFloat1;
/*  52 */     this.y = paramFloat2;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  56 */     return this.title;
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/*  60 */     return this.description;
/*     */   }
/*     */   
/*     */   public ItemStack getIcon() {
/*  64 */     return this.icon;
/*     */   }
/*     */   
/*     */   public Optional<ClientAsset.ResourceTexture> getBackground() {
/*  68 */     return this.background;
/*     */   }
/*     */   
/*     */   public AdvancementType getType() {
/*  72 */     return this.type;
/*     */   }
/*     */   
/*     */   public float getX() {
/*  76 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/*  80 */     return this.y;
/*     */   }
/*     */   
/*     */   public boolean shouldShowToast() {
/*  84 */     return this.showToast;
/*     */   }
/*     */   
/*     */   public boolean shouldAnnounceChat() {
/*  88 */     return this.announceChat;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/*  92 */     return this.hidden;
/*     */   }
/*     */   
/*     */   private void serializeToNetwork(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/*  96 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.title);
/*  97 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.description);
/*  98 */     ItemStack.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, this.icon);
/*  99 */     paramRegistryFriendlyByteBuf.writeEnum(this.type);
/* 100 */     int i = 0;
/* 101 */     if (this.background.isPresent()) {
/* 102 */       i |= 0x1;
/*     */     }
/* 104 */     if (this.showToast) {
/* 105 */       i |= 0x2;
/*     */     }
/* 107 */     if (this.hidden) {
/* 108 */       i |= 0x4;
/*     */     }
/* 110 */     paramRegistryFriendlyByteBuf.writeInt(i);
/* 111 */     Objects.requireNonNull(paramRegistryFriendlyByteBuf); this.background.map(ClientAsset::id).ifPresent(paramRegistryFriendlyByteBuf::writeIdentifier);
/* 112 */     paramRegistryFriendlyByteBuf.writeFloat(this.x);
/* 113 */     paramRegistryFriendlyByteBuf.writeFloat(this.y);
/*     */   }
/*     */   
/*     */   private static DisplayInfo fromNetwork(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 117 */     Component component1 = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 118 */     Component component2 = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 119 */     ItemStack itemStack = (ItemStack)ItemStack.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 120 */     AdvancementType advancementType = (AdvancementType)paramRegistryFriendlyByteBuf.readEnum(AdvancementType.class);
/* 121 */     int i = paramRegistryFriendlyByteBuf.readInt();
/* 122 */     Optional<T> optional = ((i & 0x1) != 0) ? Optional.<T>of((T)new ClientAsset.ResourceTexture(paramRegistryFriendlyByteBuf.readIdentifier())) : Optional.<T>empty();
/* 123 */     boolean bool1 = ((i & 0x2) != 0) ? true : false;
/* 124 */     boolean bool2 = ((i & 0x4) != 0) ? true : false;
/* 125 */     DisplayInfo displayInfo = new DisplayInfo(itemStack, component1, component2, (Optional)optional, advancementType, bool1, false, bool2);
/* 126 */     displayInfo.setLocation(paramRegistryFriendlyByteBuf.readFloat(), paramRegistryFriendlyByteBuf.readFloat());
/* 127 */     return displayInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\DisplayInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */