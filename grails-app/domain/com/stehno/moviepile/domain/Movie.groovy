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

package com.stehno.moviepile.domain

/**
 * Domain object representation of a movie.
 */
class Movie {

    String title
    String description
    Integer releaseYear
    MpaaRating mpaaRating
    Format format
    Integer runtime
    Broadcast broadcast

    static hasMany = [
        genres:Genre,
        actors:Actor,
        sites:WebSite,
        storageSlots:StorageSlot,
        posters:Poster
    ]

    Date dateCreated
    Date lastUpdate

    static constraints = {
        title blank:false, size:1..100
        description nullable:true, blank:true, maxSize:2000
        releaseYear nullable:true, range:1900..2100
        runtime nullable:true, min:0

        mpaaRating inList:MpaaRating.values() as List
        format inList:Format.values() as List
        broadcast inList:Broadcast.values() as List

        lastUpdate nullable:true
        dateCreated nullable:true
    }

    static search = {
        title index:'tokenized'
        description index:'tokenized'
        releaseYear index:'un_tokenized'
        mpaaRating index:'un_tokenized'
        format index:'un_tokenized'
        runtime index:'un_tokenized'
        broadcast index:'un_tokenized'

        genres indexEmbedded:[ depth:2 ]
        actors indexEmbedded:[ depth:2 ]
        sites indexEmbedded:[ depth:2 ]
    }
}

/**
 * Simple enumeration of available MPAA Ratings.
 */
enum MpaaRating {
    UNKNOWN('Unknown'),
    G('G'),
    PG('PG'),
    PG_13('PG-13'),
    R('R'),
    NC_17('NC-17'),
    UNRATED('Unrated')

    final String label

    private MpaaRating( String label ){
        this.label = label
    }

    static MpaaRating fromLabel( String label ){
        (MpaaRating.values().find { it.label == label }) ?: MpaaRating.UNKNOWN
    }
}

/**
 * Simple enumeration of movie formats.
 */
enum Format {
    UNKNOWN('Unknown'),
    VCD('VCD'),
    DVD('DVD'),
    DVD_R('DVD-R'),
    BLURAY('BluRay')

    final String label

    private Format( String label ){
        this.label = label
    }

    static Format fromLabel( String label ){
        (Format.values().find { it.label == label }) ?: Format.UNKNOWN
    }
}

/**
 * Simple enumeration of boradcast types.
 */
enum Broadcast {
    UNKNOWN('Unknown'),
    MOVIE('Movie'),
    TV_MOVIE('TV Movie'),
    TV_SPECIAL('TV Special'),
    TV_SERIES('TV Series'),
    OTHER('Other')

    final String label

    private Broadcast( String label ){
        this.label = label
    }

    static Broadcast fromLabel( String label ){
        (Broadcast.values().find { it.label == label }) ?: Broadcast.UNKNOWN
    }
}

