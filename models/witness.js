var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var witSchema  =  new Schema ({
    "name" : String,
    "phone" : String
});
module.exports = mongoose.model('witness',witSchema);
