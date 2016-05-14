var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var userSchema  =  new Schema ({
    "name" : String,
    "acc_id" : String,
    "phone" : String
});
module.exports = mongoose.model('involoved',userSchema);;
