var mongoose    =   require("mongoose");
var Schema =   mongoose.Schema;
var pictureSchema  =  new Schema ({
    "acc_id" : String
});
module.exports = mongoose.model('pictureSchema',pictureSchema);;
