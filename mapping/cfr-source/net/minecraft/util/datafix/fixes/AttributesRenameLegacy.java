/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.function.UnaryOperator;
import net.minecraft.util.datafix.fixes.References;

public class AttributesRenameLegacy
extends DataFix {
    private final String name;
    private final UnaryOperator<String> renames;

    public AttributesRenameLegacy(Schema schema, String string, UnaryOperator<String> unaryOperator) {
        super(schema, false);
        this.name = string;
        this.renames = unaryOperator;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder<?> opticFinder = type.findField("tag");
        return TypeRewriteRule.seq(this.fixTypeEverywhereTyped(this.name + " (ItemStack)", type, typed -> typed.updateTyped(opticFinder, this::fixItemStackTag)), this.fixTypeEverywhereTyped(this.name + " (Entity)", this.getInputSchema().getType(References.ENTITY), this::fixEntity), this.fixTypeEverywhereTyped(this.name + " (Player)", this.getInputSchema().getType(References.PLAYER), this::fixEntity));
    }

    private Dynamic<?> fixName(Dynamic<?> dynamic) {
        return DataFixUtils.orElse(dynamic.asString().result().map(this.renames).map(dynamic::createString), dynamic);
    }

    private Typed<?> fixItemStackTag(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.update("AttributeModifiers", dynamic -> DataFixUtils.orElse(dynamic.asStreamOpt().result().map(stream -> stream.map(dynamic -> dynamic.update("AttributeName", this::fixName))).map(dynamic::createList), dynamic)));
    }

    private Typed<?> fixEntity(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.update("Attributes", dynamic -> DataFixUtils.orElse(dynamic.asStreamOpt().result().map(stream -> stream.map(dynamic -> dynamic.update("Name", this::fixName))).map(dynamic::createList), dynamic)));
    }
}

