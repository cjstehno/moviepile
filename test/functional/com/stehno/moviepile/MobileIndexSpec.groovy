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

import com.stehno.moviepile.pages.MobileIndexPage
import com.stehno.moviepile.pages.MobileMoviePage
import com.stehno.moviepile.pages.filters.MobileTitlesPage
import com.stehno.moviepile.pages.movies.MobileMoviesPage
import geb.spock.GebReportingSpec

class MobileIndexSpec extends GebReportingSpec {

    def 'Find a Movie by Title'(){
        when:
            to MobileIndexPage
        then:
            at MobileIndexPage

        when:
            findLinkFor('Titles').click()
        then:
            waitFor {
                at MobileTitlesPage
            }

        when:
            filterList.findLinkFor('A').click()
        then:
            waitFor {
                at MobileMoviesPage
                verifyActiveFilter 'A'
            }

        when:
            findMovieLink('Ace Ventura: Pet Detective').click()
        then:
            waitFor {
                at MobileMoviePage
                verifyMovieTitle 'Ace Ventura: Pet Detective'
            }
    }
}
