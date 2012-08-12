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

package controller

import groovy.io.FileType
import com.stehno.moviepile.domain.*

/**
 * FIXME: this is only for importing legacy data xml files in a specified directory into the database.
 * This is not production code at this time.
 */
class LegacyImportController {

    private static final File importDir = new File('c:/Users/cjstehno/Desktop/legacy-mymdb')

    def executeImport(){
        def xml = new XmlSlurper()

        importDir.eachFile( FileType.FILES ){ File file->
            def movieXml = xml.parse(file)

            def actors = []
            movieXml.actors.children().each { act->
                actors << ensureActor( act.firstName.text(), act.middleName.text(), act.lastName.text() )
            }

            def websites = []
            movieXml.websites.children().each { web->
                websites << new WebSite( label:web.text(), url:web.@ref.text() )
            }

            def posters = []
            movieXml.posters.children().each { poster->
                posters << new Poster( content:poster.text().decodeBase64()  )
            }

            def movie = new Movie(
                title: movieXml.title.text(),
                description: movieXml.summary.text(),
                releaseYear: movieXml.@'release-year'.text() as Integer,
                mpaaRating: MpaaRating.valueOf( MpaaRating, movieXml.@rating.text()),
                format: Format.valueOf( Format, movieXml.@format.text()),
                runtime: movieXml.@runtime.text() as Integer,
                broadcast: Broadcast.valueOf( Broadcast, movieXml.@broadcast.text()),
            )

            movieXml.genres.children().each { gen->
                movie.addToGenres ensureGenre( gen.text() )
            }

            actors.each { act->
                movie.addToActors( act )
            }

            websites.each { web->
                movie.addToSites( web )
            }

            if( posters ){
                posters.each { poster->
                    movie.addToPosters( poster )
                }
            }

            movieXml.storage.children().each { slot->
                movie.addToStorageSlots(new StorageSlot(
                    name:slot.@name.text(),
                    index:(slot.@index.text() as Integer)
                ))
            }

            movie.save( flush:true )
        }
    }

    private Actor ensureActor( String firstName, String middleName, String lastName ){
        def found

        def actors = Actor.findAllByLastName(lastName)
        if( actors ){
            final String targetFullName = fullName(firstName,middleName,lastName)
            found = actors.find { act-> act.fullName == targetFullName }
        }

        found ?: new Actor( firstName:firstName, middleName:middleName, lastName:lastName )
    }

    private String fullName( String firstName, String middleName, String lastName ){
        "${firstName ? firstName + ' ' : ''}${middleName ? middleName + ' ' : ''}$lastName" as String
    }

    private Genre ensureGenre( String name ){
        def found = Genre.findByName( name )
        found ?: new Genre( name:name )
    }
}
