angular.module('DojoIBL')

    .service('LaService', function ($q, $sce, La, CacheFactory, RunService, ActivityService, InquiryTemplate, AccountService) {


        return {
            sendResponseStatement: function(comment, user, phase, activityId, activity_name, activity_description, groupId, group_title, group_description, projectId, project_title, project_description){
                var newStatement = new La({
                    "version": "1.0.0",
                    "timestamp": "2017-11-04T07:00:14Z",
                    "id": "abcdefghijk123456789",
                    "actor": {
                        "objectType": "Agent",
                        "account": {
                            "homePage": "https://dojo-ibl.appspot.com/#/profile/"+user.localId,
                            "name": user.localId
                        }
                    },
                    "verb": {
                        "id": "http://adlnet.gov/expapi/verbs/commented",
                        "display": {
                            "en-US": "Indicates the learner commented on an activity"
                        }
                    },
                    "object": {
                        "objectType": "Activity",
                        "id": "https://dojo-ibl.appspot.com/#/inquiry/"+groupId+"/phase/"+phase+"/activity/"+activityId,
                        "definition": {
                            "name": {
                                "en-US": activity_name
                            },
                            "description": {
                                "en-US": activity_description
                            },
                            "type": "http://activitystrea.ms/schema/1.0/task"
                        }
                    },
                    "result": {
                        "response": comment
                    },
                    "context": {
                        "extensions": {
                            "http://activitystrea.ms/schema/1.0/place": {
                                "definition": {
                                    "type": "http://activitystrea.ms/schema/1.0/place",
                                    "name": {
                                        "en-US": "Place"
                                    },
                                    "description": {
                                        "en-US": "Represents a physical location."
                                    }
                                },
                                "id": "http://vocab.org/placetime/geopoint/wgs84/X-15.416497Y28.079203.html",
                                "geojson": {
                                    "type": "FeatureCollection",
                                    "features": [
                                        {
                                            "geometry": {
                                                "type": "Point",
                                                "coordinates": [
                                                    0,
                                                    0
                                                ]
                                            },
                                            "type": "Feature"
                                        }
                                    ]
                                },
                                "objectType": "Place"
                            }
                        },
                        "contextActivities": {
                            "parent": [{
                                "objectType": "Group",
                                "definition": {
                                    "type": "http://activitystrea.ms/schema/1.0/group",
                                    "name": {
                                        "en-US": group_title
                                    },
                                    "description": {
                                        "en-US": group_description
                                    }
                                },
                                "id": groupId
                            },{
                                "objectType": "Project",
                                "definition": {
                                    "type": "http://adlnet.gov/expapi/activities/course",
                                    "name": {
                                        "en-US": project_title
                                    },
                                    "description": {
                                        "en-US": project_description
                                    }
                                },
                                "id": projectId
                            }]
                        }
                    },
                    "authority": {
                        "objectType": "Agent",
                        "name": "",
                        "mbox": "mailto:"+user.email
                    },
                    "stored": "2015-11-04T07:00:10.858600+00:00"
                    });

                return newStatement.$save();
            }
        }
    }
);