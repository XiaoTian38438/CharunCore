/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Locale;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.NbtPredicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.TagValueInput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntityDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 33 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType((Message)Component.translatable("commands.data.entity.invalid"));
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = paramString -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException {
/* 38 */         return new EntityDataAccessor(EntityArgument.getEntity(param1CommandContext, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function) {
/* 43 */         return param1ArgumentBuilder.then(Commands.literal("entity").then(param1Function.apply(Commands.argument(arg, (ArgumentType)EntityArgument.entity()))));
/*    */       }
/*    */     };
/*    */   
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityDataAccessor(Entity paramEntity) {
/* 50 */     this.entity = paramEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag paramCompoundTag) throws CommandSyntaxException {
/* 55 */     if (this.entity instanceof net.minecraft.world.entity.player.Player) {
/* 56 */       throw ERROR_NO_PLAYERS.create();
/*    */     }
/* 58 */     UUID uUID = this.entity.getUUID();
/* 59 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.entity.problemPath(), LOGGER); 
/* 60 */     try { this.entity.load(TagValueInput.create((ProblemReporter)scopedCollector, (HolderLookup.Provider)this.entity.registryAccess(), paramCompoundTag));
/* 61 */       this.entity.setUUID(uUID);
/* 62 */       scopedCollector.close(); }
/*    */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*    */       catch (Throwable throwable1)
/*    */       { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 67 */      } public CompoundTag getData() { return NbtPredicate.getEntityTagToCompare(this.entity); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 72 */     return (Component)Component.translatable("commands.data.entity.modified", new Object[] { this.entity.getDisplayName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag paramTag) {
/* 77 */     return (Component)Component.translatable("commands.data.entity.query", new Object[] { this.entity.getDisplayName(), NbtUtils.toPrettyComponent(paramTag) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt) {
/* 82 */     return (Component)Component.translatable("commands.data.entity.get", new Object[] { paramNbtPath.asString(), this.entity.getDisplayName(), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramDouble) }), Integer.valueOf(paramInt) });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\EntityDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */