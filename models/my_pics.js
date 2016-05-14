var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var myPicSchema  =  new Schema ({
    "user_id" : String,
    "orignal_name" : String
});
module.exports = mongoose.model('myPicSchema',myPicSchema);;
