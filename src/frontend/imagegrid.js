let fs = require('fs');
let net = require('net');

let dirPath = "./data";

fs.readdir(dirPath, function(err, files) {

    if (err) {

        console.error("Could not open the directory.", err);
        process.exit(1);
    }

    files.forEach(function(file) {

        let dataset = JSON.parse(file);

        for (let i = 0; i < dataset.length; i++) {

            let image = dataset[i];
            let htmlImgElement = document.createElement("img");
            htmlImgElement.src = "data:image/png;base64," + image["base64"];
            htmlImgElement.alt = image["name"];
        }
    });
});

client.end();
