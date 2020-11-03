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
            resolve(response.data)
        ));
    });

    var getTaskByProject = (project) => new Promise(function (resolve) {
        var url = project._links.tasks.href;

        axios.get(url).then(response => (
            resolve(response.data)
        ));
    });

    var saveTask = (task) => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/task`;
        const DTO = Object.assign({"taskTitle": task.taskTitle, "taskDescription": task.taskDescription, "project": task.project._links.project.href, "category": "http://localhost:8081/category/1", "status" : "http://localhost:8081/status/1"});
        if (task.item.addDate) {
            url = task.item._links.task.href;
            axios.put(url, DTO).then(response => (
                resolve(response.data)
            ));
        } else {
            axios.post(url, DTO).then(response => (
                resolve(response.data)
            ));
        }
    });

    var delTask = (task) => new Promise(function (resolve) {
        var url = task._links.task.href;

        axios.delete(url).then(response => (
            resolve(response.data)
        ));
    });

    var saveTrack = (track) => new Promise(function (resolve) {
        var url = `${BACKEND_URL}/track`;
        const DTO = Object.assign({"startTime": track.startTime, "endTime": track.endTime, "task": track.task._links.task.href, "person" : "http://localhost:8081/person/3"});
        if (track.endTime) {
            url = track._links.track.href;
            axios.put(url, DTO).then(response => (
                resolve(response.data)
            ));
        } else {
            axios.post(url, DTO).then(response => (
                resolve(response.data)
            ));
        }
    });

    var getTrackByTask = (task) => new Promise(function (resolve) {
        var url = task._links.tracks.href;

        axios.get(url).then(response => (
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
        getTaskByProject,
        saveTask,
        delTask,
        saveTrack,
        getTrackByTask
    };

}());