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

package com.stehno.moviepile.pages

import geb.Page

class MobileIndexPage extends Page {

    static url = 'http://localhost:8080/moviepile/mobile'

    static at = {
        $("h1",0).text() == 'MoviePile'
    }

    static content = {
        findLinkFor { linkText->
            $('a', text:linkText )
        }

        findListCountFor { linkText->
            $('a', text:linkText ).next().text() as Integer
        }
    }
}