/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.modules;

import org.apache.james.GuiceModuleTestExtension;
import org.apache.james.modules.blobstore.BlobStoreChoosingConfiguration;
import org.apache.james.modules.objectstorage.PayloadCodecFactory;
import org.apache.james.modules.objectstorage.guice.DockerSwiftTestRule;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.inject.Module;
import com.google.inject.util.Modules;

public class SwiftBlobStoreExtension implements GuiceModuleTestExtension {

    private final DockerSwiftTestRule swiftRule;

    public SwiftBlobStoreExtension() {
        this.swiftRule = new DockerSwiftTestRule();
    }

    public SwiftBlobStoreExtension(PayloadCodecFactory payloadCodecFactory) {
        this.swiftRule = new DockerSwiftTestRule(payloadCodecFactory);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        swiftRule.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        swiftRule.stop();
    }

    @Override
    public Module getModule() {
        return Modules.override(swiftRule.getModule())
            .with(binder -> binder.bind(BlobStoreChoosingConfiguration.class)
                .toInstance(BlobStoreChoosingConfiguration.objectStorage()));
    }
}
