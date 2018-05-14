<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="Transempiric"/>
    <meta name="author" content="Transempiric"/>
    <title>Transempiric Webflux Template</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css"/>
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/test/ws">Transempiric Webflux Template</a>
            </div>
        </div>
    </nav>
    <div class="container wrapper">
        <h3>Secure Web-socket Example</h3>
        <p>Echo the input with a delay of 2 seconds</p>
        <p>
            Input<br />
            <textarea id="input" name="input" cols="40"></textarea>
        </p>

        <p>
            Output<br />
            <div id="output"></div>
        </p>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/js/common.js"></script>

    <script type="application/javascript">
        // Use "wss://" for HTTPS
        var socket = new WebSocket("ws://" + location.host + "/api/ws/echotest?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZGV2IiwiYXVkIjoidW5rbm93biIsImV4cCI6MjEzMTEyOTA2MCwiaWF0IjoxNTI2MzI5MDYwfQ.KM-cb8vcuO6QQCFpbcHrj96TJLbQjPc5_8SN88aM3xTE89e_9HXSZnSW7XexhwFAej7hbMj4t8q65uXV8Vi08g");
        addEvent("keypress", document.getElementById('input'), function(event) {
            socket.send(event.key);
        });
        var output = document.getElementById("output");
        socket.onmessage = function(e) {
            output.innerHTML = output.innerHTML + e.data;
        }
    </script>
</body>
</html>