var express     =   require("express");
var app         =   express();
var bodyParser  =   require("body-parser");
var mongoOp     =   require("./models/users");
var Acc        =   require("./models/acc");
var Inv        =   require("./models/Involved");
var Witness      =   require("./models/witness");
var Pic        =   require("./models/pics");
var myPic        =   require("./models/my_pics");
var router      =   express.Router();
var mongoose    =   require("mongoose");
var _ = require('underscore');
var multer  =   require('multer');
var fs = require('fs');
var imageDirPath = "./uploads";

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({"extended" : false}));


if (!fs.existsSync(imageDirPath)){
        fs.mkdirSync(imageDirPath);
}
storage = multer.diskStorage({ //multers disk storage settings
        destination: function (req, file, cb) {
            cb(null, imageDirPath);
            },
        filename: function (req, file, cb) {
                cb(null, file.originalname);
                }
    });

upload = multer({ //multer settings
        storage: storage
        }).single('file');

router.get("/",function(req,res){
    res.json({"error" : false,"message" : "Hello World"});
});

router.route("/users")
    .get(function(req,res){
        var response = {};
        var done = false;
        mongoOp.find({},function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"message" : data};
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }

        res.json(response);
    })
    .post(function(req,res){
        var response = {};

        console.log(req.body);

        var user = new mongoOp();
        user.email = req.body.email;
        user.name = req.body.name;
        user.phone = req.body.phone;
        console.log(user);
        user.save(function(err){
            if(err) {
                response = {"error" : true,"user" : "error"};
            }
            else
            {
                response = {"error" : false, "user" : user.id};
            }

            res.json(response);
        });
    });

router.route("/acc")
    .get(function(req,res){
        var response = {};
        var done = false;
        Acc.find({},function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"message" : data};
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }

        res.json(response);
    })
    .post(function(req, res) {

        var acc = new Acc();
        acc.name = req.body.name;
        acc.location = req.body.location;
        acc.my_id = req.body.my_id;
        acc.opp_id = req.body.opp_id;
        acc.desc = "";

        acc.save(function(err){
            if(err) {
                response = {"error" : true,"id" : "error"};
            }
            else {
                response = {"error" : false,"id" : acc.id};
            }

            res.json(response);
        });
    });

router.route("/acc/:id")
    .get(function(req, res) {
       
        response = {}
        var done = false;

        Acc.findById(req.params.id,function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
                res.json(response);
            } else {

                response = {"acc" : data};
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }
        
        res.json(response);
    });


router.route("/user/:id/pic/:name")
    .get(function(req,res){
        var response = {};
        var done = false;
        console.log(req.params);
        myPic.find({user_id : req.params.id,  orignal_name : req.params.name },function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                
                if (data[0] != undefined)
                {
                    response = {"error" : false,"pic" : data[0]._id + ".jpg" };
                }
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }

        res.json(response);
    });
router.route("/user/:id/pic")
    .get(function(req,res){
        var response = {};
        var done = false;
        myPic.find({user_id : req.params.id},function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"message" : data};
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }

        res.json(response);
    });
router.route("/user/:id/pic")
    .post(function(req, res) {


        upload(req,res,function(err){
            if(err){
                res.json({error_code:1,err_desc:err});
                return;
            }
            
            myPic.remove({user_id : req.params.id, orignal_name : req.file.originalname}, function(err) {});
            
            var pic = new myPic();
            pic.user_id = req.params.id;
            pic.orignal_name = req.file.originalname;

            pic.save(function(err){
                    if(err) {
                        response = {"error" : true,"id" : "error"};
                    }
                    else {
                        response = {"error" : false,"id" : pic.id};

                    fs.rename("./uploads/"+req.file.originalname,"./uploads/" + pic.id + ".jpg",function(err){
                        if(err){
                        res.json({error_code:1,err_desc:err});
                        return;
                        }

                        res.json(response);
                    });
                    }
                });
            });
    })
    .get(function(req, res) {
        var done = false;
        var response = {};

        Acc.findOne({ opp_id : req.params.id },function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
                res.json(response);
            } else {
                console.log(data);
                filter = [{acc_id : req.params.id}]

                if (data != null)
                {
                    filter.push({acc_id : data._id});
                }

                Pic.find({ $or : filter} , function(err, pic_data) {
                    if (err) {
                        response = { "error" : true, "pics" : "error" }
                    }
                    else
                    {
                        response = { "error" : false, "pics" : pic_data }
                    }

                    done = true;
            });

        }});
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }
        
        res.json(response);
    });



router.route("/pic")
    .get(function(req,res){
        var response = {};
        var done = false;
        Pic.find({},function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"message" : data};
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }

        res.json(response);
    });



router.route("/acc/:id/pic")
    .post(function(req, res) {


        upload(req,res,function(err){
            if(err){
                res.json({error_code:1,err_desc:err});
                return;
            }
            
            
            var pic = new Pic();
            pic.acc_id = req.params.id;

            pic.save(function(err){
                    if(err) {
                        response = {"error" : true,"id" : "error"};
                    }
                    else {
                        response = {"error" : false,"id" : pic.id};

                    fs.rename("./uploads/"+req.file.originalname,"./uploads/" + pic.id + ".jpg",function(err){
                        if(err){
                        res.json({error_code:1,err_desc:err});
                        return;
                        }

                        res.json(response);
                    });
                    }
                });
            });
    })
    .get(function(req, res) {
        var done = false;
        var response = {};

        Acc.findOne({ opp_id : req.params.id },function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
                res.json(response);
            } else {
                console.log(data);
                filter = [{acc_id : req.params.id}]


                if (data != null)
                {
                    filter.push({acc_id : data._id});
                }

                Pic.find({ $or : filter} , function(err, pic_data) {
                    if (err) {
                        response = { "error" : true, "pics" : "error" }
                    }
                    else
                    {
                        response = { "error" : false, "pics" : pic_data }
                    }

                    done = true;
            });

        }});
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }
        
        res.json(response);
    });

router.route("/acc/:id/desc")
    .post(function(req, res) {
        var response = {};
        Acc.findById(req.params.id,function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                if(req.body.desc !== undefined) {
                   data.desc = req.body.desc;
                }
                data.save(function(err){
                    if(err) {
                        response = {"error" : true,"message" : "Error updating data"};
                    } else {
                        response = {"error" : false,"message" : "Data is updated"};
                    }
                    res.json(response);
                })
            }
        });

    });

router.route("/acc/:id/wit")
    .post(function(req, res) {

        var response = {};
        var done = false;

        for (var index in req.body.witlist)
        {
        var user = new Inv();
        var done = false;

        user.name = req.body.witlist[index].name;
        user.acc_id = req.params.id;
        user.phone = req.body.witlist[index].phone;
        console.log(user);
        user.save(function(err){
            if(err) {
                response = {"error" : true,"user" : "error"};
                res.json(response);
            }

        });


        }

        res.json({});
    })
    .get(function(req, res) {
        var done = false;
        var response = {};

        Acc.findOne({ opp_id : req.params.id },function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
                res.json(response);
            } else {
                console.log(data);
                filter = [{acc_id : req.params.id}]

                if (data != null)
                {
                    filter.push({acc_id : data._id});
                }

                Inv.find({ $or : filter} , function(err, inv_data) {
                    if (err) {
                        response = { "error" : true, "pics" : "error" }
                    }
                    else
                    {
                        response = { "error" : false, "pics" : inv_data }
                    }

                    done = true;
            });

        }});
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }
        
        res.json(response);
    });

router.route("/user/:id/acc")
    .get(function(req,res){
        var done = false;
        var response = {};

        Acc.find({$or: [{my_id : req.params.id}]},function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
                res.json(response);
            } else {

                response = _.extend(response, {"acc" : data});
            }

            done = true;

        });
        
        while (done === false)
        {
            require('deasync').runLoopOnce();
        }
        
        res.json(response);
    });

router.route("/users/:id")
    .get(function(req,res){
        var response = {};
        mongoOp.findById(req.params.id,function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"message" : data};
            }
            res.json(response);
        });
    })
    .put(function(req,res){
        var response = {};
        mongoOp.findById(req.params.id,function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                if(req.body.name !== undefined) {
                    data.name = req.body.name;
                }
                if(req.body.phone !== undefined) {
                    data.phone = req.body.phone;
                }
                if(req.body.email !== undefined) {
                    data.email = req.body.email;
                }
                data.save(function(err){
                    if(err) {
                        response = {"error" : true,"message" : "Error updating data"};
                    } else {
                        response = {"error" : false,"message" : "Data is updated"};
                    }
                    res.json(response);
                })
            }
        });
    })
    .delete(function(req,res){
        var response = {};
        mongoOp.findById(req.params.id,function(err,data){
            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                mongoOp.remove({_id : req.params.id},function(err){
                    if(err) {
                        response = {"error" : true,"message" : "Error deleting data"};
                    } else {
                        response = {"error" : true,"message" : "Data associated with "+req.params.id+"is deleted"};
                    }
                    res.json(response);
                });
            }
        });
    })

app.use('/',router);
app.use(express.static('uploads'));
mongoose.connect('mongodb://localhost:27017/demoDb');
app.listen(3000);
console.log("Listening to PORT 3000");
