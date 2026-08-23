/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public class KeybindContents implements ComponentContents {
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.fieldOf("keybind").forGetter(())).apply((Applicative)paramInstance, KeybindContents::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<KeybindContents> MAP_CODEC;
/*    */   private final String name;
/*    */   private Supplier<Component> nameResolver;
/*    */   
/*    */   public KeybindContents(String paramString) {
/* 24 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   private Component getNestedComponent() {
/* 28 */     if (this.nameResolver == null) {
/* 29 */       this.nameResolver = KeybindResolver.keyResolver.apply(this.name);
/*    */     }
/*    */     
/* 32 */     return this.nameResolver.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 37 */     return getNestedComponent().visit(paramContentConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/* 42 */     return getNestedComponent().visit(paramStyledContentConsumer, paramStyle);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: if_acmpne -> 7
/*    */     //   5: iconst_1
/*    */     //   6: ireturn
/*    */     //   7: aload_1
/*    */     //   8: instanceof net/minecraft/network/chat/contents/KeybindContents
/*    */     //   11: ifeq -> 37
/*    */     //   14: aload_1
/*    */     //   15: checkcast net/minecraft/network/chat/contents/KeybindContents
/*    */     //   18: astore_2
/*    */     //   19: aload_0
/*    */     //   20: getfield name : Ljava/lang/String;
/*    */     //   23: aload_2
/*    */     //   24: getfield name : Ljava/lang/String;
/*    */     //   27: invokevirtual equals : (Ljava/lang/Object;)Z
/*    */     //   30: ifeq -> 37
/*    */     //   33: iconst_1
/*    */     //   34: goto -> 38
/*    */     //   37: iconst_0
/*    */     //   38: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     //   #48	-> 5
/*    */     //   #51	-> 7
/*    */     //   #50	-> 14
/*    */     //   #51	-> 27
/*    */     //   #50	-> 38
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "keybind{" + this.name + "}";
/*    */   }
/*    */   
/*    */   public String getName() {
/* 65 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<KeybindContents> codec() {
/* 70 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\KeybindContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */