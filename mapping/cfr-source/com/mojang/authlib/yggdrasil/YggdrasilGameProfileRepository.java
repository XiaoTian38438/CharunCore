/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Iterables
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib.yggdrasil;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import com.mojang.authlib.yggdrasil.response.NameAndId;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilGameProfileRepository
implements GameProfileRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilGameProfileRepository.class);
    private static final int ENTRIES_PER_PAGE = 2;
    private static final int MAX_FAIL_COUNT = 3;
    private static final int DELAY_BETWEEN_PAGES = 100;
    private static final int DELAY_BETWEEN_FAILURES = 750;
    private final MinecraftClient client;
    private final URL searchPageUrl;
    private final String nameLookupUrl;

    public YggdrasilGameProfileRepository(Proxy proxy, Environment environment) {
        this.client = MinecraftClient.unauthenticated(proxy);
        this.searchPageUrl = HttpAuthenticationService.constantURL(environment.profilesHost() + "/minecraft/profile/lookup/bulk/byname");
        this.nameLookupUrl = environment.profilesHost() + "/minecraft/profile/lookup/name/";
    }

    @Override
    public void findProfilesByNames(String[] stringArray, ProfileLookupCallback profileLookupCallback) {
        Set set = Arrays.stream(stringArray).filter(string -> !Strings.isNullOrEmpty((String)string)).collect(Collectors.toSet());
        boolean bl = false;
        for (List list : Iterables.partition(set, (int)2)) {
            boolean bl2;
            List<String> list2 = list.stream().map(YggdrasilGameProfileRepository::normalizeName).toList();
            int n = 0;
            do {
                bl2 = false;
                try {
                    ProfileSearchResultsResponse profileSearchResultsResponse = this.client.post(this.searchPageUrl, list2, ProfileSearchResultsResponse.class);
                    List<NameAndId> list3 = profileSearchResultsResponse != null ? profileSearchResultsResponse.profiles() : List.of();
                    n = 0;
                    LOGGER.debug("Page {} returned {} results, parsing", (Object)0, (Object)list3.size());
                    Object object = new HashSet(list3.size());
                    Iterator iterator = list3.iterator();
                    while (iterator.hasNext()) {
                        Object object2 = (NameAndId)iterator.next();
                        LOGGER.debug("Successfully looked up profile {}", object2);
                        object.add(YggdrasilGameProfileRepository.normalizeName(((NameAndId)object2).name()));
                        profileLookupCallback.onProfileLookupSucceeded(((NameAndId)object2).name(), ((NameAndId)object2).id());
                    }
                    for (Object object2 : list) {
                        if (object.contains(YggdrasilGameProfileRepository.normalizeName((String)object2))) continue;
                        LOGGER.debug("Couldn't find profile {}", object2);
                        profileLookupCallback.onProfileLookupFailed((String)object2, new ProfileNotFoundException("Server did not find the requested profile"));
                    }
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException interruptedException) {}
                }
                catch (MinecraftClientException minecraftClientException) {
                    if (++n == 3) {
                        for (Object object : list) {
                            LOGGER.debug("Couldn't find profile {} because of a server error", object);
                            profileLookupCallback.onProfileLookupFailed((String)object, minecraftClientException.toAuthenticationException());
                        }
                        continue;
                    }
                    try {
                        Thread.sleep(750L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    bl2 = true;
                }
            } while (bl2);
        }
    }

    @Override
    public Optional<NameAndId> findProfileByName(String string) {
        try {
            return Optional.ofNullable(this.client.get(HttpAuthenticationService.constantURL(this.nameLookupUrl + YggdrasilGameProfileRepository.normalizeName(string)), NameAndId.class));
        }
        catch (MinecraftClientException minecraftClientException) {
            LOGGER.warn("Couldn't find profile with name: {}", (Object)string, (Object)minecraftClientException);
            return Optional.empty();
        }
    }

    private static String normalizeName(String string) {
        return string.toLowerCase(Locale.ROOT);
    }
}

