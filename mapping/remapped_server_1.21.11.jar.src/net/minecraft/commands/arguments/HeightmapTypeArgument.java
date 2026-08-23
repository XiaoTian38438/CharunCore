/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapTypeArgument
/*    */   extends StringRepresentableArgument<Heightmap.Types> {
/*    */   static {
/* 14 */     LOWER_CASE_CODEC = (Codec<Heightmap.Types>)StringRepresentable.fromEnumWithMapping(HeightmapTypeArgument::keptTypes, paramString -> paramString.toLowerCase(Locale.ROOT));
/*    */   } private static final Codec<Heightmap.Types> LOWER_CASE_CODEC;
/*    */   private static Heightmap.Types[] keptTypes() {
/* 17 */     return (Heightmap.Types[])Arrays.<Heightmap.Types>stream(Heightmap.Types.values()).filter(Heightmap.Types::keepAfterWorldgen).toArray(paramInt -> new Heightmap.Types[paramInt]);
/*    */   }
/*    */   
/*    */   private HeightmapTypeArgument() {
/* 21 */     super(LOWER_CASE_CODEC, HeightmapTypeArgument::keptTypes);
/*    */   }
/*    */   
/*    */   public static HeightmapTypeArgument heightmap() {
/* 25 */     return new HeightmapTypeArgument();
/*    */   }
/*    */   
/*    */   public static Heightmap.Types getHeightmap(CommandContext<CommandSourceStack> paramCommandContext, String paramString) {
/* 29 */     return (Heightmap.Types)paramCommandContext.getArgument(paramString, Heightmap.Types.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String convertId(String paramString) {
/* 34 */     return paramString.toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\HeightmapTypeArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */