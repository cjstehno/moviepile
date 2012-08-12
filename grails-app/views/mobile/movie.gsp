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
                <g:link action="index" data-icon="home" data-transition="slide" data-direction="reverse">Home</g:link>
                <h1>MoviePile</h1>
            </div>
            <div data-role="content">
                <g:each in="${movie.posters}" var="poster">
                    <img src="<g:createLink action="poster" id="${poster.id}" />" />
                </g:each>

                <h2>${movie.title}</h2>
                <p>${movie.description}</p>

                <section class="ui-grid-a">
                    <div class="ui-block-a"><strong>Storage:</strong></div>
                    <div class="ui-block-b">
                        <g:each in="${movie.storageSlots}" var="slot">
                            <g:link action="unit" id="${slot.name}">${slot.name}-${slot.index}</g:link>,
                        </g:each>
                    </div>

                    <div class="ui-block-a"><strong>Released:</strong></div>
                    <div class="ui-block-b"><g:link action="year" id="${movie.releaseYear}">${movie.releaseYear}</g:link></div>

                    <div class="ui-block-a"><strong>Runtime:</strong></div>
                    <div class="ui-block-b">${movie.runtime} min</div>

                    <div class="ui-block-a"><strong>Rating:</strong></div>
                    <div class="ui-block-b">${movie.mpaaRating?.label}</div>

                    <div class="ui-block-a"><strong>Format:</strong></div>
                    <div class="ui-block-b">${movie.format?.label}</div>

                    <div class="ui-block-a"><strong>Broadcast:</strong></div>
                    <div class="ui-block-b">${movie.broadcast?.label}</div>
                </section>

                <h3>Genres</h3>
                <ul data-role="listview" data-inset="true">
                    <g:each in="${movie.genres}" var="genre">
                        <li><g:link action="genre" id="${genre.id}" data-transition="slide">${genre.name}</g:link></li>
                    </g:each>
                </ul>

                <h3>Actors</h3>
                <ul data-role="listview" data-inset="true">
                    <g:each in="${movie.actors}" var="actor">
                        <li><g:link action="actor" id="${actor.id}" data-transition="slide">${actor.displayName}</g:link></li>
                    </g:each>
                </ul>

                <h3>External Links</h3>
                <ul data-role="listview" data-inset="true">
                    <g:each in="${movie.sites}" var="site">
                        <li><a href="${site.url}" target="_blank">${site.label}</a></li>
                    </g:each>
                </ul>
            </div>
        </div>

    </body>
</html>