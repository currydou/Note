package com.yodoo.android.baseutil.base;

/**
 * Created by libin-com on 2018/3/7.
 */

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ModuleService {
    @Inject
    public ModuleService() {

    }

    private Map<String, Object> moduleServices = new HashMap<>();

    public ModuleService registService(String key, Object service) {
        moduleServices.put(key, service);
        return this;
    }

    public Object getService(String key) {
        return moduleServices.get(key);
    }
}
