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
            <g:each in="${items}">
                <li><g:link action="${filterAction}" id="${it.id}" data-transition="slide">${it.label}</g:link><span class="ui-li-count">${it.count}</span></li>
            </g:each>
        </ul>
    </div>
</div>

</body>
</html>