/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public interface ComponentContents
/*    */ {
/*    */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/* 13 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 17 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default MutableComponent resolve(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/* 21 */     return MutableComponent.create(this);
/*    */   }
/*    */   
/*    */   MapCodec<? extends ComponentContents> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ComponentContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */