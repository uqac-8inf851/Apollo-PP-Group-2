var axios = require('axios');

module.exports = (function () {

    const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8081';

    var getProgram = () => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/program`;

        axios.get(url).then(response => (
            resolve(response.data)
        ));
    });

    var saveProgram = (program) => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/program`;
        const DTO = Object.assign({"programTitle": program.programTitle, "programDescription": program.programDescription});
        if (program.item.addDate) {
            url = program.item._links.program.href;
            axios.put(url, DTO).then(response => (
                resolve(response.data)
            ));
        } else {
            axios.post(url, DTO).then(response => (
                resolve(response.data)
            ));
        }
    });

    var delProgram = (program) => new Promise(function (resolve) {
        var url = program._links.program.href;

        axios.delete(url).then(response => (
            resolve(response.data)
        ));
    });

    var getProject = () => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/project`;

        axios.get(url).then(response => (
            resolve(response.data)
        ));
    });

    var getProjectByProgram = (program) => new Promise(function (resolve) {
        var url = program._links.projects.href;

        axios.get(url).then(response => (
            resolve(response.data)
        ));
    });

    var saveProject = (project) => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/project`;
        const DTO = Object.assign({"projectTitle": project.projectTitle, "projectDescription": project.projectDescription, "program": project.program._links.program.href});
        if (project.item.addDate) {
            url = project.item._links.project.href;
            axios.put(url, DTO).then(response => (
                resolve(response.data)
            ));
        } else {
            axios.post(url, DTO).then(response => (
                resolve(response.data)
            ));
        }
    });

    var delProject = (project) => new Promise(function (resolve) {
        var url = project._links.project.href;

        axios.delete(url).then(response => (
            resolve(response.data)
        ));
    });

    var getTask = () => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/task`;

        axios.get(url).then(response => (
            console.log(response.data),
            resolve(response.data)
        ));
    });

    var saveTask = (task) => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/task`;

        if (task.id) {
            url += `/${task.id}`;
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
        var url = `${BACKEND_URL}/task`;

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
        getProjectByProgram,
        saveProject,
        delProject,
        getTask,
        saveTask,
        delTask
    };

}());