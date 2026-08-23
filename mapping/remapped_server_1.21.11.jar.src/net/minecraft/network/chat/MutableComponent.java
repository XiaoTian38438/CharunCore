/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ 
/*     */ public final class MutableComponent
/*     */   implements Component
/*     */ {
/*     */   private final ComponentContents contents;
/*     */   private final List<Component> siblings;
/*     */   private Style style;
/*  17 */   private FormattedCharSequence visualOrderText = FormattedCharSequence.EMPTY;
/*     */   private Language decomposedWith;
/*     */   
/*     */   MutableComponent(ComponentContents paramComponentContents, List<Component> paramList, Style paramStyle) {
/*  21 */     this.contents = paramComponentContents;
/*  22 */     this.siblings = paramList;
/*  23 */     this.style = paramStyle;
/*     */   }
/*     */   
/*     */   public static MutableComponent create(ComponentContents paramComponentContents) {
/*  27 */     return new MutableComponent(paramComponentContents, Lists.newArrayList(), Style.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public ComponentContents getContents() {
/*  32 */     return this.contents;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Component> getSiblings() {
/*  37 */     return this.siblings;
/*     */   }
/*     */   
/*     */   public MutableComponent setStyle(Style paramStyle) {
/*  41 */     this.style = paramStyle;
/*  42 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Style getStyle() {
/*  47 */     return this.style;
/*     */   }
/*     */   
/*     */   public MutableComponent append(String paramString) {
/*  51 */     if (paramString.isEmpty()) {
/*  52 */       return this;
/*     */     }
/*  54 */     return append(Component.literal(paramString));
/*     */   }
/*     */   
/*     */   public MutableComponent append(Component paramComponent) {
/*  58 */     this.siblings.add(paramComponent);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(UnaryOperator<Style> paramUnaryOperator) {
/*  63 */     setStyle(paramUnaryOperator.apply(getStyle()));
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(Style paramStyle) {
/*  68 */     setStyle(paramStyle.applyTo(getStyle()));
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting... paramVarArgs) {
/*  73 */     setStyle(getStyle().applyFormats(paramVarArgs));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting paramChatFormatting) {
/*  78 */     setStyle(getStyle().applyFormat(paramChatFormatting));
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withColor(int paramInt) {
/*  83 */     setStyle(getStyle().withColor(paramInt));
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withoutShadow() {
/*  88 */     setStyle(getStyle().withoutShadow());
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormattedCharSequence getVisualOrderText() {
/*  94 */     Language language = Language.getInstance();
/*  95 */     if (this.decomposedWith != language) {
/*  96 */       this.visualOrderText = language.getVisualOrder(this);
/*  97 */       this.decomposedWith = language;
/*     */     } 
/*  99 */     return this.visualOrderText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/MutableComponent
/*     */     //   11: ifeq -> 69
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/MutableComponent
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   23: aload_2
/*     */     //   24: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 69
/*     */     //   35: aload_0
/*     */     //   36: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   39: aload_2
/*     */     //   40: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   43: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 69
/*     */     //   49: aload_0
/*     */     //   50: getfield siblings : Ljava/util/List;
/*     */     //   53: aload_2
/*     */     //   54: getfield siblings : Ljava/util/List;
/*     */     //   57: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   62: ifeq -> 69
/*     */     //   65: iconst_1
/*     */     //   66: goto -> 70
/*     */     //   69: iconst_0
/*     */     //   70: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     //   #105	-> 5
/*     */     //   #111	-> 7
/*     */     //   #108	-> 14
/*     */     //   #109	-> 27
/*     */     //   #110	-> 43
/*     */     //   #111	-> 57
/*     */     //   #108	-> 70
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     int i = 1;
/* 117 */     i = 31 * i + this.contents.hashCode();
/* 118 */     i = 31 * i + this.style.hashCode();
/* 119 */     i = 31 * i + this.siblings.hashCode();
/* 120 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     StringBuilder stringBuilder = new StringBuilder(this.contents.toString());
/* 126 */     boolean bool1 = !this.style.isEmpty() ? true : false;
/* 127 */     boolean bool2 = !this.siblings.isEmpty() ? true : false;
/* 128 */     if (bool1 || bool2) {
/* 129 */       stringBuilder.append('[');
/* 130 */       if (bool1) {
/* 131 */         stringBuilder.append("style=");
/* 132 */         stringBuilder.append(this.style);
/*     */       } 
/* 134 */       if (bool1 && bool2) {
/* 135 */         stringBuilder.append(", ");
/*     */       }
/* 137 */       if (bool2) {
/* 138 */         stringBuilder.append("siblings=");
/* 139 */         stringBuilder.append(this.siblings);
/*     */       } 
/* 141 */       stringBuilder.append(']');
/*     */     } 
/* 143 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\MutableComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */