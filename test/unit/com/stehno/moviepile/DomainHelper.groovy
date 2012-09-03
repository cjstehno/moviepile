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

package com.stehno.moviepile

import com.stehno.moviepile.domain.Actor
import com.stehno.moviepile.domain.Movie

/**
 * Provides helper methods useful for writing unit tests with domain objects.
 * These methods are not for testing domain classes directly but for providing data collections
 * of domain objects useful in other tests.
 */
class DomainHelper {

    /**
     * Creates and saves movies based on the provided properties. This does not need to be a complete property
     * set since the validation is disabled.
     *
     * @param protos the list of prototype property maps
     */
    void provideMovies( protos ){
        protos.each {
            new Movie( it ).save(validate:false)
        }
    }

    /**
     * Creates and saves actors based on the provided properties. This does not need to be a complete property
     * set since the validation is disabled.
     *
     * @param protos the list of prototype property maps
     */
    void provideActors( protos ){
        protos.each {
            new Actor(it).save(validate:false)
        }
    }
}
