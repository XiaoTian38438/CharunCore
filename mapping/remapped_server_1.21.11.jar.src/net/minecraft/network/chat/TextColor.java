/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TextColor
/*    */ {
/*    */   private static final String CUSTOM_COLOR_PREFIX = "#";
/* 19 */   public static final Codec<TextColor> CODEC = Codec.STRING.comapFlatMap(TextColor::parseColor, TextColor::serialize);
/*    */   private static final Map<ChatFormatting, TextColor> LEGACY_FORMAT_TO_COLOR;
/*    */   private static final Map<String, TextColor> NAMED_COLORS;
/*    */   
/*    */   static {
/* 24 */     LEGACY_FORMAT_TO_COLOR = (Map<ChatFormatting, TextColor>)Stream.<ChatFormatting>of(ChatFormatting.values()).filter(ChatFormatting::isColor).collect(ImmutableMap.toImmutableMap(Function.identity(), paramChatFormatting -> new TextColor(paramChatFormatting.getColor().intValue(), paramChatFormatting.getName())));
/* 25 */     NAMED_COLORS = (Map<String, TextColor>)LEGACY_FORMAT_TO_COLOR.values().stream().collect(ImmutableMap.toImmutableMap(paramTextColor -> paramTextColor.name, Function.identity()));
/*    */   }
/*    */   
/*    */   private final int value;
/*    */   private final String name;
/*    */   
/*    */   private TextColor(int paramInt, String paramString) {
/* 32 */     this.value = paramInt & 0xFFFFFF;
/* 33 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   private TextColor(int paramInt) {
/* 37 */     this.value = paramInt & 0xFFFFFF;
/* 38 */     this.name = null;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 42 */     return this.value;
/*    */   }
/*    */   
/*    */   public String serialize() {
/* 46 */     return (this.name != null) ? this.name : formatValue();
/*    */   }
/*    */   
/*    */   private String formatValue() {
/* 50 */     return String.format(Locale.ROOT, "#%06X", new Object[] { Integer.valueOf(this.value) });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 55 */     if (this == paramObject) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 59 */       return false;
/*    */     }
/* 61 */     TextColor textColor = (TextColor)paramObject;
/* 62 */     return (this.value == textColor.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 67 */     return Objects.hash(new Object[] { Integer.valueOf(this.value), this.name });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return serialize();
/*    */   }
/*    */   
/*    */   public static TextColor fromLegacyFormat(ChatFormatting paramChatFormatting) {
/* 76 */     return LEGACY_FORMAT_TO_COLOR.get(paramChatFormatting);
/*    */   }
/*    */   
/*    */   public static TextColor fromRgb(int paramInt) {
/* 80 */     return new TextColor(paramInt);
/*    */   }
/*    */   
/*    */   public static DataResult<TextColor> parseColor(String paramString) {
/* 84 */     if (paramString.startsWith("#")) {
/*    */       try {
/* 86 */         int i = Integer.parseInt(paramString.substring(1), 16);
/* 87 */         if (i < 0 || i > 16777215) {
/* 88 */           return DataResult.error(() -> "Color value out of range: " + paramString);
/*    */         }
/* 90 */         return DataResult.success(fromRgb(i), Lifecycle.stable());
/* 91 */       } catch (NumberFormatException numberFormatException) {
/* 92 */         return DataResult.error(() -> "Invalid color value: " + paramString);
/*    */       } 
/*    */     }
/* 95 */     TextColor textColor = NAMED_COLORS.get(paramString);
/* 96 */     if (textColor == null) {
/* 97 */       return DataResult.error(() -> "Invalid color name: " + paramString);
/*    */     }
/* 99 */     return DataResult.success(textColor, Lifecycle.stable());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\TextColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */