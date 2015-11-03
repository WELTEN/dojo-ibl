// Models
window.GameAccess = Backbone.Model.extend({
    initialize: function(){
        //console.log("Game Access initialized");
    }
});

window.Game = Backbone.Model.extend({
    idAttribute: 'gameId',
    initialize: function(a){
    },
    url: function(){
        return "/rest/myGames/gameId/"+this.id;
    },
    defaults:{
        "title":"",
        "description": "",
        "accessRight": 0
    }
});

window.RunAccess = Backbone.Model.extend({
    initialize: function(){
        console.log("Run Access initialized");
    },
    parse : function(response){
        return response.gamesAccess;
    }
});

window.Run = Backbone.Model.extend({
    global_identifier: 0,
    initialize: function(a){
        //console.log("Run initialize");
    }
});

window.User = Backbone.Model.extend({
    initialize: function(a){
        //console.log("User initialize");
    }
});

window.Activity = Backbone.Model.extend({
    initialize: function(a){
        //console.log("Activity/generalItem initialize");
    },
    defaults:{
        "id":null,
        "name":"",
        "sortKey":0,
        "description":"",
        "lastModificationDate":0,
        "gameId":0,
        "dependsOn":"",
        "roles":""
    }
});

window.MyGame = Backbone.Model.extend({
    initialize: function(a){
        this.on('all', function(e){
            //console.debug(this.get("responseValue") + " event: "+ e);
        })
    },
    urlRoot: '/rest/myGames',
    defaults:{
        "title": 0
    }
});

window.MyRun = Backbone.Model.extend({
    initialize: function(a){
        this.on('all', function(e){
            //console.debug(this.get("responseValue") + " event: "+ e);
        })
    },
    urlRoot: '/rest/myRuns',
    defaults:{
        "title": 0,
        "gameId": 0
    }
});

window.Response = Backbone.Model.extend({
    initialize: function(a){
        this.on('all', function(e){
            //console.debug(this.get("responseValue") + " event: "+ e);
        })
    },
    urlRoot: '/rest/response',
    defaults:{
        "generalItemId": 0,
        "responseValue": "",
        "runId": 0,
        "userEmail": "",
        "lastModificationDate": 0
    }
});

window.Message = Backbone.Model.extend({
    idAttribute: "messageId",
    initialize: function(a){
        //console.log("Message initialize");
    },
    urlRoot: '/rest/messages/message',
    defaults:{
        "id":null,
        "runId":"",
        "threadId":0,
        "subject":"",
        "body":0
    }
});

window.ChannelAPI = Backbone.Model.extend({
    initialize: function(a){
        //console.log("Channel API initialize");
    }
});

// Collections
window.GameAccessCollection = Backbone.Collection.extend({
    model: GameAccess,
    url: "/rest/myGames/gameAccess",
    parse: function(response){
        return response.gamesAccess;
    }
});

window.GameCollection = Backbone.Collection.extend({
    model: Game
});

window.GameParticipateCollection = Backbone.Collection.extend({
    model: Game,
    url: function(){
        return "/rest/myGames/participate";
    },
    parse: function(response){
        return response.games;
    }
});

window.RunAccessCollection = Backbone.Collection.extend({
    model: RunAccess,
    url: "/rest/myRuns/runAccess"
});

window.ActivitiesCollection = Backbone.Collection.extend({
    model: Activity,
    url: function(){
        return "/rest/generalItems/gameId/"+this.id;
    },
    comparator: function(item) {
        return item.get('sortKey');
    },
    parse: function(response){

        //var listSource = new Array();
        ////var self = this;
        //
        //_.each(response.generalItems, function(generalItem){
        //    listSource.push(new Activity( {
        //        "id": generalItem.id,
        //        "name": generalItem.name,
        //        "sortKey": generalItem.sortKey,
        //        "description": generalItem.description,
        //        "lastModificationDate":generalItem.lastModificationDate,
        //        "gameId": generalItem.gameId,
        //        "dependsOn": generalItem.dependsOn,
        //        "roles": generalItem.roles
        //    }));
        //});

        //return listSource;

    }
});

window.ActivityCollection = Backbone.Collection.extend({
    model: Activity,
    url: function(){
        return "/rest/generalItems/gameId/"+this.gameId+"/generalItem/"+this.id;
    }
});


window.ResponseCollection = Backbone.Collection.extend({
    model: Response,
    initialize: function(a){
        this.on('all', function(e){
            //console.debug("there was a change in collection", e);
        });

    },
    parse: function(response){
      return   response.responses;
    },
    url: function(){
        return "/rest/response/runId/"+this.id+"/itemId/"+this.itemId;
    },
    comparator: function(item) {
        return item.get('lastModificationDate');
    }
});

window.MessageCollection = Backbone.Collection.extend({
    model: Message,
    parse: function(response){
        return   response.messages;
    },
    url: function(){
        return "/rest/messages/runId/"+this.id+"/default";
    }
});

//Users
window.UserCollection = Backbone.Collection.extend({
    model: User,
    url: "/rest/account/accountDetails"
});

window.UserRunCollection = Backbone.Collection.extend({
    model: User,
    url: function(){
        return "/rest/users/runId/"+this.runId;
    },
    parse: function(response){
        return response.users;
    }
});

window.RunByGameCollection = Backbone.Collection.extend({
    model: Run,
    initialize: function(models) {
        this.id = models.id;
    },
    url: function() {
        return '/rest/myRuns/participate/gameId/' + this.id +'/';
    }
});

window.RunCollection = Backbone.Collection.extend({
    model: Game,
    url: function(){
        return "/rest/myRuns/runId/"+ this.id;
    }
});

window.ChannelAPICollection = Backbone.Collection.extend({
    model: ChannelAPI,
    url: function(){
        return "/rest/channelAPI/token";
    }
});

// Functions
var setHeader = function (xhr) {
    // Check cookie and read that value

    //console.log(location.href);

    //var s = document.cookie;

    var accessToken = "";

    // Problem:
    // 1) someone put an access token manually it will restore the current cookie
    // 2)

    if(location.href.indexOf("accessToken=") > -1){
        var aux = location.href.split("accessToken=")[1];
        accessToken = aux.split("&")[0];
        //console.log(location.href, aux, accessToken);

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie("arlearn.AccessToken", accessToken, {expires: date, path: "/"});
        $.cookie("arlearn.OauthType", 2, {expires: date, path: "/"});

        document.location = "/";


    }

    if($.cookie("arlearn.AccessToken")){
        accessToken = $.cookie("arlearn.AccessToken");
        //console.log(location.href, accessToken);
    }

    //// There is a cookie with accessToken
    //if(s.indexOf("accessToken=") > -1){
    //    accessToken = s.split("accessToken=")[1];
    //    console.log(location.href, accessToken);
    //    if(location.href.indexOf("accessToken=") > -1){
    //
    //        document.location = "/";
    //    }else{
    //
    //    }
    //}else{
    //    // No cookie but accessToken in the URL
    //    if(location.href.indexOf("accessToken=") > -1){
    //        var aux = location.href.split("accessToken=")[1];
    //        accessToken = aux.split("&")[0];
    //        console.log(location.href, aux, accessToken);
    //
    //        var date = new Date();
    //        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
    //        var expires = "; expires=" + date.toGMTString();
    //
    //        document.cookie = "accessToken=" + accessToken + expires + "; path=";
    //        document.location = "/";
    //    }else{
    //        // No cookies and no access token
    //        alert("session expired");
    //    }
    //}

    //32ea7e66b9e1904b2786f531e415dda

    xhr.setRequestHeader('Authorization', 'GoogleLogin auth='+accessToken);
    xhr.setRequestHeader('Accept', 'application/json');

    //console.log(accessToken,xhr);
}