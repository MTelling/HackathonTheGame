var stompClient = null;

function subscribeToPM() {
    stompClient.subscribe('/user/queue/pm', function (pmResponse) {
        console.log("Got pm response!");
        console.log(JSON.parse(pmResponse.body).status);
    });
}

function subscribeToLogin() {
    stompClient.subscribe('/user/queue/login', function (status) {
        var status = JSON.parse(status.body).status;
    });
}

function subscribeToGame() {
    stompClient.subscribe('/topic/game', function (gameResponse) {
        console.log("Got response!");
        console.log(JSON.parse(gameResponse.body).status);
    });
}

function connect() {
    var socket = new SockJS('/htg');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);








        //stompClient.send("/app/game", {}, JSON.stringify({}));
        //stompClient.send("/app/pm", {}, JSON.stringify({"code": "somuchcode"}));


    });
}

function login(username) {
    stompClient.send("/app/login", {}, JSON.stringify({"username": username}));

}

function handleLogin(status) {
    console.log("Got login status " + status);
    if (status === "success") {

    } else if (status === "exists") {

    }
}




(document).ready(function () {

    connect();

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#loginbtn" ).click(function() {
        login($( "#username" ).val());
    });

});


