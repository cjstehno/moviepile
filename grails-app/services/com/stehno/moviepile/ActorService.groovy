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

/**
 * Services for working with Actors.
 */
class ActorService {

    static transactional = true

    /**
     * Lists the valid "actor letters", which is a unique set of the first letters of all the actors
     * last names. The resulting data will be a map of letters to the count of actors having that letter
     * as the first in their last name.
     *
     * The map keys will be sorted.
     *
     * @return a map of letters to counts or an empty map.
     */
    Map<String,Integer> listAndCountValidActorNameLetters(){
        def letters = [:] as TreeMap<String,Integer>

        Actor.list().each { act->
            def firstLetter = act.lastName[0].toUpperCase()
            letters[firstLetter] = ( letters[firstLetter] ?: 0 ) + 1
        }

        return letters
    }

    /**
     * Provides a list of the actors whose last name starts with the given letter (case insensitive).
     *
     * @param letter the letter
     * @return the actors whose last name starts with the given letter, or an empty list.
     */
    List<Actor> listActorsWithNameLetter( final String letter ){
        Actor.findAllByLastNameIlike( "$letter%", [sort:'lastName', order:'asc'] )
    }
}
