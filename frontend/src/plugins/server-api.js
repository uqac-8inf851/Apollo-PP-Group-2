var axios = require('axios');

module.exports = (function () {

    var getProgram = () => new Promise(function (resolve) {
        var url = 'http://localhost:8080/program';

        axios.get(url).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    var getProject = () => new Promise(function (resolve) {
        var url = 'http://localhost:8080/project';

        axios.get(url).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    var getTask = () => new Promise(function (resolve) {
        var url = 'http://localhost:8080/task';

        axios.get(url).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    return {
        getProgram: getProgram,
        getProject: getProject,
        getTask: getTask
    };

}());