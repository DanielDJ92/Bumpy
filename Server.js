var express     =   require("express");
var app         =   express();
var bodyParser  =   require("body-parser");
var mongoOp     =   require("./models/users");
var Acc        =   require("./models/acc");
var Pic        =   require("./models/pics");
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
        var user = new mongoOp();
        user.userEmail = req.body.email;
        user.userPassword = req.body.password;
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

router.route("/user/:id/acc")
    .get(function(req,res){
        var done = false;
        var response = {};

        Acc.find({$or: [{my_id : req.params.id}, {opp_id : req.params.id}]},function(err,data){
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
                if(req.body.userEmail !== undefined) {
                    data.userEmail = req.body.userEmail;
                }
                if(req.body.userPassword !== undefined) {
                    data.userPassword = req.body.userPassword;
                }
                data.save(function(err){
                    if(err) {
                        response = {"error" : true,"message" : "Error updating data"};
                    } else {
                        response = {"error" : false,"message" : "Data is updated for "+req.params.id + " " + data.userPassword};
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
mongoose.connect('mongodb://localhost:27017/demoDb');
app.listen(3000);
console.log("Listening to PORT 3000");
