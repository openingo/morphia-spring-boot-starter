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

package org.openingo.boot.morphia.configuration;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MorphiaAutoConfiguration
 * @author Qicz
 */
@Configuration
@EnableConfigurationProperties(MorphiaConfigurationProperties.class)
public class MorphiaAutoConfiguration {

    private final MorphiaConfigurationProperties properties;

    public MorphiaAutoConfiguration(MorphiaConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(Datastore.class)
    public Datastore datastore() {
        Morphia morphia = new Morphia();
        if (properties.getMapPackage() != null) {
            morphia.mapPackage(properties.getMapPackage());
        }
        MongoClientOptions options = new MongoClientOptions.Builder().build();
        MongoClient mongoClient;
        if (properties.getUsername() != null && properties.getPassword() != null) {
            // with password
            MongoCredential credential = MongoCredential.createCredential(
                    properties.getUsername(),
                    properties.getAuthDatabase(),
                    properties.getPassword().toCharArray());
            mongoClient = new MongoClient(new ServerAddress(properties.getHost(), properties.getPort()), credential, options);
        } else {
            // non password
            mongoClient = new MongoClient(new ServerAddress(properties.getHost(), properties.getPort()));
        }
        Datastore datastore = morphia.createDatastore(mongoClient, properties.getDatabase());
        // building @Indexed
        datastore.ensureIndexes();
        return datastore;
    }
}
