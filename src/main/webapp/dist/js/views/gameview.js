//window.GameListView = Backbone.View.extend({
//    tagName:  "div",
//    className: "col-md-4",
//    initialize:function () {
//
//        console.log("A game view has been initialized");
//
//        //console.log(this.options.model2);
//
//        this.template = _.template(tpl.get('game'));
//
//
//
//    },
//    events: {
//        'click #show-runs' : 'showRuns'
//
//    },
//    render: function () {
//        $(this.el).html(this.template(this.model));
//        return this;
//    },
//    showRuns: function(e){
//        e.preventDefault();
//        console.log(this.model);
//        console.log(_.template(tpl.get('run')));
//        var subject = $('#search').val() || 'NYC';
//        this.tweets.url = "http://localhost:8888/rest/myGames/gameId/"+ this.id;
//        this.tweets.fetch();
//        //$(this).append();
//    }
//
//    //,
//    //events:{
//    //    "click .save":"saveWine"
//    //},
//    //saveWine:function () {
//    //    //this.model.set({
//    //    //    name:$('#name').val(),
//    //    //    grapes:$('#grapes').val(),
//    //    //    country:$('#country').val(),
//    //    //    region:$('#region').val(),
//    //    //    year:$('#year').val(),
//    //    //    description:$('#description').val()
//    //    //});
//    //    if (this.model.isNew()) {
//    //        var self = this;
//    //        app.wineList.create(this.model, {
//    //            success:function () {
//    //                app.navigate('wines/' + self.model.id, false);
//    //            }
//    //        });
//    //    } else {
//    //        this.model.save();
//    //    }
//    //
//    //    return false;
//    //}
//});

// Main
window.MainView = Backbone.View.extend({
    tagName: "div",
    className: "box box-success box-solid",
    initialize:function () {
        this.template = _.template(tpl.get('main'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

// Game
window.GameListView = Backbone.View.extend({
    tagName:  "div",
    initialize:function (options) {
        //console.log(options);

        //if(options.model){
            this.template = _.template(tpl.get('game'));
        //}

        //if(options.model2){
        //    this.template = _.template(tpl.get('game'));
        //}
    },
    events: {
        'click .show-runs-student' : 'showRunsStudents',
        'click .show-runs-teacher' : 'showRunsTeacher'
    },
    render: function () {
        //if(this.model){
        //    $(this.el).html(this.template(this.model));
        //    console.log(this.model);
        //}

        //if(this.model2){
            $(this.el).html(this.template(this.model));
            //console.log(this.model2);
        //}

        return this;
    },
    showRunsStudents: function(e){
        e.preventDefault();
        var aux = $(this.el);
        $(".nav.nav-stacked").slideUp(400).html("");

        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
        this.RunAccessList.fetch({
            beforeSend: setHeader,

            success: function(response, xhr) {

                if(xhr.runs.length == 0){
                    $(aux).find(".nav.nav-stacked").hide().html("<li><a href=''>No inquiries</a></li>").slideDown(400);
                }else{
                    _.each(xhr.runs, function(run){
                        $(aux).find(".nav.nav-stacked").hide().html(new this.RunListView({ model: run }).render().el).slideDown(400);
                    });
                }
            }
        });
    },
    showRunsTeacher: function(e){
        e.preventDefault();
    }
});

// Run
window.RunListView = Backbone.View.extend({
    tagName:  "li",
    //className: "col-md-4",
    initialize:function () {
        this.template = _.template(tpl.get('run'));
        //console.log(1,this.template);
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Inquiries
window.InquiryView = Backbone.View.extend({
    tagName:  "div",
    className: "col-md-9",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquiryLeftSidebarView = Backbone.View.extend({
    tagName: "div",
    className: "col-md-2 pull-left",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_left_sidebar'));
        console.log("load inquiry left sidebar");
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquirySidebarView = Backbone.View.extend({
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_sidebar'));
        console.log("load inquiry sidebar");
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquiryStructureView = Backbone.View.extend({
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_structure'));
    },
    events: {
        //'click .fade-this-in.skill-badge-small': 'add',
        //'click ul > li > a': 'access_phase'

    },
    access_phase: function (ev){

        //var name_class = $(ev.target).parent();
        //var menu_div = $(ev.target).parent().parent().parent();

        //$(ev.target).parent().siblings().hide();

        //$(name_class).css('-webkit-transform','rotate(0deg) translate(0) rotate(0deg)');
      },
    add: function(ev){
        console.debug("[Add event InquiryStructureView]","Click in activity. Hiding siblings...");
        //$(ev.target).parent().parent().parent().parent().siblings().hide();
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Phases
window.PhaseView = Backbone.View.extend({
    initialize:function () {
        this.template = _.template(tpl.get('phase'));
    },
    events: {
        'click .close-phase': 'close_phase'
    },
    close_phase: function (ev){
        console.debug("[close_phase event PhaseView]","Closing phase.");

        if($.cookie("dojoibl.run")){
        //    app.showInquiry($.cookie("dojoibl.run"));
            app.navigate('inquiry/' + $.cookie("dojoibl.run"));
        }

        $("aside#summary > div").switchClass( "col-md-2", "col-md-9",  200, function(){


            //$(".circle-container li.deg0").css('-webkit-transform','translate(10em)');
            //$(".circle-container li.deg0").css('-ms-transform','translate(10em)');
            //$(".circle-container li.deg0").css('transform','translate(10em)');

            $("#inquiry-explanation").fadeIn();
            $("ul > li").show();
            $("ul.box-header.with-border.nav.nav-tabs > li").show();
            $("#summary .title-summary").hide();
            $("#summary ul.nav-tabs.box-header").show();
        });

        $(".menu_1 a.deg0_1").switchClass( "deg0_1", "deg0",  400);
        $(".menu_1 a.deg45_1").switchClass( "deg45_1", "deg45",  400);
        $(".menu_1 a.deg135_1").switchClass( "deg135_1", "deg135",  400);
        $(".menu_1 a.deg180_1").switchClass( "deg180_1", "deg180",  400);
        $(".menu_1 a.deg225_1").switchClass( "deg225_1", "deg225",  400);
        $(".menu_1 a.deg315_1").switchClass( "deg315_1", "deg315",  400);

        $(".menu_1").switchClass( "menu_1", "menu",  400);

        //$("ul.circle-container-secondary").switchClass( "circle-container-secondary", "circle-container",  800,function(){
        //    //$(".circle-container li.deg0").css('-webkit-transform','translate(10em)');
        //    //$(".circle-container li.deg0").css('-ms-transform','translate(10em)');
        //    //$(".circle-container li.deg0").css('transform','translate(10em)');
        //    //
        //    //$(".circle-container > *:nth-of-type(2)").css('-webkit-transform','rotate(60deg) translate(10em) rotate(-60deg)');
        //    //$(".circle-container > *:nth-of-type(2)").css('-ms-transform','rotate(60deg) translate(10em) rotate(-60deg)');
        //    //$(".circle-container > *:nth-of-type(2)").css('transform','rotate(60deg) translate(10em) rotate(-60deg)');
        //    //
        //    //$(".circle-container > *:nth-of-type(3)").css('-webkit-transform','rotate(120deg) translate(10em) rotate(-120deg)');
        //    //$(".circle-container > *:nth-of-type(3)").css('-ms-transform','rotate(120deg) translate(10em) rotate(-120deg)');
        //    //$(".circle-container > *:nth-of-type(3)").css('transform','rotate(120deg) translate(10em) rotate(-120deg)');
        //    //
        //    //$(".circle-container > *:nth-of-type(4)").css('-webkit-transform','rotate(180deg) translate(10em) rotate(-180deg)');
        //    //$(".circle-container > *:nth-of-type(4)").css('-ms-transform','rotate(180deg) translate(10em) rotate(-180deg)');
        //    //$(".circle-container > *:nth-of-type(4)").css('transform','rotate(180deg) translate(10em) rotate(-180deg)');
        //    //
        //    //$(".circle-container > *:nth-of-type(5)").css('-webkit-transform','rotate(240deg) translate(10em) rotate(-240deg)');
        //    //$(".circle-container > *:nth-of-type(5)").css('-ms-transform','rotate(240deg) translate(10em) rotate(-240deg)');
        //    //$(".circle-container > *:nth-of-type(5)").css('transform','rotate(240deg) translate(10em) rotate(-240deg)');
        //    //
        //    //$(".circle-container > *:nth-of-type(6)").css('-webkit-transform','rotate(300deg) translate(10em) rotate(-300deg)');
        //    //$(".circle-container > *:nth-of-type(6)").css('-ms-transform','rotate(300deg) translate(10em) rotate(-300deg)');
        //    //$(".circle-container > *:nth-of-type(6)").css('transform','rotate(300deg) translate(10em) rotate(-300deg)');
        //
        //
        //});


        //$(".circle-container li").switchClass( "deg0", "deg0_1",  800);

        //$(".circle-container li.deg0").css('-webkit-transform','translate(10em)');
        //$(".circle-container li.deg0").css('-ms-transform','translate(10em)');
        //$(".circle-container li.deg0").css('transform','translate(10em)');

        $("#inquiry").hide();

        ev.preventDefault();
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

// Messages
window.MessageOwnView = Backbone.View.extend({
    tagName:  "div",
    className: "direct-chat-msg",
    initialize:function (a) {
        //console.log(a);
        this.template = _.template(tpl.get('message_own'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.MessageLeftView = Backbone.View.extend({
    tagName:  "div",
    className: "direct-chat-msg",
    initialize:function () {
        this.template = _.template(tpl.get('message'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.MessageRightView = Backbone.View.extend({
    tagName:  "div",
    className: "direct-chat-msg right",
    initialize:function () {
        this.template = _.template(tpl.get('message_right'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Activities
window.ActivityBulletView = Backbone.View.extend({
    tagName:  "li",
    className: "skill-tree-row row-1",
    initialize:function () {
        this.template = _.template(tpl.get('activity'));
    },
    events: {
        //'click .skill-badge-small' : 'showDetail',
        //'click .close-detail.cancel.save' : 'closeDetail',
        //'click .save' : 'saveActivity'
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.ActivityView = Backbone.View.extend({
    tagName: 'section',
    className: 'phase-detail box box-success box-solid',
    initialize:function (xhr) {
        console.log(xhr);
        if(xhr.model.type.indexOf("VideoObject") > -1){
            this.template = _.template(tpl.get('activity_video'));
        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
            this.template = _.template(tpl.get('activity_widget'));
        }else{
            this.template = _.template(tpl.get('activity_detail'));
        }
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.ActivityDepencyView = window.ActivityView.extend({
    tagName:  "span",
    id: "skill-1-2",
    //className: "skill-tree-row row-1",
    initialize:function () {
        this.template = _.template(tpl.get('activityDependency'));
    }
});

// Responses
window.ResponseListView = Backbone.View.extend({
    tagName: "tbody",
    initialize: function(){
        this.template = _.template(tpl.get('activity_details'));

        _(this).bindAll('render');
        this.collection.bind('add', this.render);
        //this.model.bind('change', this.render);
        //console.log(this.model);
    },
    //events: {
    //    'click .save' : 'createResponse'
    //},
    //createResponse: function() {
    //    $(".message-user").html("Saving..").show().delay(500).fadeOut();
    //
    //    if  ($('textarea.response').val() == ""){
    //
    //    }else{
    //        var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
    //        newResponse.save({}, { beforeSend:setHeader });
    //
    //        this.collection.add(newResponse);
    //        console.log(this.collection);
    //    }
    //},
    render: function(){
        //console.log("render");
        $('#activity-responses').append(this.template());
        _.each(this.collection.models, function(model){
            //console.log(model.toJSON().responseValue);
            $('#activity-responses').append(new ResponseView({ model: model.toJSON() }).render().el);
        }, this);
        this.collection.reset();

        $(".form-control.input-sm.pull-right").keyup(function () {
            //split the current value of searchInput
            var data = this.value.split(" ");
            //create a jquery object of the rows
            var jo = $("#activity-responses > tbody").find("tr");
            if (this.value == "") {
                jo.show();
                return;
            }
            //hide all the rows
            jo.hide();

            //Recusively filter the jquery object to get results.
            jo.filter(function (i, v) {
                var $t = $(this);
                for (var d = 0; d < data.length; ++d) {
                    if ($t.is(":contains('" + data[d] + "')")) {
                        return true;
                    }
                }
                return false;
            })
                //show the rows that match.
                .show();
        }).focus(function () {
            this.value = "";
            $(this).css({
                "color": "black"
            });
            $(this).unbind('focus');
        }).css({
            "color": "#C0C0C0"
        });
    }
});

window.ResponseView = Backbone.View.extend({
    tagName: "tr",
    model: Response,
    initialize: function() {
         this.template = _.template(tpl.get('response'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Users
window.UserView = Backbone.View.extend({
    tagName:  "li",
    className: "dropdown user user-menu",
    initialize:function () {
        this.template = _.template(tpl.get('user'));
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.UserSidebarView = Backbone.View.extend({
    tagName:  "div",
    className: "user-panel",
    initialize:function () {
        this.template = _.template(tpl.get('user_sidebar'));
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Notifications
window.MessageNotificationView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('message_notification'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});
