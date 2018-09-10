/*
 * Copyright 2014 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.noahframe.nfcore.api.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.noahframe.nfcore.api.plugin.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation for PluginStatusProvider.
 *
 * @author Decebal Suiu
 * @author Mário Franco
 */
public class DefaultPluginStatusProvider implements PluginStatusProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultPluginStatusProvider.class);

    private final File pluginsDirectory;

    private List<String> enabledPlugins = new ArrayList<>();
    private List<String> disabledPlugins = new ArrayList<>();

    public DefaultPluginStatusProvider(File pluginsDirectory) {
        this.pluginsDirectory = pluginsDirectory;
        initialize();
    }

    private void initialize() {
        try {
            // create a list with plugin identifiers that should be only accepted by this manager (whitelist from plugins/enabled.txt file)
            enabledPlugins = FileUtils.readLines(new File(pluginsDirectory, "enabled.txt"), true);
            log.info("Enabled plugins: {}", enabledPlugins);

            // create a list with plugin identifiers that should not be accepted by this manager (blacklist from plugins/disabled.txt file)
            disabledPlugins = FileUtils.readLines(new File(pluginsDirectory, "disabled.txt"), true);
            log.info("Disabled plugins: {}", disabledPlugins);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isPluginDisabled(String pluginId) {
        if (disabledPlugins.contains(pluginId)) {
            return true;
        }

        return !enabledPlugins.isEmpty() && !enabledPlugins.contains(pluginId);
    }

    @Override
    public boolean disablePlugin(String pluginId) {
        if (disabledPlugins.add(pluginId)) {
            try {
                FileUtils.writeLines(disabledPlugins, new File(pluginsDirectory, "disabled.txt"));
            } catch (IOException e) {
                log.error("Failed to disable plugin {}", pluginId, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean enablePlugin(String pluginId) {
        try {
            if (disabledPlugins.remove(pluginId)) {
                FileUtils.writeLines(disabledPlugins, new File(pluginsDirectory, "disabled.txt"));
            }
        } catch (IOException e) {
            log.error("Failed to enable plugin {}", pluginId, e);
            return false;
        }
        return true;
    }

}
