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

import com.stehno.moviepile.domain.Actor
import com.stehno.moviepile.domain.Genre
import com.stehno.moviepile.domain.Movie
import com.stehno.moviepile.domain.Poster
import com.stehno.moviepile.domain.StorageSlot

/**
 *
 */
class MobileController {

    private static final def DEFAULT_POSTER = '/images/nocover.jpg'

    def index(){
        // FIXME: move these to service?

        def movieYears = Movie.executeQuery('select distinct(m.releaseYear) from Movie m order by m.releaseYear asc')

        // FIXME: need to account for a and the
        def movieTitleLetters = Movie.executeQuery('select distinct(substring(upper(m.title),1,1)) from Movie m').sort()


        int unitCount = StorageSlot.executeQuery('select distinct(name) from StorageSlot').size()

        [
            counts:[
                titles: movieTitleLetters.size(),
                genres: Genre.count(),
                actors: Actor.count(),
                years: movieYears.size(),
                units: unitCount
            ]
        ]
    }

    def letter(){
        def selectedLetter = params.id
        if( selectedLetter ){
            def movies = Movie.findAll('from Movie as m where substring(upper(m.title),1,1)=? order by m.title asc', [selectedLetter.toUpperCase()])

            render view:'movies', model:[ filter:params.id, movies:movies ]

        } else {
            def movieTitleLetters = Movie.executeQuery('select distinct(substring(upper(m.title),1,1)) from Movie m').sort()
            renderFilters 'Titles', 'letter', movieTitleLetters.collect { letter->
                [ id:letter, label:letter, count:countMoviesStartingWith(letter) ]
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

    def actorLetter(){
        def selectedLetter = params.id
        if( selectedLetter ){
            def actors = Actor.findAll('from Actor as a where substring(upper(a.lastName),1,1)=? order by a.lastName asc', [selectedLetter.toUpperCase()])

            renderFilters "$selectedLetter Actors", 'actor', actors.collect { act->
                [ id:act.id, label:act.displayName, count:act.movies.size() ]
            }

        } else {
            def actorLetters = Actor.executeQuery('select distinct(substring(upper(a.lastName),1,1)) from Actor a').sort()

            renderFilters 'Actors', 'actorLetter', actorLetters.collect { letter->
                [ id:letter, label:letter, count:countActorsStartingWith(letter) ]
            }
        }
    }

    def actor(){
        def actor = Actor.get(params.id)
        def movies = (actor.movies as List).sort { it.title }
        render view:'movies', model:[ filter:actor.displayName, movies:movies ]
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
            def years = Movie.executeQuery('select distinct(m.releaseYear) from Movie m order by m.releaseYear asc').collect { y->
                [ id:y, label:y, count:Movie.findAllByReleaseYear(y).size() ]
            }

            renderFilters 'Release Years', 'year', years
        }
    }

    def movie(){
        def movieId = params.id

        [
            movie:Movie.get(movieId as Long)
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

    def search(){
        [:]
    }

    def searchResults(){
        def criteria = params.criteria

        def movies = Movie.search().list {
            if(criteria){
                should {
                    def query = criteria.toLowerCase() + '*'
                    wildcard 'title', query
                    wildcard 'description', query
                    wildcard 'releaseYear', query
//                    wildcard 'mpaaRating', query
//                    wildcard 'format', query
                    wildcard 'runtime', query
//                    wildcard 'broadcast', query
                }
            }

            sort 'title'
        }

        render view:'movies', model:[ filter:'Search Results', movies:movies ]
    }

    private int countActorsStartingWith( String letter ){
        Actor.findAll('from Actor as a where substring(upper(a.lastName),1,1)=? order by a.lastName asc', [letter.toUpperCase()]).size()
    }

    private int countMoviesStartingWith( String letter ){
        Movie.findAll('from Movie as m where substring(upper(m.title),1,1)=? order by m.title asc', [letter.toUpperCase()]).size()
    }

    private void renderFilters( String name, String action, List items ){
        render view:'filters', model:[ filter:name, filterAction:action, items:items ]
    }
}
