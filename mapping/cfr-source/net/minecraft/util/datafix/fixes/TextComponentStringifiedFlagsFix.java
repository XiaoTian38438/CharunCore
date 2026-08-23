/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.util.datafix.fixes.References;

public class TextComponentStringifiedFlagsFix
extends DataFix {
    public TextComponentStringifiedFlagsFix(Schema schema) {
        super(schema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(References.TEXT_COMPONENT);
        return this.fixTypeEverywhere("TextComponentStringyFlagsFix", type, dynamicOps -> pair -> pair.mapSecond(either -> either.mapRight(pair -> pair.mapSecond(pair2 -> pair2.mapSecond(pair -> pair.mapSecond(dynamic -> dynamic.update("bold", TextComponentStringifiedFlagsFix::stringToBool).update("italic", TextComponentStringifiedFlagsFix::stringToBool).update("underlined", TextComponentStringifiedFlagsFix::stringToBool).update("strikethrough", TextComponentStringifiedFlagsFix::stringToBool).update("obfuscated", TextComponentStringifiedFlagsFix::stringToBool)))))));
    }

    private static <T> Dynamic<T> stringToBool(Dynamic<T> dynamic) {
        Optional<String> optional = dynamic.asString().result();
        if (optional.isPresent()) {
            return dynamic.createBoolean(Boolean.parseBoolean(optional.get()));
        }
        return dynamic;
    }
}

