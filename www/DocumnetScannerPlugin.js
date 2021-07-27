var exec = require('cordova/exec');

exports.scanDoc = function (options, success, error) {
    var args={
        "sourceType": (options.sourceType!=undefined  )?options.sourceType:1,
        "CROP_ENABLE": (options.crop!=undefined )?options.crop:false,
        "targetHeight": (options.targetHeight!=undefined  )?options.targetHeight:0,
        "targetWidth": (options.targetWidth!=undefined  )?options.targetWidth:0,
        "edgeDetection": (options.edgeDetection!=undefined  )?options.edgeDetection:false,
        "base64": (options.base64!=undefined  )?options.base64:false
    }
console.log( options.sourceType)
    exec(success, error, 'DocumnetScannerPlugin', null, [args]);
};
