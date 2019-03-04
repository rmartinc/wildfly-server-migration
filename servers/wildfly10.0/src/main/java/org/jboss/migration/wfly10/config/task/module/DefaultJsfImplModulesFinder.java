/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.migration.wfly10.config.task.module;

import org.jboss.migration.core.jboss.JBossSubsystemNames;
import org.jboss.migration.core.jboss.ModulesMigrationTask;
import org.jboss.migration.core.task.TaskContext;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 * Finds modules referenced by JSF subsystem configs, as source of default JSF implementation.
 * @author emmartins
 */
public class DefaultJsfImplModulesFinder implements ConfigurationModulesMigrationTaskFactory.ModulesFinder {

    private static final String JSF_IMPL_MODULE_NAME = "org.jboss.as.jsf-injection:";

    @Override
    public String getElementLocalName() {
        return "subsystem";
    }

    @Override
    public void processElement(XMLStreamReader reader, ModulesMigrationTask.ModuleMigrator moduleMigrator, TaskContext context) throws IOException {
        final String namespaceURI = reader.getNamespaceURI();
        if (namespaceURI == null || !namespaceURI.startsWith("urn:jboss:domain:"+ JBossSubsystemNames.JSF)) {
            return;
        }
        final String moduleSlot = reader.getAttributeValue(null, "default-jsf-impl-slot");
        if (moduleSlot != null) {
            moduleMigrator.migrateModule(JSF_IMPL_MODULE_NAME + moduleSlot, "Referenced as the source of default JSF implementation", context);
        }
    }
}
