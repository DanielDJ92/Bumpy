var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var accSchema  =  new Schema ({
    "desc" : String,
    "location" : String,
    "my_id" : String,
    "opp_id" : String
});
module.exports = mongoose.model('acc',accSchema);
