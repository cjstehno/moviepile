package com.stehno.moviepile

import com.stehno.moviepile.domain.Movie
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Test

@TestFor(MovieService)
@Mock(Movie)
class MovieServiceTests {

    @Delegate private DomainHelper domainHelper = new DomainHelper()

    @Test
    void 'listValidReleaseYears with a duplicate'(){
        provideMovies( [
            [releaseYear: 1980],
            [releaseYear:2000],
            [releaseYear:1990],
            [releaseYear:1980]
        ])

        def years = service.listValidReleaseYears()

        assertEquals 3, years.size()
        assertEquals 1980, years[0]
        assertEquals 1990, years[1]
        assertEquals 2000, years[2]
    }

    @Test
    void 'listValidReleaseYears when there are no movies'(){
        assertEquals 0, service.listValidReleaseYears().size()
    }

    @Test
    void 'listValidTitleLetters with a duplicate and some meaningless words'(){
        provideMovies( [
            [title:'12 Angry Men'],
            [title:'An Interesting Affair'],
            [title:'A Night to Remember'],
            [title:'The Thing'],
            [title:'Something Wicked This Way Comes'],
            [title:'The 5 Tomorrows']
        ])

        def letters = service.listValidTitleLetters()

        assertEquals 5, letters.size()
        assertEquals 'A', letters[0]
        assertEquals 'I', letters[1]
        assertEquals 'N', letters[2]
        assertEquals 'S', letters[3]
        assertEquals 'T', letters[4]
    }

    @Test
    void 'listValidTitleLetters when there are no movies'(){
        assertEquals 0, service.listValidTitleLetters().size()
    }

    @Test
    void 'listMoviesWithTitleLetter with some data'(){
        provideMovies( [
            [title:'12 Angry Men'],
            [title:'An Interesting Affair'],
            [title:'A Night to Remember'],
            [title:'The Thing'],
            [title:'Something Wicked This Way Comes'],
            [title:'And 5 Tomorrows']
        ])

        def movies = service.listMoviesWithTitleLetter( 'T' )

        assertEquals 2, movies.size()
        assertEquals 'The Thing', movies[0].title
        assertEquals 'And 5 Tomorrows', movies[1].title
    }

    @Test
    void 'listMoviesWithTitleLetter with no movies'(){
        assertEquals 0, service.listMoviesWithTitleLetter( 'T' ).size()
    }

    @Test
    void 'countMoviesWithTitleLetter with some data'(){
        provideMovies( [
            [title:'12 Angry Men'],
            [title:'An Interesting Affair'],
            [title:'A Night to Remember'],
            [title:'The Thing'],
            [title:'Something Wicked This Way Comes'],
            [title:'And 5 Tomorrows']
        ])

        assertEquals 2, service.countMoviesWithTitleLetter( 'T' )
    }
}
