/*
 * Copyright (c) 2012 Christopher J. Stehno (chris@stehno.com)
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

import com.stehno.moviepile.security.Role
import com.stehno.moviepile.security.User
import com.stehno.moviepile.security.UserRole

class BootStrap {

    def init = { servletContext ->
        if( !User.findByUsername('admin') ){
            def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
            new Role(authority: 'ROLE_USER').save(flush: true)

            def testUser = new User(username: 'admin', enabled: true, password: 'admin')
            testUser.save(flush: true)

            UserRole.create testUser, adminRole, true
        }
    }

    def destroy = {
    }
}
