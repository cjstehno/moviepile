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
import com.stehno.moviepile.pages.filters.MobileReleaseYearsPage
import geb.spock.GebReportingSpec

/**
 *
 */
class MobileReleaseYearSpec extends GebReportingSpec {

    def 'Verify Movie Count for Release Years'(){
        when:
            to MobileIndexPage

        then:
            at MobileIndexPage

        when:
            findLinkFor('Release Years').click()

        then:
            waitFor {
                at MobileReleaseYearsPage
            }

        expect:
            filterList.findListCountFor('1946') == 6
    }
}
