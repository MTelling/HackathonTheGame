var stompClient = null;

function connect() {
    var socket = new SockJS('/htg');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);


        stompClient.subscribe('/topic/game', function (gameResponse) {
            console.log("Got response!");
            console.log(JSON.parse(gameResponse.body).status);
        });

        stompClient.subscribe('/user/queue/login', function (status) {
            console.log("Got private response!");
            console.log(JSON.parse(status.body).status);
        });

        stompClient.subscribe('/user/queue/pm', function (pmResponse) {
            console.log("Got pm response!");
            console.log(JSON.parse(pmResponse.body).status);
        });


        stompClient.send("/app/game", {}, JSON.stringify({}));
        stompClient.send("/app/login", {}, JSON.stringify({"username": "morten"}));
        stompClient.send("/app/pm", {}, JSON.stringify({"code": "somuchcode"}));


    });
}


connect();

