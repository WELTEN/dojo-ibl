////////
// Games
////////
window.GameAccess = Backbone.Model.extend({
    initialize: function(){
        //console.log("Game Access initialized");
    }
});

window.Game = Backbone.Model.extend({
    methodToURL: {
        'read': '/rest/myGames/gameId/'+this.gameId,
        'create': '/rest/myGames',
        'update': '/user/update',
        'delete': '/user/remove'
    },
    sync: function(method, model, options) {
        options = options || {};
        options.url = model.methodToURL[method.toLowerCase()];

        return Backbone.sync.apply(this, arguments);
    }
});

window.GameDelete = Backbone.Model.extend({
    url: function(){
        return "/rest/myGames/gameId/"+this.gameId;
    }
});


window.GiveAccessToGame = Backbone.Model.extend({
    initialize: function(){
        console.log("Give access to a Run");
    },
    url: function(){
        return "/rest/myGames/access/gameId/"+this.gameId+"/account/"+this.accoundId+"/accessRight/"+this.accessRight;
    }
});


window.PictureUrlGame = Backbone.Model.extend({
    initialize: function(){
        console.log("Give me upload url for avatar inquiry");
    },
    url: function(){
        console.log(this.gameId);
        return "/rest/myGames/pictureUrl/gameId/"+this.gameId;
    }
});



//window.Game = Backbone.Model.extend({
//    methodToURL: {
//        'read': '/rest/myGames/gameId/'+this.gameId,
//        'create': '/rest/myGames'
//    },
//    sync: function(method, model, options) {
//        options = options || {};
//        options.url = model.methodToURL[method.toLowerCase()];
//
//        return Backbone.sync.apply(this, arguments);
//    }
//});

//////
// Run
//////
window.RunAccess = Backbone.Model.extend({
    initialize: function(){
        console.log("Run Access initialized");
    },
    parse : function(response){
        return response.gamesAccess;
    }
});

window.GiveAccessToRun = Backbone.Model.extend({
    initialize: function(){
        console.log("Give access to a Run");
    },
    url: function(){
        return "/rest/myRuns/access/runId/"+this.runId+"/account/"+this.accoundId+"/accessRight/"+this.accessRight;
    }
});

window.Run = Backbone.Model.extend({
    initialize: function(a){
        //console.log("Run initialize");
    },
    methodToURL: {
        'read': "/rest/myRuns/runId/"+this.id,
        'create': '/rest/myRuns'
    },
    sync: function(method, model, options) {
        options = options || {};
        options.url = model.methodToURL[method.toLowerCase()];

        return Backbone.sync.apply(this, arguments);
    }
});

window.RunByCode = Backbone.Model.extend({
    defaults: {
        "code": ""
    },
    url: function(){
        return "/rest/myRuns/code/"+this.code;
    }
});

window.AddUserToRun = Backbone.Model.extend({
    initialize: function(){
        console.log("Add user to a Run");
    },
    url: function(){
        return "/rest/users";
    }
});


window.User = Backbone.Model.extend({
    initialize: function(a){
        //console.log("User initialize");
    }
});

window.ActivityEdit = Backbone.Model.extend({
    defaults:{
        "gameId": 0
    },
    url: function(){
        return "/rest/generalItems/gameId/"+this.gameId;
    }
});

window.Activity = Backbone.Model.extend({
    methodToURL: {
        'read': "/rest/generalItems/gameId/"+this.gameId+"/generalItem/"+this.id+"/section/"+this.section,
        'create': '/rest/generalItems',
        'update': "/rest/generalItems/gameId/"+this.gameId+"/generalItem/"+this.id
    },
    sync: function(method, model, options) {
        options = options || {};
        options.url = model.methodToURL[method.toLowerCase()];

        return Backbone.sync.apply(this, arguments);
    }
});

window.ActivityUpdate = Backbone.Model.extend({
    url: function(){
        return "/rest/generalItems/gameId/"+this.gameId+"/generalItem/"+this.id;
    }
});

window.ActivityDelete = Backbone.Model.extend({
    url: function(){
        return "/rest/generalItems/gameId/"+this.gameId+"/generalItem/"+this.id;
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
        "lastModificationDate": 0,
        "parentId":0
    }
});

window.ResponseDelete = Backbone.Model.extend({
    initialize: function(a){ },
    url: function(){
        return '/rest/response/responseId/'+this.id
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
    intialize: function(models, options){
        options || (options = {});
        if (options.from) {
            this.from = options.from;
        };
    },
    model: GameAccess,
    url: function(){
        //if(this.from){
        //    return "/rest/myGames/gameAccess?from="+this.from;
        //
        //}else{
            return "/rest/myGames/gameAccess";
        //}
    },
    parse: function(response){
        this.from = response.serverTime;
        return response.gamesAccess;
    }
});

//window.GameCollection = Backbone.Collection.extend({
//    model: Game
//});

window.GameCollection = Backbone.Collection.extend({
    model: Game,
    url: function(){
        return '/rest/myGames/gameId/'+this.gameId;
    }
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
        return "/rest/generalItems/gameId/"+this.id+"/section/"+this.section;
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

window.TimelineCollection = Backbone.Collection.extend({
    model: Response,
    initialize: function(options){
        this.on('all', function(e){
            //console.debug("there was a change in collection", e);
        });
    },
    parse: function(response){
        this.resumptionToken = response.resumptionToken;
        return   response.responses;
    },
    url: function(){
        if(this.resumptionToken){
            console.log("segunda y siguientes con token");
            return "/rest/response/runId/"+this.id+"?resumptionToken="+this.resumptionToken;
        }else{
            console.log("la primera", this.id, Math.floor((Date.now() /1000) - 3600));
            this.from = Math.floor((Date.now() /1000) - 3600);
            return "/rest/response/runId/"+this.id+"?from=0";
        }

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
    byRun: function (runId) {
        var filtered = this.filter(function (message) {
            return message.get("runId") === runId;
        });
        return new window.MessageCollection(filtered);
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
    },
    parse: function(response){
        return response.runs;
    }
});

window.RunCollection = Backbone.Collection.extend({
    model: Run,
    url: function(){
        return "/rest/myRuns/runId/"+ this.id;
    },
    parse: function(response){
        return response.runs;
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

        document.location = "/main.html";
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