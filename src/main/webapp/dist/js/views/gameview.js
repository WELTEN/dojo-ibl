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
    className: "col-md-3 col-lg-3 col-xl-3",
    initialize:function (options) {
        console.log(options);

        //var variables = { search_label: options.v };
        var data = {"data": {"title": options.v}};

        if(options.v == 1){
            this.template = _.template(tpl.get('game_teacher'));
        }

        if(options.v == 2){
            this.template = _.template(tpl.get('game'));
        }

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

        console.log(aux);

        $(".nav.nav-stacked").slideUp(200).html("");

        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
        this.RunAccessList.fetch({
            beforeSend: setHeader,

            success: function(response, xhr) {

                if(xhr.runs.length == 0){
                    $(aux).find(".nav.nav-stacked").hide().html("<li><a href=''>No inquiries</a></li>").slideDown(200);
                }else{
                    _.each(xhr.runs, function(run){
                        $(aux).find(".nav.nav-stacked").hide().html(new this.RunListView({ model: run }).render().el).slideDown(200);
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
        'click ul#circlemenu > li > div > a': 'open_phase'
    },
    open_phase: function(e){
        console.debug("Access phase");

        $(e.currentTarget).parent().parent().siblings().hide();

        $("ul#circlemenu").attr("id", "circlemenu2");

        $("#summary .title-summary").show();
        $("#summary ul.nav-tabs.box-header").hide();

        $("#inquiry-explanation").hide();

        $("ul.box-header.with-border.nav.nav-tabs > li").fadeOut(100);

    },
    add: function(ev){
        console.debug("[Add event InquiryStructureView]","Click in activity. Hiding siblings...");
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Phases
window.PhaseView = Backbone.View.extend({
    tagName: "div",
    className: "col-md-7",
    initialize:function () {
        this.template = _.template(tpl.get('phase'));
    },
    events: {
        'click .close-phase': 'close_phase'
    },
    close_phase: function (ev){

        console.debug("[close_phase event PhaseView]","Closing phase.");

        if($.cookie("dojoibl.run")){
            app.navigate('inquiry/' + $.cookie("dojoibl.run"));
        }

        $("aside#summary > div").switchClass( "col-md-2", "col-md-9", 200, function(){

            $("ul#circlemenu2").attr("id", "circlemenu");

            //$(".circle-container li.deg0").css('-webkit-transform','translate(10em)');
            //$(".circle-container li.deg0").css('-ms-transform','translate(10em)');
            //$(".circle-container li.deg0").css('transform','translate(10em)');

            $("#inquiry-explanation").fadeIn();
            $("ul#circlemenu").children().show();
            //$("ul > li").show();
            $("ul.box-header.with-border.nav.nav-tabs > li").show();
            $("#summary .title-summary").hide();
            $("#summary ul.nav-tabs.box-header").show();
        });

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
        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
            this.template = _.template(tpl.get('activity_discussion'));
        }else if(xhr.model.type.indexOf("MultipleChoiceImageTest") > -1) {
            this.template = _.template(tpl.get('activity_tree_view'));
        }else{
            this.template = _.template(tpl.get('activity_detail'));
        }
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        $(this.el).find('#nestable3').nestable();
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
    initialize: function(options){
        this.template = _.template(tpl.get('activity_details'));

        _(this).bindAll('render');

        this.collection.bind('add', this.render);

        this.users = options.users;
    },
    render: function(){
        //if($('#activity-responses').length == 0){
            _.each(this.collection.models, function(response){
                console.debug(response);
                var aux = response.toJSON().userEmail.split(':');
                var user = this.users.where({ 'localId': aux[1] });
                $('#activity-responses').append(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el);
            }, this);
        //}

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
    initialize: function(options) {
         this.template = _.template(tpl.get('response'));
         this.template_author = _.template(tpl.get('response_author'));

        this.user = options.user;
    },
    render:function () {
        // TODO sometimes JSON error. I need to check this.
        $(this.el).append(this.template_author(this.user.toJSON()));
        $(this.el).append(this.template(this.model));
        return this;
    }
});

window.ResponseDiscussionListView = Backbone.View.extend({
    el: $(".box-footer.box-comments"),
    initialize: function(options){

        _(this).bindAll('render');

        this.collection.bind('add', this.render);

        this.users = options.users;
    },
    render: function(){
        _.each(this.collection.models, function(response){

            var aux = response.toJSON().userEmail.split(':');
            var user = this.users.where({ 'localId': aux[1] });

            $(".box-footer.box-comments").append(new ResponseDiscussionView({ model: response.toJSON(), user: user[0] }).render().el);
        }, this);

        $(".number-comments").html(this.collection.models.length == 1) ?
        this.collection.models.length+" comment" :
        this.collection.models.length+" comments"

        this.collection.reset();
    }
});

window.ResponseDiscussionView = Backbone.View.extend({
    tagName: "div",
    className: "box-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response_discussion'));
        this.template_author = _.template(tpl.get('response_discussion_author'));

        this.user = options.user;
    },
    render:function () {
        // TODO sometimes JSON error. I need to check this.
        $(this.el).append(this.template_author(this.user.toJSON()));

        $(this.el).append(this.template(this.model));
        $(this.el).find(".username").prepend(this.user.toJSON().name);
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
        }else{
            $(this.el).find(".username > .text-muted.pull-right").prepend(jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()));
        }
        return this;
    }
});

window.ResponseTreeView = Backbone.View.extend({
    initialize: function(){

        _(this).bindAll('render');

        this.collection.bind('add', this.render);
    },
    render: function(){
        _.each(this.collection.models, function(response){
            $("#nestable3 ol.dd-list").append(new ResponseTreeviewItemView({ model: response.toJSON() }).render().el);
        }, this);

        this.collection.reset();
    }
});
window.ResponseTreeviewItemView = Backbone.View.extend({
    tagName: "li",
    className: "dd-item dd3-item",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response_treeview'));
    },
    render:function () {

        $(this.el).append(this.template(this.model));

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

window.UsersInquiryView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('user_inquiry'));
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

window.GeneralFloatingNotificationView = Backbone.View.extend({
    tagName:  "div",
    className: "ui-pnotify stack-topleft",
    initialize:function () {
        this.template = _.template(tpl.get('notification_floating'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.NotificationSideBarView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('notification_sidebar'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});
