/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public class SignText {
/*     */   static {
/*  22 */     LINES_CODEC = ComponentSerialization.CODEC.listOf().comapFlatMap(paramList -> Util.fixedSize(paramList, 4).map(()), paramArrayOfComponent -> List.of(paramArrayOfComponent[0], paramArrayOfComponent[1], paramArrayOfComponent[2], paramArrayOfComponent[3]));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  27 */     DIRECT_CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)LINES_CODEC.fieldOf("messages").forGetter(()), (App)LINES_CODEC.lenientOptionalFieldOf("filtered_messages").forGetter(SignText::filteredMessages), (App)DyeColor.CODEC.fieldOf("color").orElse(DyeColor.BLACK).forGetter(()), (App)Codec.BOOL.fieldOf("has_glowing_text").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)paramInstance, SignText::load));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Codec<Component[]> LINES_CODEC;
/*     */   
/*     */   public static final Codec<SignText> DIRECT_CODEC;
/*     */   public static final int LINES = 4;
/*     */   private final Component[] messages;
/*     */   private final Component[] filteredMessages;
/*     */   private final DyeColor color;
/*     */   private final boolean hasGlowingText;
/*     */   private FormattedCharSequence[] renderMessages;
/*     */   private boolean renderMessagedFiltered;
/*     */   
/*     */   public SignText() {
/*  43 */     this(emptyMessages(), emptyMessages(), DyeColor.BLACK, false);
/*     */   }
/*     */   
/*     */   public SignText(Component[] paramArrayOfComponent1, Component[] paramArrayOfComponent2, DyeColor paramDyeColor, boolean paramBoolean) {
/*  47 */     this.messages = paramArrayOfComponent1;
/*  48 */     this.filteredMessages = paramArrayOfComponent2;
/*  49 */     this.color = paramDyeColor;
/*  50 */     this.hasGlowingText = paramBoolean;
/*     */   }
/*     */   
/*     */   private static Component[] emptyMessages() {
/*  54 */     return new Component[] { CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY };
/*     */   }
/*     */   
/*     */   private static SignText load(Component[] paramArrayOfComponent, Optional<Component[]> paramOptional, DyeColor paramDyeColor, boolean paramBoolean) {
/*  58 */     return new SignText(paramArrayOfComponent, paramOptional.orElse(Arrays.<Component>copyOf(paramArrayOfComponent, paramArrayOfComponent.length)), paramDyeColor, paramBoolean);
/*     */   }
/*     */   
/*     */   public boolean hasGlowingText() {
/*  62 */     return this.hasGlowingText;
/*     */   }
/*     */   
/*     */   public SignText setHasGlowingText(boolean paramBoolean) {
/*  66 */     if (paramBoolean == this.hasGlowingText) {
/*  67 */       return this;
/*     */     }
/*  69 */     return new SignText(this.messages, this.filteredMessages, this.color, paramBoolean);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/*  73 */     return this.color;
/*     */   }
/*     */   
/*     */   public SignText setColor(DyeColor paramDyeColor) {
/*  77 */     if (paramDyeColor == getColor()) {
/*  78 */       return this;
/*     */     }
/*  80 */     return new SignText(this.messages, this.filteredMessages, paramDyeColor, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public Component getMessage(int paramInt, boolean paramBoolean) {
/*  84 */     return getMessages(paramBoolean)[paramInt];
/*     */   }
/*     */   
/*     */   public SignText setMessage(int paramInt, Component paramComponent) {
/*  88 */     return setMessage(paramInt, paramComponent, paramComponent);
/*     */   }
/*     */   
/*     */   public SignText setMessage(int paramInt, Component paramComponent1, Component paramComponent2) {
/*  92 */     Component[] arrayOfComponent1 = Arrays.<Component>copyOf(this.messages, this.messages.length);
/*  93 */     Component[] arrayOfComponent2 = Arrays.<Component>copyOf(this.filteredMessages, this.filteredMessages.length);
/*  94 */     arrayOfComponent1[paramInt] = paramComponent1;
/*  95 */     arrayOfComponent2[paramInt] = paramComponent2;
/*  96 */     return new SignText(arrayOfComponent1, arrayOfComponent2, this.color, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public boolean hasMessage(Player paramPlayer) {
/* 100 */     return Arrays.<Component>stream(getMessages(paramPlayer.isTextFilteringEnabled())).anyMatch(paramComponent -> !paramComponent.getString().isEmpty());
/*     */   }
/*     */   
/*     */   public Component[] getMessages(boolean paramBoolean) {
/* 104 */     return paramBoolean ? this.filteredMessages : this.messages;
/*     */   }
/*     */   
/*     */   public FormattedCharSequence[] getRenderMessages(boolean paramBoolean, Function<Component, FormattedCharSequence> paramFunction) {
/* 108 */     if (this.renderMessages == null || this.renderMessagedFiltered != paramBoolean) {
/* 109 */       this.renderMessagedFiltered = paramBoolean;
/* 110 */       this.renderMessages = new FormattedCharSequence[4];
/* 111 */       for (byte b = 0; b < 4; b++) {
/* 112 */         this.renderMessages[b] = paramFunction.apply(getMessage(b, paramBoolean));
/*     */       }
/*     */     } 
/* 115 */     return this.renderMessages;
/*     */   }
/*     */   
/*     */   private Optional<Component[]> filteredMessages() {
/* 119 */     for (byte b = 0; b < 4; b++) {
/* 120 */       if (!this.filteredMessages[b].equals(this.messages[b])) {
/* 121 */         return (Optional)Optional.of(this.filteredMessages);
/*     */       }
/*     */     } 
/* 124 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public boolean hasAnyClickCommands(Player paramPlayer) {
/* 128 */     for (Component component : getMessages(paramPlayer.isTextFilteringEnabled())) {
/* 129 */       Style style = component.getStyle();
/* 130 */       ClickEvent clickEvent = style.getClickEvent();
/* 131 */       if (clickEvent != null && clickEvent.action() == ClickEvent.Action.RUN_COMMAND) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SignText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */