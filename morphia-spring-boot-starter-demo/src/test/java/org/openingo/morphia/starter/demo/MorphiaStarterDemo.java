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

package org.openingo.morphia.starter.demo;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.internal.MorphiaCursor;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openingo.morphia.starter.demo.entity.App;
import org.openingo.morphia.starter.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MorphiaStarterDemo
 *
 * @author Qicz
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = App.class)
public class MorphiaStarterDemo {

    @Autowired
    private Datastore datastore;

    @Test
    public void testSaveUser() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {

            User user = new User();
            user.setUsername("用户名UserName" + i);
            user.setPassword(Base64.getEncoder().encodeToString((i+"哈哈").getBytes()));
            user.setAddress("Address"+i);
            users.add(user);
        }
        this.datastore.save(users);
    }

    @Test
    public void testListUser() {
        List<User> users1 = this.datastore.createQuery(User.class).find().toList();
        System.out.println(users1);
    }

    @Test
    public void testUpdateUser() {
        Query<User> query = this.datastore.createQuery(User.class).search("用户名UserName12");
        UpdateOperations<User> set = this.datastore.createUpdateOperations(User.class).set("username", "username12用户名UserName12");
        this.datastore.update(query, set);
    }
}
