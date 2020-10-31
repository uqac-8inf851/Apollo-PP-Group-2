package com.apollo.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.apollo.backend.model.Category;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.model.Status;
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;

@Component
@Scope("prototype")
public class TaskBuilder {

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StatusRepository statusRepository;

	private Task task;

	private Program program;

	private Project project;

	private Category category;

	private Status status;

	private String title;
	private String description;

	public Status getStatus() {
		if(status == null)
			init();

		return status;
	}

	public Category getCategory() {
		if(category == null)
			init();

		return category;
	}

	public Project getProject() {
		if(project == null)
			init();

		return project;
	}

	public Program getProgram() {
		if(program == null)
			init();

		return program;
	}

	public Task getTask() {
		if(task == null)
			init();

		return task;
	}

	public TaskBuilder(String title, String description) {
		this.title = title;
		this.description = description;
	}

	private void init() {
		program = programRepository.save(new Program("title", "description"));

		project = projectRepository.save(new Project("title", "description", program));

		category = categoryRepository.save(new Category("name"));

		status = statusRepository.save(new Status("name"));

		task = new Task(title, description, 0, project, category, status);
	}
}