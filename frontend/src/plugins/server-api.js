var axios = require('axios');

module.exports = (function () {

    var getProgram = () => new Promise(function (resolve) {
        var url = 'http://localhost:8080/program';

        axios.get(url).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    var saveProgram = (program) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/program';

        if (program.id) {
            axios.put(url, program).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        } else {
            axios.post(url, program).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        }
    });

    var delProgram = (program) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/program';

        axios.delete(url, program).then(response => (
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

    var saveProject = (project) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/project';

        if (project.id) {
            axios.put(url, project).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        } else {
            axios.post(url, project).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        }
    });

    var delProject = (project) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/project';

        axios.delete(url, project).then(response => (
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

    var saveTask = (task) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/task';

        if (task.id) {
            axios.put(url, task).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        } else {
            axios.post(url, task).then(response => (
                console.log(response.data),
                resolve(response.data)
            ));
        }
    });

    var delTask = (task) => new Promise(function (resolve) {
        var url = 'http://localhost:8080/task';

        axios.delete(url, task).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    return {
        getProgram,
        saveProgram,
        delProgram,
        getProject,
        saveProject,
        delProject,
        getTask,
        saveTask,
        delTask
    };

}());