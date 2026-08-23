/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*    */ 
/*    */ public class WaypointArgument {
/* 13 */   public static final SimpleCommandExceptionType ERROR_NOT_A_WAYPOINT = new SimpleCommandExceptionType((Message)Component.translatable("argument.waypoint.invalid"));
/*    */   
/*    */   public static WaypointTransmitter getWaypoint(CommandContext<CommandSourceStack> paramCommandContext, String paramString) throws CommandSyntaxException {
/* 16 */     Entity entity = ((EntitySelector)paramCommandContext.getArgument(paramString, EntitySelector.class)).findSingleEntity((CommandSourceStack)paramCommandContext.getSource());
/* 17 */     if (entity instanceof WaypointTransmitter) return (WaypointTransmitter)entity;
/*    */ 
/*    */     
/* 20 */     throw ERROR_NOT_A_WAYPOINT.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\WaypointArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */