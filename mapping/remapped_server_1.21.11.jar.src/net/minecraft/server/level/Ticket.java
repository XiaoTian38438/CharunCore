/*    */ package net.minecraft.server.level;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Ticket {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.TICKET_TYPE.byNameCodec().fieldOf("type").forGetter(Ticket::getType), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("level").forGetter(Ticket::getTicketLevel), (App)Codec.LONG.optionalFieldOf("ticks_left", Long.valueOf(0L)).forGetter(())).apply((Applicative)paramInstance, Ticket::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<Ticket> CODEC;
/*    */   
/*    */   private final TicketType type;
/*    */   private final int ticketLevel;
/*    */   private long ticksLeft;
/*    */   
/*    */   public Ticket(TicketType paramTicketType, int paramInt) {
/* 22 */     this(paramTicketType, paramInt, paramTicketType.timeout());
/*    */   }
/*    */   
/*    */   private Ticket(TicketType paramTicketType, int paramInt, long paramLong) {
/* 26 */     this.type = paramTicketType;
/* 27 */     this.ticketLevel = paramInt;
/* 28 */     this.ticksLeft = paramLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 33 */     return this.type.hasTimeout() ? ("Ticket[" + 
/* 34 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with " + this.ticksLeft + " ticks left ( out of" + this.type.timeout() + ")") : ("Ticket[" + 
/*    */       
/* 36 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with no timeout");
/*    */   }
/*    */   
/*    */   public TicketType getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getTicketLevel() {
/* 44 */     return this.ticketLevel;
/*    */   }
/*    */   
/*    */   public void resetTicksLeft() {
/* 48 */     this.ticksLeft = this.type.timeout();
/*    */   }
/*    */   
/*    */   public void decreaseTicksLeft() {
/* 52 */     if (this.type.hasTimeout()) {
/* 53 */       this.ticksLeft--;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isTimedOut() {
/* 58 */     return (this.type.hasTimeout() && this.ticksLeft < 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\Ticket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */