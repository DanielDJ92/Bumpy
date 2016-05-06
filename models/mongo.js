var mongoose    =   require("mongoose");
mongoose.connect('mongodb://localhost:27017/demoDb');
var Schema =   mongoose.Schema;
var userSchema  =  new Schema ({
    "userEmail" : String,
    "userPassword" : String
});
module.exports = mongoose.model('userLogin',userSchema);;
