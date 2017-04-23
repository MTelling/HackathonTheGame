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
    setUser: function(_user){
      user = Object.assign({}, user, _user);
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
