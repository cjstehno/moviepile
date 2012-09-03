package com.stehno.moviepile.controller

import com.stehno.moviepile.DomainHelper
import com.stehno.moviepile.domain.Actor
import com.stehno.moviepile.domain.Genre
import com.stehno.moviepile.domain.Movie
import com.stehno.moviepile.domain.StorageSlot
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Before
import org.junit.Test
import com.stehno.moviepile.MovieService
import com.stehno.moviepile.ActorService

@TestFor(MobileController)
@Mock([ Movie, Actor, Genre, StorageSlot ])
class MobileControllerTests {

    @Delegate private DomainHelper domainHelper = new DomainHelper()

    @Before
    void before(){
        controller.movieService = new MovieService()
        controller.actorService = new ActorService()
    }

    // TODO: I wonder if mockito would help or hinder here

    @Test
    void 'index with no data'(){

        def model = controller.index()

        assertEquals 0, model.counts.titles
        assertEquals 0, model.counts.genres
        assertEquals 0, model.counts.actors
        assertEquals 0, model.counts.years
        assertEquals 0, model.counts.units
    }
}
