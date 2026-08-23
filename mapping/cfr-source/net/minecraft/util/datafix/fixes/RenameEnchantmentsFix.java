/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class RenameEnchantmentsFix
extends DataFix {
    final String name;
    final Map<String, String> renames;

    public RenameEnchantmentsFix(Schema schema, String string, Map<String, String> map) {
        super(schema, false);
        this.name = string;
        this.renames = map;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder<?> opticFinder = type.findField("tag");
        return this.fixTypeEverywhereTyped(this.name, type, typed2 -> typed2.updateTyped(opticFinder, typed -> typed.update(DSL.remainderFinder(), this::fixTag)));
    }

    private Dynamic<?> fixTag(Dynamic<?> dynamic) {
        dynamic = this.fixEnchantmentList(dynamic, "Enchantments");
        dynamic = this.fixEnchantmentList(dynamic, "StoredEnchantments");
        return dynamic;
    }

    private Dynamic<?> fixEnchantmentList(Dynamic<?> dynamic2, String string) {
        return dynamic2.update(string, dynamic -> dynamic.asStreamOpt().map(stream -> stream.map(dynamic -> dynamic.update("id", dynamic2 -> dynamic2.asString().map(string -> dynamic.createString(this.renames.getOrDefault(NamespacedSchema.ensureNamespaced(string), (String)string))).mapOrElse(Function.identity(), error -> dynamic2)))).map(dynamic::createList).mapOrElse(Function.identity(), error -> dynamic));
    }
}

