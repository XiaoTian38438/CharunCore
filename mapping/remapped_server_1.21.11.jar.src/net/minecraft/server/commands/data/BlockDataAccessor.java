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
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.TagValueInput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BlockDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 33 */   static final SimpleCommandExceptionType ERROR_NOT_A_BLOCK_ENTITY = new SimpleCommandExceptionType((Message)Component.translatable("commands.data.block.invalid"));
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = paramString -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException {
/* 38 */         BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(param1CommandContext, argPrefix + "Pos");
/* 39 */         BlockEntity blockEntity = ((CommandSourceStack)param1CommandContext.getSource()).getLevel().getBlockEntity(blockPos);
/* 40 */         if (blockEntity == null) {
/* 41 */           throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
/*    */         }
/* 43 */         return new BlockDataAccessor(blockEntity, blockPos);
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function) {
/* 48 */         return param1ArgumentBuilder.then(Commands.literal("block").then(param1Function.apply(Commands.argument(argPrefix + "Pos", (ArgumentType)BlockPosArgument.blockPos()))));
/*    */       }
/*    */     };
/*    */   
/*    */   private final BlockEntity entity;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public BlockDataAccessor(BlockEntity paramBlockEntity, BlockPos paramBlockPos) {
/* 56 */     this.entity = paramBlockEntity;
/* 57 */     this.pos = paramBlockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag paramCompoundTag) {
/* 62 */     BlockState blockState = this.entity.getLevel().getBlockState(this.pos);
/* 63 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.entity.problemPath(), LOGGER); 
/* 64 */     try { this.entity.loadWithComponents(TagValueInput.create((ProblemReporter)scopedCollector, (HolderLookup.Provider)this.entity.getLevel().registryAccess(), paramCompoundTag));
/*    */       
/* 66 */       this.entity.setChanged();
/* 67 */       this.entity.getLevel().sendBlockUpdated(this.pos, blockState, blockState, 3);
/* 68 */       scopedCollector.close(); }
/*    */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*    */       catch (Throwable throwable1)
/*    */       { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 73 */      } public CompoundTag getData() { return this.entity.saveWithFullMetadata((HolderLookup.Provider)this.entity.getLevel().registryAccess()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 78 */     return (Component)Component.translatable("commands.data.block.modified", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag paramTag) {
/* 83 */     return (Component)Component.translatable("commands.data.block.query", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), NbtUtils.toPrettyComponent(paramTag) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt) {
/* 88 */     return (Component)Component.translatable("commands.data.block.get", new Object[] { paramNbtPath.asString(), Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramDouble) }), Integer.valueOf(paramInt) });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\BlockDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */