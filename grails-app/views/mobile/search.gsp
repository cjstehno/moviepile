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
                <h1>MoviePile: Search</h1>
            </div>
            <div data-role="content">
                <g:form action="search" method="POST" data-transition="slide">
                    <div data-role="fieldcontainer">
                        <label for="criteria">Search:</label>
                        <g:textField name="criteria" placeholder="Search for..." />
                    </div>
                    <button>Search</button>
                </g:form>
            </div>
        </div>

    </body>
</html>