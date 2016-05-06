var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var userSchema  =  new Schema ({
    "userEmail" : String,
    "userPassword" : String
});
module.exports = mongoose.model('userLogin',userSchema);;
