var stompClient = null;
var currentChallengeDescription = null;

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
    stompClient.subscribe('/topic/news', function (data) {
        console.log("Got news!");
        var news = JSON.parse(data.body);
        var type = news.type;

        if (type === "win") {
            handleWin(news.message);
        }
    });
}

function subscribeToGame() {
    stompClient.subscribe('/user/queue/game', function (gameResponse) {
        console.log("Got game description!");
        var challengeDescription = JSON.parse(gameResponse.body).challengeDescription;

        if (currentChallengeDescription === null || challengeDescription.name !== currentChallengeDescription.name) {
            currentChallengeDescription = challengeDescription;
            handleNewChallenge()
        }
    });
}

function handleNewChallenge() {
    console.log("getting new challenge");
    console.log("Name is " + currentChallengeDescription.name);
    console.log("Description is " + currentChallengeDescription.description);
    $("#challengeName").text(currentChallengeDescription.name);
    $("#challengeDescription").text(currentChallengeDescription.description);
    $("#challengeInitialCode").text(currentChallengeDescription.initialCode);
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

function requestChallengeDescription() {
    stompClient.send("/app/game", {}, JSON.stringify({}));
}

function handleWin(username) {
    alert(username + " won!");

    requestChallengeDescription();
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

    requestChallengeDescription();
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
        stompClient.send("/app/pm", {}, JSON.stringify({"code": "package Testing;\n\npublic class Program {\n\tpublic Object[] run(String[] args) {\n\t\treturn new Object[] {Integer.parseInt(args[0]) + Integer.parseInt(args[1])};\n\t}\n}"}));
    });


});


