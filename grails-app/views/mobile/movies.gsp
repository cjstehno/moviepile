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
                <h1>MoviePile: ${filter}</h1>
            </div>
            <div data-role="content">
                <ul data-role="listview" data-filter="true">
                    <g:each in="${movies}" var="movie">
                        <li><g:link action="movie" id="${movie.id}" data-transition="slide"><h2>${movie.title}</h2>
                            <span class="ui-li-aside">${movie.storageSlots.collect { "${it.name}-${it.index}" }.join(', ') }</span>
                            <p>${movie.description}</p></g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>

    </body>
</html>