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

