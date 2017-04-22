var stompClient = null;
var currentChallengeDescription = {
  name: "",
  description: "",
  initialCode: "",
};
var user = {
  username: ""
};

module.exports = function () {
  return {
    user: user,
    challenge: currentChallengeDescription,
    stompClient: stompClient,
    connect: function (router) {
        var socket = new SockJS('/htg');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/queue/login', function (status) {
              var response = JSON.parse(status.body)
              var status = response.status;
                if (status === "success") {
                   // GUI
                   user.username = response.username;
                   router.push('/game');
                } else if (status === "exists") {
                    // GUI
                    console.log("EXIST!!");
                }
            });
        });
    },
    login: function(username) {
       console.log("Trying to login as " + username);
       stompClient.send("/app/login", {}, JSON.stringify({"username": username}));
     }
  };
};
//
// $(document).ready(function () {
//
//     connect();
    //
    // $("form").on('submit', function (e) {
    //     e.preventDefault();
    // });
    //
    // $( "#loginbtn" ).click(function() {
    //     login($( "#username" ).val());
    // });
    //
    // $( "#winbtn" ).click(function() {
    //     stompClient.send("/app/pm", {}, JSON.stringify({"code": "package Testing;\n\npublic class Program {\n\tpublic Object[] run(String[] args) {\n\t\treturn new Object[] {Integer.parseInt(args[0]) + Integer.parseInt(args[1])};\n\t}\n}"}));
    // });
//
//
// });
