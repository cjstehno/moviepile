package com.stehno.moviepile

import com.stehno.moviepile.domain.Actor
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Test

@TestFor(ActorService)
@Mock(Actor)
class ActorServiceTests {

    @Delegate private DomainHelper domainHelper = new DomainHelper()

    @Test
    void 'listAndCountValidActorNameLetters with data'(){
        provideActors( [
            [ lastName:'Gibson' ],
            [ lastName:'Alda' ],
            [ lastName:'Gordon' ],
            [ lastName:'Baldwin' ]
        ] )

        def map = service.listAndCountValidActorNameLetters()

        assertEquals 3, map.size()

        assertEquals 1, map['A']
        assertEquals 1, map['B']
        assertEquals 2, map['G']
    }

    @Test
    void 'listAndCountValidActorNameLetters with no actors'(){
        assertEquals 0, service.listAndCountValidActorNameLetters().size()
    }

    @Test
    void 'listActorsWithNameLetter with data'(){
        provideActors( [
            [ lastName:'Gibson' ],
            [ lastName:'Alda' ],
            [ lastName:'Gordon' ],
            [ lastName:'Baldwin' ]
        ] )

        def actors = service.listActorsWithNameLetter('g')

        assertEquals 2, actors.size()
        assertEquals 'Gibson', actors[0].lastName
        assertEquals 'Gordon', actors[1].lastName
    }
}
