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

package com.stehno.moviepile.controller

import com.stehno.moviepile.ActorService
import com.stehno.moviepile.MovieService
import com.stehno.moviepile.domain.*
import grails.web.RequestParameter

/**
 *  Entry point for the mobile interface.
 */
class MobileController {

    private static final def DEFAULT_POSTER = '/images/nocover.jpg'

    MovieService movieService
    ActorService actorService

    /**
     * Renders the main entry point, the index page with its appropriate data.
     *
     * @return the populated model object
     */
    def index(){
        // FIXME: service?
        int unitCount = StorageSlot.withCriteria {
            projections {
                countDistinct 'name'
            }
        }[0]

        [
            counts:[
                titles: movieService.listValidTitleLetters().size(),
                genres: Genre.count(),
                actors: Actor.count(),
                years: movieService.listValidReleaseYears().size(),
                units: unitCount
            ]
        ]
    }

    /**
     * Renders either a list of valid title letters or a list of movies for a specified title letter.
     */
    def letter(){
        def selectedLetter = params.id
        if( selectedLetter ){
            def movies = movieService.listMoviesWithTitleLetter(selectedLetter)
            render view:'movies', model:[ filter:params.id, movies:movies ]

        } else {
            def movieTitleLetters = movieService.listValidTitleLetters()

            // TODO: investigate adding counts to this list query, rather than as separate

            renderFilters 'Titles', 'letter', movieTitleLetters.collect { letter->
                [ id:letter, label:letter, count:movieService.countMoviesWithTitleLetter(letter) ]
            }
        }
    }

    def genre(){
        if( params.id ){
            def genre = Genre.get(params.id)

            def movies = (genre.movies as List).sort { it.title }

            render view:'movies', model:[ filter: genre.name, movies:movies ]

        } else {
            def genres = Genre.listOrderByName()

            renderFilters 'Genres', 'genre', genres.collect { gen->
                [ id:gen.id, label:gen.name, count:gen.movies.size() ]
            }
        }
    }

    def actorLetter( @RequestParameter('id') String selectedLetter ){
        if( selectedLetter ){
            def actors = actorService.listActorsWithNameLetter(selectedLetter)
            renderFilters "$selectedLetter Actors", 'actor', actors.collect { act->
                [ id:act.id, label:act.displayName, count:act.movies.size() ]
            }

        } else {
            def lettersAndCounts = actorService.listAndCountValidActorNameLetters()
            renderFilters 'Actors', 'actorLetter', lettersAndCounts.collect { letter, count->
                [ id:letter, label:letter, count:count ]
            }
        }
    }

    def actor(){
        def actor = Actor.get(params.id)
        render view:'movies', model:[ filter:actor.displayName, movies:(actor.movies as List).sort { it.title } ]
    }

    def unit(){
        if(params.id){
            def movies = [] as Set<Movie>
            StorageSlot.findAllByName(params.id).each { slot->
                movies.addAll( slot.movies )
            }

            movies.sort { it.title }

            render view:'movies', model:[ filter:params.id, movies:movies ]

        } else {
            def unitsNames = [] as Set<String>
            StorageSlot.listOrderByName().each { u->
                unitsNames << u.name
            }

            def units = unitsNames.collect { u->
                [ id:u, label:u, count:StorageSlot.countByName(u) ]
            }

            renderFilters 'Storage', 'unit', units
        }
    }

    def year(){
        if( params.id){
            def movies = Movie.findAllByReleaseYear(params.id as Integer, [sort:'title'])
            render view:'movies', model:[ filter:params.id, movies:movies ]

        } else {
            renderFilters 'Release Years', 'year', movieService.listValidReleaseYears().collect { y->
                [ id:y, label:y, count:Movie.countByReleaseYear(y) ]
            }
        }
    }

    def movie(){
        [
            movie:Movie.get(params.id as Long)
        ]
    }

    def poster(){
        def poster = Poster.get(params.id)
        if( !poster ){
            response.outputStream.withStream { it << servletContext.getResource(DEFAULT_POSTER).getBytes() }
        } else {
            response.outputStream.withStream { it << poster.content }
        }
    }

    // FIXME: re-implement search functionality
//    def search(){
//        [:]
//    }
//
//    def searchResults(){
//        def criteria = params.criteria
//
//        def movies = Movie.search().list {
//            if(criteria){
//                should {
//                    def query = criteria.toLowerCase() + '*'
//                    wildcard 'title', query
//                    wildcard 'description', query
//                    wildcard 'releaseYear', query
////                    wildcard 'mpaaRating', query
////                    wildcard 'format', query
//                    wildcard 'runtime', query
////                    wildcard 'broadcast', query
//                }
//            }
//
//            sort 'title'
//        }
//
//        render view:'movies', model:[ filter:'Search Results', movies:movies ]
//    }

    private void renderFilters( String name, String action, List items ){
        render view:'filters', model:[ filter:name, filterAction:action, items:items ]
    }
}
