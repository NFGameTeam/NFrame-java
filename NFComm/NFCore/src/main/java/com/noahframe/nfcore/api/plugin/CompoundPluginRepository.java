/*
 * Copyright 2012 Decebal Suiu
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Decebal Suiu
 * @author Mário Franco
 */
public class CompoundPluginRepository implements PluginRepository {

    private final PluginRepository[] repositories;

    public CompoundPluginRepository(PluginRepository... repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<File> getPluginArchives() {
        List<File> file = new ArrayList<>();
        for (PluginRepository repository : repositories) {
            file.addAll(repository.getPluginArchives());
        }

        return file;
    }

    @Override
    public boolean deletePluginArchive(String pluginPath) {
        for (PluginRepository repository : repositories) {
            if (repository.deletePluginArchive(pluginPath)) {
                return true;
            }
        }

        return false;
    }

}
