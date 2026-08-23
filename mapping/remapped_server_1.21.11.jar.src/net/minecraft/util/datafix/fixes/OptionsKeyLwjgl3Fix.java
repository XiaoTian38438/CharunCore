/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class OptionsKeyLwjgl3Fix extends DataFix {
/*     */   public static final String KEY_UNKNOWN = "key.unknown";
/*     */   
/*     */   public OptionsKeyLwjgl3Fix(Schema paramSchema, boolean paramBoolean) {
/*  17 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */   private static final Int2ObjectMap<String> MAP;
/*     */   static {
/*  21 */     MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> {
/*     */           paramInt2ObjectOpenHashMap.put(0, "key.unknown");
/*     */           paramInt2ObjectOpenHashMap.put(11, "key.0");
/*     */           paramInt2ObjectOpenHashMap.put(2, "key.1");
/*     */           paramInt2ObjectOpenHashMap.put(3, "key.2");
/*     */           paramInt2ObjectOpenHashMap.put(4, "key.3");
/*     */           paramInt2ObjectOpenHashMap.put(5, "key.4");
/*     */           paramInt2ObjectOpenHashMap.put(6, "key.5");
/*     */           paramInt2ObjectOpenHashMap.put(7, "key.6");
/*     */           paramInt2ObjectOpenHashMap.put(8, "key.7");
/*     */           paramInt2ObjectOpenHashMap.put(9, "key.8");
/*     */           paramInt2ObjectOpenHashMap.put(10, "key.9");
/*     */           paramInt2ObjectOpenHashMap.put(30, "key.a");
/*     */           paramInt2ObjectOpenHashMap.put(40, "key.apostrophe");
/*     */           paramInt2ObjectOpenHashMap.put(48, "key.b");
/*     */           paramInt2ObjectOpenHashMap.put(43, "key.backslash");
/*     */           paramInt2ObjectOpenHashMap.put(14, "key.backspace");
/*     */           paramInt2ObjectOpenHashMap.put(46, "key.c");
/*     */           paramInt2ObjectOpenHashMap.put(58, "key.caps.lock");
/*     */           paramInt2ObjectOpenHashMap.put(51, "key.comma");
/*     */           paramInt2ObjectOpenHashMap.put(32, "key.d");
/*     */           paramInt2ObjectOpenHashMap.put(211, "key.delete");
/*     */           paramInt2ObjectOpenHashMap.put(208, "key.down");
/*     */           paramInt2ObjectOpenHashMap.put(18, "key.e");
/*     */           paramInt2ObjectOpenHashMap.put(207, "key.end");
/*     */           paramInt2ObjectOpenHashMap.put(28, "key.enter");
/*     */           paramInt2ObjectOpenHashMap.put(13, "key.equal");
/*     */           paramInt2ObjectOpenHashMap.put(1, "key.escape");
/*     */           paramInt2ObjectOpenHashMap.put(33, "key.f");
/*     */           paramInt2ObjectOpenHashMap.put(59, "key.f1");
/*     */           paramInt2ObjectOpenHashMap.put(68, "key.f10");
/*     */           paramInt2ObjectOpenHashMap.put(87, "key.f11");
/*     */           paramInt2ObjectOpenHashMap.put(88, "key.f12");
/*     */           paramInt2ObjectOpenHashMap.put(100, "key.f13");
/*     */           paramInt2ObjectOpenHashMap.put(101, "key.f14");
/*     */           paramInt2ObjectOpenHashMap.put(102, "key.f15");
/*     */           paramInt2ObjectOpenHashMap.put(103, "key.f16");
/*     */           paramInt2ObjectOpenHashMap.put(104, "key.f17");
/*     */           paramInt2ObjectOpenHashMap.put(105, "key.f18");
/*     */           paramInt2ObjectOpenHashMap.put(113, "key.f19");
/*     */           paramInt2ObjectOpenHashMap.put(60, "key.f2");
/*     */           paramInt2ObjectOpenHashMap.put(61, "key.f3");
/*     */           paramInt2ObjectOpenHashMap.put(62, "key.f4");
/*     */           paramInt2ObjectOpenHashMap.put(63, "key.f5");
/*     */           paramInt2ObjectOpenHashMap.put(64, "key.f6");
/*     */           paramInt2ObjectOpenHashMap.put(65, "key.f7");
/*     */           paramInt2ObjectOpenHashMap.put(66, "key.f8");
/*     */           paramInt2ObjectOpenHashMap.put(67, "key.f9");
/*     */           paramInt2ObjectOpenHashMap.put(34, "key.g");
/*     */           paramInt2ObjectOpenHashMap.put(41, "key.grave.accent");
/*     */           paramInt2ObjectOpenHashMap.put(35, "key.h");
/*     */           paramInt2ObjectOpenHashMap.put(199, "key.home");
/*     */           paramInt2ObjectOpenHashMap.put(23, "key.i");
/*     */           paramInt2ObjectOpenHashMap.put(210, "key.insert");
/*     */           paramInt2ObjectOpenHashMap.put(36, "key.j");
/*     */           paramInt2ObjectOpenHashMap.put(37, "key.k");
/*     */           paramInt2ObjectOpenHashMap.put(82, "key.keypad.0");
/*     */           paramInt2ObjectOpenHashMap.put(79, "key.keypad.1");
/*     */           paramInt2ObjectOpenHashMap.put(80, "key.keypad.2");
/*     */           paramInt2ObjectOpenHashMap.put(81, "key.keypad.3");
/*     */           paramInt2ObjectOpenHashMap.put(75, "key.keypad.4");
/*     */           paramInt2ObjectOpenHashMap.put(76, "key.keypad.5");
/*     */           paramInt2ObjectOpenHashMap.put(77, "key.keypad.6");
/*     */           paramInt2ObjectOpenHashMap.put(71, "key.keypad.7");
/*     */           paramInt2ObjectOpenHashMap.put(72, "key.keypad.8");
/*     */           paramInt2ObjectOpenHashMap.put(73, "key.keypad.9");
/*     */           paramInt2ObjectOpenHashMap.put(78, "key.keypad.add");
/*     */           paramInt2ObjectOpenHashMap.put(83, "key.keypad.decimal");
/*     */           paramInt2ObjectOpenHashMap.put(181, "key.keypad.divide");
/*     */           paramInt2ObjectOpenHashMap.put(156, "key.keypad.enter");
/*     */           paramInt2ObjectOpenHashMap.put(141, "key.keypad.equal");
/*     */           paramInt2ObjectOpenHashMap.put(55, "key.keypad.multiply");
/*     */           paramInt2ObjectOpenHashMap.put(74, "key.keypad.subtract");
/*     */           paramInt2ObjectOpenHashMap.put(38, "key.l");
/*     */           paramInt2ObjectOpenHashMap.put(203, "key.left");
/*     */           paramInt2ObjectOpenHashMap.put(56, "key.left.alt");
/*     */           paramInt2ObjectOpenHashMap.put(26, "key.left.bracket");
/*     */           paramInt2ObjectOpenHashMap.put(29, "key.left.control");
/*     */           paramInt2ObjectOpenHashMap.put(42, "key.left.shift");
/*     */           paramInt2ObjectOpenHashMap.put(219, "key.left.win");
/*     */           paramInt2ObjectOpenHashMap.put(50, "key.m");
/*     */           paramInt2ObjectOpenHashMap.put(12, "key.minus");
/*     */           paramInt2ObjectOpenHashMap.put(49, "key.n");
/*     */           paramInt2ObjectOpenHashMap.put(69, "key.num.lock");
/*     */           paramInt2ObjectOpenHashMap.put(24, "key.o");
/*     */           paramInt2ObjectOpenHashMap.put(25, "key.p");
/*     */           paramInt2ObjectOpenHashMap.put(209, "key.page.down");
/*     */           paramInt2ObjectOpenHashMap.put(201, "key.page.up");
/*     */           paramInt2ObjectOpenHashMap.put(197, "key.pause");
/*     */           paramInt2ObjectOpenHashMap.put(52, "key.period");
/*     */           paramInt2ObjectOpenHashMap.put(183, "key.print.screen");
/*     */           paramInt2ObjectOpenHashMap.put(16, "key.q");
/*     */           paramInt2ObjectOpenHashMap.put(19, "key.r");
/*     */           paramInt2ObjectOpenHashMap.put(205, "key.right");
/*     */           paramInt2ObjectOpenHashMap.put(184, "key.right.alt");
/*     */           paramInt2ObjectOpenHashMap.put(27, "key.right.bracket");
/*     */           paramInt2ObjectOpenHashMap.put(157, "key.right.control");
/*     */           paramInt2ObjectOpenHashMap.put(54, "key.right.shift");
/*     */           paramInt2ObjectOpenHashMap.put(220, "key.right.win");
/*     */           paramInt2ObjectOpenHashMap.put(31, "key.s");
/*     */           paramInt2ObjectOpenHashMap.put(70, "key.scroll.lock");
/*     */           paramInt2ObjectOpenHashMap.put(39, "key.semicolon");
/*     */           paramInt2ObjectOpenHashMap.put(53, "key.slash");
/*     */           paramInt2ObjectOpenHashMap.put(57, "key.space");
/*     */           paramInt2ObjectOpenHashMap.put(20, "key.t");
/*     */           paramInt2ObjectOpenHashMap.put(15, "key.tab");
/*     */           paramInt2ObjectOpenHashMap.put(22, "key.u");
/*     */           paramInt2ObjectOpenHashMap.put(200, "key.up");
/*     */           paramInt2ObjectOpenHashMap.put(47, "key.v");
/*     */           paramInt2ObjectOpenHashMap.put(17, "key.w");
/*     */           paramInt2ObjectOpenHashMap.put(45, "key.x");
/*     */           paramInt2ObjectOpenHashMap.put(21, "key.y");
/*     */           paramInt2ObjectOpenHashMap.put(44, "key.z");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 159 */     return fixTypeEverywhereTyped("OptionsKeyLwjgl3Fix", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsKeyLwjgl3Fix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */