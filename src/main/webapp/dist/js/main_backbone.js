Backbone.View.prototype.close = function () {
    console.log('Closing view ' + this);
    if (this.beforeClose) {
        this.beforeClose();
    }
    this.remove();
    this.unbind();
};

var AppRouter = Backbone.Router.extend({

    initialize: function() {
    },
    routes: {
        ""			: "main",
        "inquiry/:id"	: "showInquiry",
        "inquiry/:id/phase/:phase" : "showPhase",
        "inquiry/:id/phase/:phase/activity/:activity" : "showActivity"
    },
    main: function() {
        this.common();
        this.initialGame();
        //this.initialRun();
        this.initializeChannelAPI();
    },
    initialGame: function(callback) {

        $('#inquiries').html( new MainView({ }).render().el );

        this.GameList = new GameCollection();

        this.GameAccessList = new GameAccessCollection();
        this.GameAccessList.fetch({
            beforeSend: setHeader,
            success: successGameHandler
        });

        this.GameParticipateList = new GameParticipateCollection();
        this.GameParticipateList.fetch({
            beforeSend: setHeader,
            success: successGameParticipateHandler
        });


        console.log("GameAccessList",app.GameAccessList);
        console.log("GameParticipateList", app.GameParticipateList);
        console.log("GameList", app.GameList);
    },
    initialRun: function(callback) {
        if (this.RunList) {
            if (callback)
                callback();
        } else {
            this.RunAccessList = new RunAccessCollection();
            this.RunAccessList.fetch({
                beforeSend: setHeader,
                success: function(response, xhr) {

                    _.each(xhr.runAccess, function(e){
                        this.RunList = new RunCollection({ });
                        this.RunList.id = e.runId;

                        this.RunList.fetch({
                            beforeSend: setHeader,
                            success: function (response, results) {

                                $('#inquiries > div > div.box-body').append( new RunListView({ model: results }).render().el );
                                if (callback)
                                    callback();
                            }
                        });
                    });
                }
            });
        }
    },
    showInquiry:function (id) {

        window.Run.global_identifier = id;

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie("dojoibl.run", id, {expires: date, path: "/"});

        this.common();
        this.loadInquiryUsers(id);
        this.initializeChannelAPI();
        this.loadChat(id);

        $('#inquiries').html(new InquiryStructureView({ }).render().el);
        //$('aside#summary').hide();

        /////////////////////////////////////////////////////////////////////////////
        // Hide scroll bar while hovering the chat to make the chat experience easier
        // Avoid undesirable scrolling movements
        /////////////////////////////////////////////////////////////////////////////
        //var b = $('html');
        //$('.direct-chat').hover(function() {
        //    var s = b.scrollTop();
        //    b.css('overflow', 'hidden');
        //    b.scrollTop(s);
        //}, function() {
        //    var s = b.scrollTop();
        //    b.css('overflow', 'auto');
        //    b.scrollTop(s);
        //});

        $('input#add-new-message').keypress(function (e) {
            var key = e.which;
            if(key == 13){
                var newMessage = new Message({ runId: id, threadId: 0, subject: "", body: $('input#add-new-message').val() });

                newMessage.save({}, {
                    beforeSend:setHeader
                });
            }
        });

        $('button#send-message').click(function(){

            var newMessage = new Message({ runId: id, threadId: 0, subject: "", body: $('input#add-new-message').val() });

            newMessage.save({}, {
                beforeSend:setHeader
            });

        });



        this.RunList = new RunCollection({ });
        this.RunList.id = id;

        this.RunList.fetch({
            beforeSend: setHeader,
            success: function (response, results) {

                this.inquiryView = new InquiryView({ model: results });

                $('aside#summary').html(this.inquiryView.render().el);
                $("#inquiry").hide();
                //$("#inquiry-explanation").children().hide();
                //$( "#inquiry-explanation .general").show();
                //$("#circlemenu .question, #circlemenu .method, #circlemenu .collection, #circlemenu .analysis, #circlemenu .interpretation, #circlemenu .communication").hover(
                //    function(e) {
                //        $("#inquiry-explanation .general").hide();
                //        $("#inquiry-explanation ."+$(e.toElement).attr('class')).show();
                //    }, function() {
                //        $( "#inquiry-explanation").children().hide();
                //        $( "#inquiry-explanation .general").show();
                //    }
                //);
            }
        });
    },
    showPhase: function(id, phase){
        options = { to: { width: 200, height: 60 } };

        var _self = this;
        var _id = id;

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
        $.cookie("dojoibl.game", id, {expires: date, path: "/"});

        if ( $("#inquiry").length == 0 || $("aside#summary").length == 0 ){
            $('#inquiries').html(new InquiryStructureView({ }).render().el).hide().fadeIn();
            if($.cookie("dojoibl.run")){

                ////////////////////////////////////////////////////////////
                // If we refresh the wepage we need to load everything again
                ////////////////////////////////////////////////////////////
                $('#inquiries').html(new InquiryStructureView({ }).render().el);

                this.RunList = new RunCollection({ });
                this.RunList.id = $.cookie("dojoibl.run");

                this.RunList.fetch({
                    beforeSend: setHeader,
                    success: function (response, results) {

                        this.inquiryView = new InquiryView({ model: results });

                        $('aside#summary').html(this.inquiryView.render().el);
                        $("#inquiry").hide();

                        //$("#inquiry-explanation").children().hide();
                        //$( "#inquiry-explanation .general").show();
                        //$("#circlemenu .question, #circlemenu .method, #circlemenu .collection, #circlemenu .analysis, #circlemenu .interpretation, #circlemenu .communication").hover(
                        //    function(e) {
                        //        $("#inquiry-explanation .general").hide();
                        //        $("#inquiry-explanation ."+$(e.toElement).attr('class')).show();
                        //    }, function() {
                        //        $( "#inquiry-explanation").children().hide();
                        //        $( "#inquiry-explanation .general").show();
                        //    }
                        //);

                        _self.loadPhaseActivities(_id);
                        _self.common();
                        _self.loadChat($.cookie("dojoibl.run"));
                        _self.loadInquiryUsers($.cookie("dojoibl.run"));
                    }
                });
            }
        }

        this.loadPhaseActivities(id);

    },
    showActivity: function(id, phase, activity){

        this.loadInquiryUsers(window.Run.global_identifier);


        if ($(".phase-master").length == 0){
            this.showPhase(id, phase);
            console.log("show phase", $(".phase-master"));

        }

        $(".phase-master").animate({
            'marginLeft' : '-30%',
            'width': '26%',
            'top': '264px'
        }, 800, function() {

            $(this).find(".box-tools.pull-right").hide();

            $(".phase-detail").show();

        });

        this.Activity = new ActivityCollection({ });
        this.Activity.id = activity;
        this.Activity.gameId = id;
        this.Activity.fetch({
            beforeSend: setHeader,
            success: successActivityHandler
        });

        if($.cookie("dojoibl.run")){
            window.Run.global_identifier = $.cookie("dojoibl.run");
        }

        console.log("InquiryUsers ->", app.InquiryUsers);

        this.Responses = new window.ResponseCollection(), new window.ResponseListView({ collection: this.Responses, users: app.InquiryUsers });

        this.Responses.id = window.Run.global_identifier;
        this.Responses.itemId = activity;
        this.Responses.fetch({
            beforeSend: setHeader
        });

        //this.Responses = new ResponseCollection({ });
        //this.Responses.id = window.Run.global_identifier;
        //this.Responses.itemId = activity;
        //this.Responses.fetch({
        //    beforeSend: setHeader,
        //    success: successResponsesHandler
        //});
    },
    initializeChannelAPI: function(){
        console.debug("[initializeChannelAPI]", "Initializing the chat");
        //this.loadChat();

        this.ChannelAPI = new ChannelAPICollection();
        this.ChannelAPI.fetch({
            beforeSend: setHeader,
            success: function(response, xhr){

                channel = new goog.appengine.Channel(xhr.token);
                socket = channel.open();

                socket.onopen = function() {
                    ///////////////////////////////////////////////////////////////////////////////////////
                    // TODO Block the chat until is connected
                    ///////////////////////////////////////////////////////////////////////////////////////
                    $('#messages').append('<p>Connected!</p>');
                };

                socket.onmessage = function(message) {

                    var a = $.parseJSON(message.data);

                    if(a.type == "org.celstec.arlearn2.beans.notification.MessageNotification") {

                        //console.debug(a);

                        if ($('.direct-chat-messages').length){
                            console.log(a);
                            var aux = a.messageId;

                            //if(app.MessagesList.get(aux) != "undefined"){
                                //console.debug(app.MessagesList.get(aux).toJSON());

                                //this.MessageReceived = new MessageCollection({ id: a.messageId });
                                //this.MessageReceived.fetch({
                                //    beforeSend: setHeader,
                                //    success: function (response, xhr) {
                                //        console.log(response, xhr);
                                //    }
                                //});

                                console.log(app.MessagesList.where({'messageId': 6438740092256256}));

                                console.log(a, app.UserList.at(0).toJSON());


                                $('.direct-chat-messages').append(new MessageOwnView({ model: a }).render().el);
                                $('.direct-chat-messages').animate({
                                    scrollTop: $('.direct-chat-messages')[0].scrollHeight
                                }, 200);
                            //}

                            $('input#add-new-message').val('');
                        }else{
                            ///////////////////////////////////////////////////////////////////////////////////////
                            // TODO We would need to check here if I have other windows open not nofifying myself again.
                            // TODO Changes in one collection modifies a view: http://stackoverflow.com/questions/10341141/backbone-multiple-views-subscribing-to-a-single-collections-event-is-this-a-b
                            ///////////////////////////////////////////////////////////////////////////////////////
                            $('.messages-menu > ul').append(new MessageNotificationView({ model: a }).render().el);
                            console.log("Message notifications:", $('.messages-menu span.label-success').html());

                            var message_notifications = $('.messages-menu span.label-success').html();

                            if(message_notifications != "" && message_notifications > 0){
                                message_notifications = parseInt(message_notifications) + 1;
                                $('.messages-menu span.label-success').html(message_notifications);
                            }else{
                                $('.messages-menu span.label-success').html(1);
                            }
                        }
                    }
                };

                socket.onerror = function() { $('#messages').append('<p>Connection Error!</p>'); };
                socket.onclose = function() { $('#messages').append('<p>Connection Closed!</p>'); };
            }
        });

    },
    loadChat: function(id){
        console.debug("[loadChat]", "Loading the chat content");
        this.MessagesList = new MessageCollection();
        this.MessagesList.id = id;
        this.MessagesList.fetch({
            beforeSend: setHeader,
            success: function (response, xhr) {
                //console.log("list of messages", xhr);
                _.each(xhr.messages, function(message){
                    //////////////////////////////////////////////////////////////
                    // TODO make different type of message if it is not my message
                    // TODO place the focus at the end of the chat box
                    //////////////////////////////////////////////////////////////
                    //console.log(message);


                    //var localId =  app.MessagesList.where({'messageId': message.messageId});
                    //console.log(message.senderId);
                    //
                    //console.log(message.senderId +"=="+app.UserList.at(0).toJSON().localId);

                    if (message.senderId == app.UserList.at(0).toJSON().localId){
                        $('.direct-chat-messages').append(new MessageLeftView({ model: message }).render().el);
                        console.log("yes");
                    }else{
                        $('.direct-chat-messages').append(new MessageRightView({ model: message }).render().el);
                        console.log("no");
                    }

                    $('.direct-chat-messages').animate({
                        scrollTop: $('.direct-chat-messages')[0].scrollHeight
                    }, 0);
                });
            }
        });

        //$('.direct-chat-messages').animate({
        //    scrollTop: $('.direct-chat-messages')[0].scrollHeight
        //}, 2000);
    },
    common: function(callback) {

        console.debug("[common]", "Checking user...");

        if (this.UserList) {
            if (callback)
                callback();
        } else {

            this.UserList = new UserCollection();
            this.UserList.fetch({
                beforeSend: setHeader,
                success: function(response, xhr) {

                    //console.log(xhr);

                    if(xhr.errorCode == 2){
                        window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
                    }else{
                        //console.log(location.href.split("code=")[1]);
                        //v.ar accessToken = location.href.split("code=")[1];
                        //document.cookie="accessToken="+accessToken+"; expires=Thu, 18 Dec 2013 12:00:00 UTC";

                        $('ul.navbar-nav > li:eq(0)').after( new UserView({ model: xhr }).render().el );
                        $( new UserSidebarView({ model: xhr }).render().el).insertBefore("form.sidebar-form");
                        if (callback)
                            callback();
                    }
                }
            });
        }
    },
    loadPhaseActivities: function(id){
        //$("ul.circle-container").switchClass( "circle-container", "circle-container-secondary",  500);

        $(".menu a.deg0").switchClass( "deg0", "deg0_1",  400);
        $(".menu a.deg45").switchClass( "deg45", "deg45_1",  400);
        $(".menu a.deg135").switchClass( "deg135", "deg135_1",  400);
        $(".menu a.deg180").switchClass( "deg180", "deg180_1",  400);
        $(".menu a.deg225").switchClass( "deg225", "deg225_1",  400);
        $(".menu a.deg315").switchClass( "deg315", "deg315_1",  400);

        $(".menu").switchClass( "menu", "menu_1",  400);

        $("#summary .title-summary").show();
        $("#summary ul.nav-tabs.box-header").hide();

        $("#inquiry-explanation").hide();

        $("ul.box-header.with-border.nav.nav-tabs > li").fadeOut(100);

        $("aside#summary > div").switchClass( "col-md-9", "col-md-2", 500, function(){
            this.ActivityList = new ActivitiesCollection({ });
            this.ActivityList
            this.ActivityList.id = id;

            this.ActivityList.fetch({
                beforeSend: setHeader,
                success: successActivitiesHandler
            });
        });
    },
    loadInquiryUsers: function(id){
        if(!this.InquiryUsers){
            // Load users of the Inquiry
            this.InquiryUsers = new UserRunCollection({});
            this.InquiryUsers.runId = id;
            this.InquiryUsers.fetch({
                beforeSend: setHeader,
                success: successInquiryUsers
            });
        }
    }
});

var successInquiryUsers = function(response, xhr){
    //_.each(xhr.gamesAccess, function(e){
    //    if(app.GameList.where({ 'gameId': e.gameId }) == ""){
    //        this.Game = new Game({ id: e.gameId });
    //        this.Game.fetch({
    //            beforeSend: setHeader,
    //            success: function (response, game) {
    //                $('#inquiries > div > div.box-body').append( new GameListView({ model: game, v: 1 }).render().el );
    //            }
    //        });
    //        app.GameList.add(this.Game);
    //    }
    //});

    console.log(xhr.users);

};
var successGameHandler = function(response, xhr){
    _.each(xhr.gamesAccess, function(e){
        if(app.GameList.where({ 'gameId': e.gameId }) == ""){
            this.Game = new Game({ id: e.gameId });
            this.Game.fetch({
                beforeSend: setHeader,
                success: function (response, game) {
                    $('#inquiries > div > div.box-body').append( new GameListView({ model: game, v: 1 }).render().el );
                }
            });
            app.GameList.add(this.Game);
        }
    });
};

var successGameParticipateHandler = function(response, xhr){
    _.each(xhr.games, function(game){
        if(app.GameList.where({ 'gameId': game.id }) == ""){
            app.GameList.add(game);
            $('#inquiries > div > div.box-body').append( new GameListView({ model: game, v: 2 }).render().el );
        }
    });
};

var successActivitiesHandler = function (response, results) {
    $('#inquiry').html( new PhaseView({ }).render().el );
    $("#inquiry").show();



    console.log(results.generalItems);
    _.each(results.generalItems, function(generalItem){
        //console.log(generalItem);
        ///////////////////////////////////////////////////////////////////////////////////////
        // TODO Re-think how to display the activities
        ///////////////////////////////////////////////////////////////////////////////////////
        //if(typeof(generalItem.dependsOn) != "undefined" && generalItem.dependsOn !== null) {
        //console.log("yes depen");
        //$('section.phase-master > .box-body > ul').append(new ActivityDepencyView({ model: generalItem }).render().el);
        //}else{
        //    console.log("no depen");
        $('section.phase-master > .box-body > ul').append(new ActivityBulletView({ model: generalItem }).render().el);
        //}
    });

};

var successActivityHandler = function(response, xhr){

    //console.debug("Global identifier (RUN)",window.Run.global_identifier);

    var _self = this;

    //if(xhr.type.indexOf("VideoObject") > -1){
    //    console.log(xhr);
    //    $('section.phase-master').after(new ActivityVideoView({ model: xhr }).render().el);
    //}else{
    //    $('section.phase-master').after(new ActivityView({ model: xhr }).render().el);
    //}

    $('section.phase-master').after(new ActivityView({ model: xhr }).render().el);

    $(".close-detail, .cancel").click(function(e){
        //$(".phase-master").animate({
        //    'marginLeft' : '+0%'
        //});

        $(".phase-master").animate({
            'marginLeft' : '+0%',
            'width': '100%',
            'top': '0px'
        });
        $(".phase-master").find(".box-tools.pull-right").show();
        $(".phase-master").find(".skill-tree-row.row-1").show();


        //console.debug(xhr,"saving or closing...");

        app.navigate('inquiry/'+xhr.gameId+'/phase/1');

        $(".phase-detail").hide();
        $(".phase-detail").remove();

        e.preventDefault();
    });

    $('textarea').keyup(function(e){
        //console.log(e);
        if(e.keyCode == 51){
            console.debug("[successActivityHandler]","Display other responses to link them.");
            $(this).trigger('enter');
        }
    });

    $(".save").click(function(){
        $(".message-user").html("Saving..").show().delay(500).fadeOut();

        if  ($('textarea.response').val() == ""){

        }else{
            var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
            newResponse.save({}, {
                beforeSend:setHeader,
                success: function(r, new_response){
                    app.Responses.add(new_response);
                }
            });
        }
    });

    $(".save-close").click(function(){
        $(".message-user").html("Saving..").show().delay(500).fadeOut();

        if  ($('textarea.response').val() == ""){
            //$(".phase-master").animate({
            //    'marginLeft' : '+0%'
            //});

            $(".phase-master").animate({
                'marginLeft' : '+0%',
                'width': '100%',
                'top': '0px'
            });
            $(".phase-master").find(".box-tools.pull-right").show();
            $(".phase-master").find(".skill-tree-row.row-1").show();


            //console.debug(xhr,"saving or closing...");

            app.navigate('inquiry/'+xhr.gameId+'/phase/1');

            $(".phase-detail").hide();
            $(".phase-detail").remove();
        }else{
            var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
            newResponse.save({}, {
                beforeSend:setHeader,
                success: function(r, s){

                    //
                    //$(".phase-master").animate({
                    //    'marginLeft' : '+0%',
                    //    'width': '100%',
                    //    'top': '0px'
                    //});
                    //$(".phase-master").find(".box-tools.pull-right").show();
                    //
                    //console.debug(s, xhr,"saving or closing...");
                    //
                    //app.navigate('inquiry/'+xhr.gameId+'/phase/1');
                    //
                    //$(".phase-detail").hide();
                    //$(".phase-detail").remove();


                    e.preventDefault();
                }
            });

            $(".phase-master").animate({
                'marginLeft' : '+0%',
                'width': '100%',
                'top': '0px'
            });
            $(".phase-master").find(".box-tools.pull-right").show();
            $(".phase-master").find(".skill-tree-row.row-1").show();


            //console.debug(xhr,"saving or closing...");

            app.navigate('inquiry/'+xhr.gameId+'/phase/1');

            $(".phase-detail").hide();
            $(".phase-detail").remove();
        }
    });

};

//var successResponsesHandler = function(response, xhr){
//
//    //console.debug("successResponsesHandler", xhr);
//
//    _.each(xhr.responses, function(response){
//        //console.debug(response,$('.table-hover > tbody'));
//        $('.table-hover > tbody').append(new ResponseView({ model: response }).render().el);
//    });
//};

tpl.loadTemplates(['main', 'game','game_teacher', 'inquiry', 'run', 'user', 'user_sidebar', 'phase', 'activity', 'activity_detail','activity_details', 'inquiry_structure',
    'inquiry_sidebar', 'activityDependency', 'message', 'message_right', 'inquiry_left_sidebar','message_own', 'response','response_author',
    'message_notification', 'activity_video', 'activity_widget'], function() {
    app = new AppRouter();
    Backbone.history.start();
});

/////////////////
// For the future
/////////////////
//window.addEventListener('popstate', function(e) {
//    app.navigate(Backbone.history.getFragment(), { trigger: true, replace: true });
//});
