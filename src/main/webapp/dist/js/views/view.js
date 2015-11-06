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

//// Main
//window.MainView = Backbone.View.extend({
//    tagName: "div",
//    className: "row",
//    initialize:function () {
//        this.template = _.template(tpl.get('main'));
//    },
//    render:function () {
//        $(this.el).html(this.template(this.model));
//
//        return this;
//    }
//});

// Game
window.GameListView = Backbone.View.extend({
    tagName:  "div",
    className: "col-lg-3 animated fadeInUp",
    initialize:function (options) {
        //this.collection.bind('add', this.render);
        this.listenTo(app.GameList, 'add', this.show);
        if(options.v == 1){
            this.template = _.template(tpl.get('game_teacher'));
        }

        if(options.v == 2){
            this.template = _.template(tpl.get('game'));
        }
    },
    events: {
        'click .show-runs' : 'showRuns'
    },
    show: function(){
        console.log("render");
    },
    render: function () {
        //_.each(this.collection.models, function(response){
        //
        //}, this);
        //
        //this.collection.reset();

        //console.log("render");

        $(this.el).html(this.template({ model: this.model }));
        return this;
    },
    showRuns: function(e){
        e.preventDefault();
        var _aux = $(this.el).find(".widget-text-box");

        console.log($(_aux).length);
        $(_aux).slideUp(200).html("");

        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
        this.RunAccessList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                if(xhr.runs.length == 0){
                    console.error("The game does not have runs. There have been a problem during the creation of the inquiry.")
                    //$(_aux).html("<li><a href=''>No inquiries</a></li>").slideDown(200);
                }else if(xhr.runs.length == 1){
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListView({ model: run }).render().el).slideDown(200);
                    });
                }else{
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListView({ model: run }).render().el).slideDown(200);
                    });
                }
            }
        });
    }
});

// Run
window.RunListView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('run'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Inquiries
window.InquiryView = Backbone.View.extend({
    tagName:  "div",
    id: "inquiry-content",
    className: "col-md-9 wrapper wrapper-content animated fadeInUp",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquiryNewView = Backbone.View.extend({
    tagName:  "div",
    className: "col-md-12 wrapper wrapper-content animated fadeInUp",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_new'));

    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.SideBarView = Backbone.View.extend({
    tagName:  "div",
    className: "col-lg-3 wrapper wrapper-content",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_sidebar'));
    },
    render:function () {
        $(this.el).html(this.template());

        return this;
    }
});

// Phases
window.PhaseView = Backbone.View.extend({
    className: "animated fadeInUp",
    initialize:function () {
        this.template = _.template(tpl.get('phase'));
    },
    render:function () {
        $(this.el).html(this.template());

        return this;
    }
});

// Activities
window.ActivityBulletView = Backbone.View.extend({
    tagName: "li",
    initialize:function (options) {
        this.phase = options.phase;
        this.template = _.template(tpl.get('activity'));
        $(this.el).attr("data", this.model.id);
    },
    render:function () {
        $(this.el).html(this.template({model: this.model, phase: this.phase}));
        return this;
    }
});

window.ItemBreadcrumbView = Backbone.View.extend({
    tagName: "div",
    className: "move-right-breadcrumb",
    initialize:function () {
        this.template = _.template(tpl.get('item_breadcrumb_activity'));
    },
    render:function () {
        $(this.el).html(this.template({ }));
        return this;
    }
});


// Responses
window.ResponseListView = Backbone.View.extend({
    initialize: function(options){
        _(this).bindAll('render');
        this.collection.bind('add', this.render);
        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;
    },
    render: function(){
        _.each(this.collection.models, function(response){
            var aux = response.toJSON().userEmail.split(':');
            var user = this.users.where({ 'localId': aux[1] });

            $(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el).insertBefore($('#list_answers > .social-comment:last-child'));
        }, this);

        this.collection.reset();

        $('.reply').click(function(e){
            if($(this).parent().parent().find(".response").length == 0){
                console.log("add");
                $(this).parent().parent().append(new ResponseReplyView({}).render().el);
            }else{
                console.log("remove");
                $(this).parent().siblings(".social-comment:last").remove();
            }

            e.preventDefault();
        });

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
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response'));
        this.user = options.user;
    },
    render:function () {
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: "Now" }));
        }else{
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()) }));
        }
        return this;
    }
});

window.ResponseReplyView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function() {
        this.template = _.template(tpl.get('response_reply'));
    },
    render:function () {
        $(this.el).append(this.template());

        return this;
    }
});


window.ActivityView = Backbone.View.extend({
    initialize:function (xhr) {
        if(xhr.model.type.indexOf("VideoObject") > -1){
            console.debug("VideoObject");
            this.template = _.template(tpl.get('activity_video'));
        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
            console.debug("OpenBadge");
            this.template = _.template(tpl.get('activity_widget'));
        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
            this.template = _.template(tpl.get('activity_discussion'));
        }else if(xhr.model.type.indexOf("MultipleChoiceImageTest") > -1) {
            this.template = _.template(tpl.get('activity_tree_view'));
        }else if(xhr.model.type.indexOf("NarratorItem") > -1) {
            this.template = _.template(tpl.get('activity_concept_map'));
        }else{
            console.debug("Discussion");
            this.template = _.template(tpl.get('activity_detail'));
        }
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        $(this.el).find('#nestable3').nestable();
        return this;
    }
});


window.ConceptMapView = Backbone.View.extend({
    initialize: function(){
    },
    render: function(){

        var jsonObj = [];
        var nodes = [];
        var links = [];

        _.each(this.model.responses, function(response){
            var item_nodes = {}
            item_nodes ["id"] = response.responseId;
            item_nodes ["name"] = response.responseValue;
            var min = 50;
            var max = 550;
            var random = Math.floor(Math.random() * (max - min + 1)) + min;
            item_nodes ["x"] = random;
            var min = 50;
            var max = 150;
            var random = Math.floor(Math.random() * (max - min + 1)) + min;
            item_nodes ["y"] = random;

            nodes.push(item_nodes);
        }, this);

        var item2 = {};
        item2 ["nodes"] = nodes;

        var item_links = {};

        item_links["source"] = 0;
        item_links["target"] = 1;

        links.push(item_links);

        item2 ["links"] = links;
        jsonObj.push(item2);

        this.renderMindMap(JSON.stringify(jsonObj));
        console.log(JSON.stringify(jsonObj));

        return this;
    },
    renderMindMap: function(jsonObj){

        console.log()

        var width = $("#concept-map").width(),
            height = 500,
            shiftKey;

        var svg = d3.select("#concept-map")
            .each(function() { this.focus(); })
            .append("svg")
            .attr("width", width)
            .attr("height", height);

        var link = svg.append("g")
            .attr("class", "cp-link")
            .selectAll("line");

        var brush = svg.append("g")
            .datum(function() { return {selected: false, previouslySelected: false}; })
            .attr("class", "cp-brush");

        var node = svg.append("g")
            .attr("class", "node")
            .selectAll("rect");

        var labels = svg.append("g")
            .attr("class", "labels")
            .selectAll("text");

        var graph = JSON.parse(jsonObj)[0];

        graph.links.forEach(function(d) {
            d.source = graph.nodes[d.source];
            d.target = graph.nodes[d.target];
        });

        link = link.data(graph.links).enter().append("line")
            .attr("x1", function(d) {
                return d.source.x+(d.source.name.length * 7)/2;
            })
            .attr("y1", function(d) {
                return d.source.y+20;
            })
            .attr("x2", function(d) {
                return d.target.x+(d.target.name.length * 7)/2;
            })
            .attr("y2", function(d) {
                return d.target.y+20;
            });

        brush.call(d3.svg.brush()
            .x(d3.scale.identity().domain([0, width]))
            .y(d3.scale.identity().domain([0, height]))
            .on("brushstart", function(d) {
                console.log("brushstart");
                node.each(function(d) { d.previouslySelected = shiftKey && d.selected; });
            })
            .on("brush", function() {
                var extent = d3.event.target.extent();
                node.classed("selected", function(d) {
                    return d.selected = d.previouslySelected ^
                    (extent[0][0] <= d.x && d.x < extent[1][0]
                    && extent[0][1] <= d.y && d.y < extent[1][1]);
                });
            })
            .on("brushend", function() {
                d3.event.target.clear();
                d3.select(this).call(d3.event.target);
            }));

        node = node.data(graph.nodes).enter().append("rect")
            .attr("x", function(d) { return d.x; })
            .attr("y", function(d) { return d.y; })
            .attr("width", function(d) {
                console.log(d.name.length);
                return d.name.length*7;
            })
            .attr("height", function(d) {
                return 30;
            })
            .on("mousedown", function(d) {
                if (!d.selected) { // Don't deselect on shift-drag.
                    if (!shiftKey){
                        node.classed("selected", function(p) { return p.selected = d === p; });
                        labels.classed("selected", function(p) { return p.selected = d === p; });
                    }
                    else{
                        d3.select(this).classed("selected", d.selected = true);
                    }
                }
            })
            .on("mouseup", function(d) {
                if (d.selected && shiftKey){
                    d3.select(this).classed("selected", d.selected = false);
                }
            })
            .on("dblclick", function(d){
                console.log("dblclick");
            })
            .on("contextmenu", function(data, index){
                d3.event.preventDefault();
            })
            .call(d3.behavior.drag()
                .on("drag", function(d) { nudge(d3.event.dx, d3.event.dy); }));

        labels = labels.data(graph.nodes).enter().append("text")
            .attr("y", function(d) {
                return d.y+20;
            })
            .attr("x", function(d) {
                return d.x+10;
            })
            .text(function(d){
                return d.name
            })
            .on("mousedown", function(d) {
                if (!d.selected) { // Don't deselect on shift-drag.
                    if (!shiftKey){
                        node.classed("selected", function(p) { return p.selected = d === p; });
                        labels.classed("selected", function(p) { return p.selected = d === p; });
                    }
                    else{
                        d3.select(this).classed("selected", d.selected = true);
                    }
                }
            })
            .on("mouseup", function(d) {
                if (d.selected && shiftKey){
                    d3.select(this).classed("selected", d.selected = false);
                }
            })
            .call(d3.behavior.drag()
                .on("drag", function(d) { nudge(d3.event.dx, d3.event.dy); }));

        function nudge(dx, dy) {
            node.filter(function(d) { return d.selected; })
                .attr("x", function(d) { return d.x += dx; })
                .attr("y", function(d) { return d.y += dy; });

            link.filter(function(d) { return d.source.selected; })
                .attr("x1", function(d) { return d.source.x+(d.source.name.length * 7)/2; })
                .attr("y1", function(d) { return d.source.y+20; });

            link.filter(function(d) { return d.target.selected; })
                .attr("x2", function(d) { return d.target.x+(d.target.name.length * 7)/2; })
                .attr("y2", function(d) { return d.target.y+20; });

            labels.filter(function(d) { return d.selected; })
                .attr("x", function(d) { return d.x+10; })
                .attr("y", function(d) { return d.y+20; });
        }
    }
});

// =====================================================






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
        console.log("hola");
    },
    events: {
        'click ul#circlemenu > li > div > a': 'open_phase'
    },
    open_phase: function(e){
        console.debug("Access phase");

        //$(e.currentTarget).parent().parent().siblings().hide();

        //$("ul#circlemenu").attr("id", "circlemenu2");

        //$("#summary .title-summary").show();
        //$("#summary ul.nav-tabs.box-header").hide();
        //
        //$("#inquiry-explanation").hide();
        //
        //$("ul.box-header.with-border.nav.nav-tabs > li").fadeOut(100);

    },
    add: function(ev){
        console.debug("[Add event InquiryStructureView]","Click in activity. Hiding siblings...");
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});



// Messages
window.MessageFromNotificationView = Backbone.View.extend({
    tagName:  "div",
    className: "right",
    initialize:function (a) {
        this.template = _.template(tpl.get('message_own'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.MessageLeftView = Backbone.View.extend({
    tagName:  "div",
    className: "left",
    initialize:function () {
        this.template = _.template(tpl.get('message_right'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.MessageRightView = Backbone.View.extend({
    tagName:  "div",
    className: "right",
    initialize:function () {
        this.template = _.template(tpl.get('message'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});
//
//
//window.ActivityView = Backbone.View.extend({
//    tagName: 'section',
//    className: 'phase-detail box box-success box-solid',
//    initialize:function (xhr) {
//        if(xhr.model.type.indexOf("VideoObject") > -1){
//            this.template = _.template(tpl.get('activity_video'));
//        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
//            this.template = _.template(tpl.get('activity_widget'));
//        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
//            this.template = _.template(tpl.get('activity_discussion'));
//        }else if(xhr.model.type.indexOf("MultipleChoiceImageTest") > -1) {
//            this.template = _.template(tpl.get('activity_tree_view'));
//        }else{
//            this.template = _.template(tpl.get('activity_detail'));
//        }
//    },
//
//    render:function () {
//        $(this.el).html(this.template(this.model));
//        $(this.el).find('#nestable3').nestable();
//        return this;
//    }
//});

window.ActivityDepencyView = window.ActivityView.extend({
    tagName:  "span",
    id: "skill-1-2",
    //className: "skill-tree-row row-1",
    initialize:function () {
        this.template = _.template(tpl.get('activityDependency'));
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
