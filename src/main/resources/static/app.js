var stompClient = null;

function connect() {
    var socket = new SockJS('/htg');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);


        stompClient.subscribe('/user/queue/login', function(message) {
            console.log(JSON.parse(message.body).status);
        });

        stompClient.send("/app/login", {}, JSON.stringify({'username': "test"}));


    });
}


connect();

