var stompClient = null;

function subscribeToPM() {
    stompClient.subscribe('/user/queue/pm', function (pmResponse) {
        console.log("Got pm response!");
        console.log(JSON.parse(pmResponse.body).status);
    });
}

function subscribeToCheckLogin() {
    stompClient.subscribe('/user/queue/checkLogin', function (status) {
        var status = JSON.parse(status.body).status;
        handlePageRefresh(status);
    });
}

function subscribeToLogin() {
    stompClient.subscribe('/user/queue/login', function (status) {
        var status = JSON.parse(status.body).status;
        handleLogin(status);
    });
}

function subscribeToNews() {
    stompClient.subscribe('/topic/news', function (news) {
        console.log("Got news!");
        console.log(JSON.parse(news.body).news);
    });
}

function subscribeToGame() {
    stompClient.subscribe('/topic/game', function (gameResponse) {
        console.log("Got response!");
        console.log(JSON.parse(gameResponse.body).status);
    });
}

function unsubscribeCheckLogin() {
    stompClient.unsubscribe('/user/queue/checkLogin');
}

function unsubscribeLogin() {
    stompClient.unsubscribe('/user/queue/login');
}

function unsubscribeGame() {
    stompClient.unsubscribe('/topic/game');
}

function handlePageRefresh(status) {
    if (status === "alreadyIn") {
        showGame();
        beginGame();
    } else {
        showLogin();
        subscribeToLogin();
    }

    unsubscribeCheckLogin();
}

function unsubscribePM() {
    stompClient.unsubscribe('/user/queue/pm');
}

function connect() {
    var socket = new SockJS('/htg');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);


        subscribeToCheckLogin();
        stompClient.send("/app/checkLogin", {}, JSON.stringify({"username":""}));



        //stompClient.send("/app/game", {}, JSON.stringify({}));
        //stompClient.send("/app/pm", {}, JSON.stringify({"code": "somuchcode"}));


    });
}

function login(username) {
    console.log("Trying to login as " + username);
    stompClient.send("/app/login", {}, JSON.stringify({"username": username}));

}

function handleLogin(status) {
    console.log("Got login status " + status);
    if (status === "success") {

        // GUI
        showGame();


        // This is socket stuff
        beginGame();


    } else if (status === "exists") {
        // GUI
        showLoginError();
    }
}

function hideLogin() {
    $("#loginContainer").hide();
}

function showLogin() {
    $("#loginContainer").show();
}

function showGame() {
    hideLogin();
    hideLoginError();
    $("#gameContainer").show();
}

function beginGame() {
    unsubscribeLogin();
    subscribeToPM();
    subscribeToGame();
    subscribeToNews();
}

function showLoginError() {
    $("#loginErrorContainer").show();
}

function hideLoginError() {
    $("#loginErrorContainer").hide();
}




$(document).ready(function () {

    connect();

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#loginbtn" ).click(function() {
        login($( "#username" ).val());
    });

    $( "#winbtn" ).click(function() {
        stompClient.send("/app/pm", {}, JSON.stringify({"code": "somuchcode"}));
    });


});
