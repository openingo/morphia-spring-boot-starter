/*
 * MIT License
 *
 * Copyright (c) 2020 OpeningO Co.,Ltd.
 *
 *    https://openingo.org
 *    contactus(at)openingo.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.openingo.morphia.config;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MorphiaAutoConfiguration
 * @author Qicz
 */
@Configuration
@EnableConfigurationProperties(MorphiaConfig.class)
public class MorphiaAutoConfiguration {

    private final MorphiaConfig morphiaConfig;

    public MorphiaAutoConfiguration(MorphiaConfig morphiaConfig) {
        this.morphiaConfig = morphiaConfig;
    }

    @Bean
    @ConditionalOnMissingBean(Datastore.class)
    public Datastore datastore() {
        Morphia morphia = new Morphia();
        if (morphiaConfig.getMapPackage() != null) {
            morphia.mapPackage(morphiaConfig.getMapPackage());
        }
        MongoClientOptions options = new MongoClientOptions.Builder().build();
        MongoClient mongoClient;
        if (morphiaConfig.getUsername() != null && morphiaConfig.getPassword() != null) {
            // with password
            MongoCredential credential = MongoCredential.createCredential(
                    morphiaConfig.getUsername(),
                    morphiaConfig.getAuthDatabase(),
                    morphiaConfig.getPassword().toCharArray());
            mongoClient = new MongoClient(new ServerAddress(morphiaConfig.getHost(), morphiaConfig.getPort()), credential, options);
        } else {
            // non password
            mongoClient = new MongoClient(new ServerAddress(morphiaConfig.getHost(), morphiaConfig.getPort()));
        }
        Datastore datastore = morphia.createDatastore(mongoClient, morphiaConfig.getDatabase());
        // building @Indexed
        datastore.ensureIndexes();
        return datastore;
    }
}
