/*    */ package net.minecraft.server.dialog.body;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public interface DialogBody {
/*    */   public static final Codec<DialogBody> DIALOG_BODY_CODEC;
/*    */   
/*    */   static {
/* 12 */     DIALOG_BODY_CODEC = BuiltInRegistries.DIALOG_BODY_TYPE.byNameCodec().dispatch(DialogBody::mapCodec, paramMapCodec -> paramMapCodec);
/*    */   } MapCodec<? extends DialogBody> mapCodec();
/* 14 */   public static final Codec<List<DialogBody>> COMPACT_LIST_CODEC = ExtraCodecs.compactListCodec(DIALOG_BODY_CODEC);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\body\DialogBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */