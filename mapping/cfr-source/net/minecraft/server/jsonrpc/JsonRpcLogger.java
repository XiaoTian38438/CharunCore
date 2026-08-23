/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 */
package net.minecraft.server.jsonrpc;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import org.slf4j.Logger;

public class JsonRpcLogger {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PREFIX = "RPC Connection #{}: ";

    public void log(ClientInfo clientInfo, String string, Object ... objectArray) {
        if (objectArray.length == 0) {
            LOGGER.info(PREFIX + string, (Object)clientInfo.connectionId());
        } else {
            ArrayList<Object> arrayList = new ArrayList<Object>(Arrays.asList(objectArray));
            arrayList.addFirst(clientInfo.connectionId());
            LOGGER.info(PREFIX + string, arrayList.toArray());
        }
    }
}

