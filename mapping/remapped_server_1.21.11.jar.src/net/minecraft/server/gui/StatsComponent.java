/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.util.Locale;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.Timer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ 
/*    */ public class StatsComponent
/*    */   extends JComponent {
/* 17 */   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*    */   
/* 19 */   private final int[] values = new int[256];
/*    */   private int vp;
/* 21 */   private final String[] msgs = new String[11];
/*    */   private final MinecraftServer server;
/*    */   private final Timer timer;
/*    */   
/*    */   public StatsComponent(MinecraftServer paramMinecraftServer) {
/* 26 */     this.server = paramMinecraftServer;
/* 27 */     setPreferredSize(new Dimension(456, 246));
/* 28 */     setMinimumSize(new Dimension(456, 246));
/* 29 */     setMaximumSize(new Dimension(456, 246));
/* 30 */     this.timer = new Timer(500, paramActionEvent -> tick());
/* 31 */     this.timer.start();
/* 32 */     setBackground(Color.BLACK);
/*    */   }
/*    */   
/*    */   private void tick() {
/* 36 */     long l = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/* 37 */     this.msgs[0] = "Memory use: " + l / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
/* 38 */     this.msgs[1] = "Avg tick: " + DECIMAL_FORMAT.format(this.server.getAverageTickTimeNanos() / TimeUtil.NANOSECONDS_PER_MILLISECOND) + " ms";
/* 39 */     this.values[this.vp++ & 0xFF] = (int)(l * 100L / Runtime.getRuntime().maxMemory());
/* 40 */     repaint();
/*    */   }
/*    */ 
/*    */   
/*    */   public void paint(Graphics paramGraphics) {
/* 45 */     paramGraphics.setColor(new Color(16777215));
/* 46 */     paramGraphics.fillRect(0, 0, 456, 246);
/*    */     byte b;
/* 48 */     for (b = 0; b < 'Ā'; b++) {
/* 49 */       int i = this.values[b + this.vp & 0xFF];
/* 50 */       paramGraphics.setColor(new Color(i + 28 << 16));
/* 51 */       paramGraphics.fillRect(b, 100 - i, 1, i);
/*    */     } 
/* 53 */     paramGraphics.setColor(Color.BLACK);
/* 54 */     for (b = 0; b < this.msgs.length; b++) {
/* 55 */       String str = this.msgs[b];
/* 56 */       if (str != null) {
/* 57 */         paramGraphics.drawString(str, 32, 116 + b * 16);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void close() {
/* 63 */     this.timer.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\gui\StatsComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */