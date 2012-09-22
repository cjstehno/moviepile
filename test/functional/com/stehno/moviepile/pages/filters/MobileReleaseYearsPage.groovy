package com.stehno.moviepile.pages.filters

class MobileReleaseYearsPage extends MobileFiltersPage {

    static at = {
        $("h1",0).text() == 'MoviePile: Release Years'
    }
}
