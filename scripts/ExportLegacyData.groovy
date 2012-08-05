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

import groovy.sql.Sql
import groovy.xml.MarkupBuilder

includeTargets << grailsScript("_GrailsInit")

target(main: "Exports a legacy MyMdb database to an xml fileset.") {
    def outdir = 'c:/Users/cjstehno/Desktop/legacy-mymdb'
    new File(outdir).mkdirs()

    def db = Sql.newInstance( 'jdbc:h2:file:b:/Projects/moviepile/mymdb', 'sa', '', 'org.h2.Driver' )

    def actorLookup = []
    db.eachRow('select m.movie_id,a.first_name,a.middle_name,a.last_name from movie_actors m, actor a where a.id=m.actor_id'){ row->
        actorLookup << [
            movieId:  row.movie_id,
            firstName: row.first_name ?: '',
            middleName: row.middle_name ?: '',
            lastName: row.last_name ?: ''
        ]
    }
    println "Loaded ${actorLookup.size()} actors..."

    def genreLookup = []
    db.eachRow('select m.movie_id,g.name from movie_genres m, genre g where g.id=m.genre_id'){ row->
        genreLookup << [
            movieId: row.movie_id,
            name: row.name
        ]
    }
    println "Loaded ${genreLookup.size()} genres..."

    def websiteLookup = []
    db.eachRow('select m.movie_sites_id as movie_id, w.label, w.url from movie_web_site m, web_site w where m.WEB_SITE_ID = w.ID'){ row->
        websiteLookup << [
            movieId:row.movie_id,
            label:row.label,
            url:row.url
        ]
    }
    println "Loaded ${websiteLookup.size()} web sites..."

    def storageLookup = []
    db.eachRow('select sm.movie_id,su.name, s.index from storage_movie sm,storage_unit su,storage s where s.id = sm.storage_movies_id and su.id = s.unit_id'){ row->
        storageLookup << [
            movieId:  row.movie_id,
            name: row.name,
            index: row.index
        ]
    }
    println "Loaded ${storageLookup.size()} storage entries..."

    int count = 0
    db.eachRow('select id,poster_id,broadcast,date_created,description,format,last_update,mpaa_rating,runtime,title,release_year from movie'){ row->
        createFile( "$outdir/movie-${row.id}.xml" ).withWriter { writer->
            new MarkupBuilder(writer).movie( id:row.id, 'release-year':row.release_year, format:row.format, runtime:row.runtime, rating:row.mpaa_rating, broadcast:row.broadcast ){
                title( row.title )
                summary( row.description )
                dates( created:row.date_created, updated:row.last_update )
                websites {
                    websiteLookup.findAll { ws-> ws.movieId == row.id }?.each { ws->
                        website( ref:ws.url,  ws.label )
                    }
                }
                storage {
                    storageLookup.findAll { st-> st.movieId == row.id }?.each { entry->
                        slot( name:entry.name, index:entry.index )
                    }
                }
                genres {
                    genreLookup.findAll { g-> g.movieId == row.id }?.each { g->
                        genre( g.name )
                    }
                }
                actors {
                    actorLookup.findAll { a-> a.movieId == row.id }?.each { a->
                        actor {
                            firstName(a.firstName)
                            middleName(a.middleName)
                            lastName(a.lastName)
                        }
                    }
                }
                posters {
                    byte[] content = db.firstRow('select content from poster where id=?', [row.id])?.content
                    if( content ){
                        poster( content.encodeBase64(true) )
                    }
                }
            }

        }
        count++
    }
    println "Wrote $count movies."
}

private File createFile( path ){
    def file = new File( path )
    if( file.exists() ) file.delete()
    file.createNewFile()
    return file
}

setDefaultTarget(main)
