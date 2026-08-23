/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ 
/*    */ public interface Action {
/*    */   public static final Codec<Action> CODEC;
/*    */   
/*    */   MapCodec<? extends Action> codec();
/*    */   
/*    */   Optional<ClickEvent> createAction(Map<String, ValueGetter> paramMap);
/*    */   
/*    */   static {
/* 17 */     CODEC = BuiltInRegistries.DIALOG_ACTION_TYPE.byNameCodec().dispatch(Action::codec, paramMapCodec -> paramMapCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public static interface ValueGetter
/*    */   {
/*    */     String asTemplateSubstitution();
/*    */ 
/*    */     
/*    */     Tag asTag();
/*    */ 
/*    */     
/*    */     static Map<String, String> getAsTemplateSubstitutions(Map<String, ValueGetter> param1Map) {
/* 30 */       return Maps.transformValues(param1Map, ValueGetter::asTemplateSubstitution);
/*    */     }
/*    */     
/*    */     static ValueGetter of(final String value) {
/* 34 */       return new ValueGetter()
/*    */         {
/*    */           public String asTemplateSubstitution() {
/* 37 */             return value;
/*    */           }
/*    */ 
/*    */           
/*    */           public Tag asTag() {
/* 42 */             return (Tag)StringTag.valueOf(value);
/*    */           }
/*    */         };
/*    */     }
/*    */     
/*    */     static ValueGetter of(final Supplier<String> value) {
/* 48 */       return new ValueGetter()
/*    */         {
/*    */           public String asTemplateSubstitution() {
/* 51 */             return value.get();
/*    */           }
/*    */           
/*    */           public Tag asTag()
/*    */           {
/* 56 */             return (Tag)StringTag.valueOf(value.get()); } }; } } class null implements ValueGetter { public String asTemplateSubstitution() { return value; } public Tag asTag() { return (Tag)StringTag.valueOf(value); } } class null implements ValueGetter { public Tag asTag() { return (Tag)StringTag.valueOf(value.get()); }
/*    */ 
/*    */     
/*    */     public String asTemplateSubstitution() {
/*    */       return value.get();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\action\Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */