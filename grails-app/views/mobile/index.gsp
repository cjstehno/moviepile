<!DOCTYPE html>
<html>
    <head>
        <title>MoviePile</title>
        <meta name="layout" content="mobile"/>
        <g:javascript src="application.js" />
    </head>
    <body>

        <div data-role="page">
            <div data-role="header">
                <h1>MoviePile</h1>
            </div>
            <div data-role="content">
                <ul data-role="listview" data-filter="true">
                    <li><g:link action="letter" data-transition="slide">Titles</g:link><span class="ui-li-count">${counts.titles}</span></li>
                    <li><g:link action="genre" data-transition="slide">Genres</g:link><span class="ui-li-count">${counts.genres}</span></li>
                    <li><g:link action="actor" data-transition="slide">Actors</g:link><span class="ui-li-count">${counts.actors}</span></li>
                    <li><g:link action="unit" data-transition="slide">Storage</g:link><span class="ui-li-count">${counts.units}</span></li>
                    <li><g:link action="year" data-transition="slide">Release Years</g:link><span class="ui-li-count">${counts.years}</span></li>
                </ul>
            </div>
        </div>

    </body>
</html>