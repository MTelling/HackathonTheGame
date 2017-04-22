var stompClient = null;



function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);


        stompClient.subscribe('/user/queue/login', function(message) {
            console.log(JSON.parse(message.body).message);
            loginSucces();
        });

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function loginSucces() {

    stompClient.unsubscribe('/user/queue/login');

    stompClient.subscribe('/topic/public-chat', function (greeting) {
        showNewMessage(JSON.parse(greeting.body).content);
    });

    stompClient.subscribe('/user/queue/status', function (status) {
        showStatus(JSON.parse(status.body).message);
    });

    $("#login-form").hide();
    $("#main-content").show();
}

function login() {
    stompClient.send("/app/login", {}, JSON.stringify({'content': $("#name").val()}));
}


function sendMessage() {
    stompClient.send("/app/public-chat", {}, JSON.stringify({'content': $("#chat-message").val()}));
}

function askForStatus() {
    stompClient.send('/app/private', {}, JSON.stringify({'content': "request"}));
}

function showStatus(message) {
    $("#status-messages").append("<tr><td>" + message + "</td></tr>");
}


function showNewMessage(message) {
    $("#public-messages").append("<tr><td>" + message + "</td></tr>");

}

$(document).ready(function () {

    console.log("Dom ready - connecting!");
    connect();

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#connect" ).click(function() { login(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send-message" ).click(function() { sendMessage(); });
    $( "#send-status" ).click(function() { askForStatus(); });
});

