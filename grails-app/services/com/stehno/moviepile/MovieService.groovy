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

import com.stehno.moviepile.domain.Movie

/**
 * Services related to movie instances.
 */
class MovieService {

    private static final IGNORED_WORDS = [ 'a', 'an', 'the', 'and' ]
    static transactional = true

    /**
     * Provides a distinct list of all release years available for at least one movie.
     *
     * @return a list of unique release years for movies
     */
    List<Movie> listValidReleaseYears(){
        Movie.withCriteria {
            projections {
                distinct 'releaseYear'
            }
            order('releaseYear', 'asc')
        }
    }

    /**
     * Provides a distinct list of all of the "starting letters" for all movie titles. The starting letter is the first
     * meaningful letter of the title.
     *
     * Rules:
     *  -   the first meaningful word (not: A, The, An, 'number')
     *  -   the first non-numeric letter
     *
     * @return a unique list of meaningful title letters.
     */
    List<String> listValidTitleLetters(){
        def movieTitles = Movie.withCriteria {
            projections {
                distinct 'title'
            }
            order 'title', 'asc'
        }

        def letters = [] as Set<String>
        movieTitles.each { title->
            letters << extractUsefulTitle(title)[0].toUpperCase()
        }

        // TODO: this could be a candidate for some caching

        return letters.toList().sort()
    }

    /**
     *  Provides a list of movies that start with the given letter. The results are determined in a similar manner to the
     *  listValidTitleLetters() method rules.
     *
     *  @param letter the letter to be used as the filter
     */
    List<Movie> listMoviesWithTitleLetter( final String letter ){
        def movies = Movie.withCriteria {
            ilike 'title', "%$letter%"
            order 'title', 'asc'
        }

        movies.findAll { movie->
            extractUsefulTitle(movie.title).toLowerCase().startsWith(letter.toLowerCase())
        }.sort {
            extractUsefulTitle( it.title )
        }
    }

    /**
     *
     * @param letter
     * @return
     */
    int countMoviesWithTitleLetter( final String letter ){
        def movies = Movie.withCriteria {
            ilike 'title', "%$letter%"
            order 'title', 'asc'
        }

        // TODO: can I integrate these functions into the criteria itself?

        movies.findAll { movie->
            extractUsefulTitle(movie.title).toLowerCase().startsWith(letter.toLowerCase())
        }.size()
    }

    private String extractUsefulTitle( final String title ){
        title.split(' ').find { word->
            !word.isNumber() && !(word.toLowerCase() in IGNORED_WORDS)
        } ?: title
    }
}
