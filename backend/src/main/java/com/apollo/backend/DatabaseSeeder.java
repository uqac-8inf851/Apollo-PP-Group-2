package com.apollo.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.apollo.backend.model.Category;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.model.Status;
import com.apollo.backend.model.Task;
import com.apollo.backend.model.Team;
import com.apollo.backend.model.User;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;
import com.apollo.backend.repository.TaskRepository;
import com.apollo.backend.repository.TeamRepository;
import com.apollo.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public DatabaseSeeder() {}

    @EventListener
    public void seed(ContextRefreshedEvent event) {

        seedUser();
        seedTeam();
        seedCategory();
        seedStatus();
        seedProgram();
        seedProject();
        seedTask();
        seedTrack();
	}

	private void seedTrack() {
    }

    private void seedTask() {
        List<Project> projectList = new ArrayList<Project>();
		projectRepository.findAll().forEach(projectList::add);
		Optional<Project> project = projectList.stream().filter(x -> x.getTitle().equalsIgnoreCase("Build starship")).findFirst();

        List<Category> categoryList = new ArrayList<Category>();
		categoryRepository.findAll().forEach(categoryList::add);
        Optional<Category> category = categoryList.stream().filter(x -> x.getName().equalsIgnoreCase("Category 1")).findFirst();
        
        List<Status> statusList = new ArrayList<Status>();
		statusRepository.findAll().forEach(statusList::add);
		Optional<Status> status = statusList.stream().filter(x -> x.getName().equalsIgnoreCase("Backlog")).findFirst();

		Task task = new Task("Build rocket first stage", "Let's do it!", 0, project.get(), category.get(), status.get());

        List<Task> news = new ArrayList<Task>();
        news.add(task);

        Iterable<Task> exists = taskRepository.findAll();

		for (Task nItem : news) {
			boolean notExist = true;

			for (Task item : exists) {
				if(item.getTitle().equalsIgnoreCase(nItem.getTitle()))
					notExist = false;
			}

			if(notExist)
                taskRepository.save(nItem);
		}
    }

    private void seedProject() {
        List<Program> programList = new ArrayList<Program>();
		programRepository.findAll().forEach(programList::add);
		Optional<Program> program = programList.stream().filter(x -> x.getTitle().equalsIgnoreCase("Apollo 11")).findFirst();

		Project project = new Project("Build starship", "We have to build the rocket to go to the moon.", program.get());

        List<Project> news = new ArrayList<Project>();
        news.add(project);

        Iterable<Project> exists = projectRepository.findAll();

		for (Project nItem : news) {
			boolean notExist = true;

			for (Project item : exists) {
				if(item.getTitle().equalsIgnoreCase(nItem.getTitle()))
					notExist = false;
			}

			if(notExist)
                projectRepository.save(nItem);
		}
    }

    private void seedProgram() {
        List<Program> news = new ArrayList<Program>();
        news.add(new Program("Apollo 9", "Earth orbit"));
        news.add(new Program("Apollo 10", "Moon orbit"));
        news.add(new Program("Apollo 11", "Moon landing"));
        
		Iterable<Program> exists = programRepository.findAll();

		for (Program nItem : news) {
			boolean notExist = true;

			for (Program item : exists) {
				if(item.getTitle().equalsIgnoreCase(nItem.getTitle()))
					notExist = false;
			}

			if(notExist)
                programRepository.save(nItem);
		}
    }

    private void seedStatus() {
        List<Status> news = new ArrayList<Status>();
        news.add(new Status("Backlog"));
        news.add(new Status("Doing"));
        news.add(new Status("Done"));

		Iterable<Status> exists = statusRepository.findAll();

		for (Status nItem : news) {
			boolean notExist = true;

			for (Status item : exists) {
				if(item.getName().equalsIgnoreCase(nItem.getName()))
					notExist = false;
			}

			if(notExist)
                statusRepository.save(nItem);
		}
    }

    private void seedCategory() {
        List<Category> news = new ArrayList<Category>();
        news.add(new Category("Category 1"));
        news.add(new Category("Category 2"));
        news.add(new Category("Category 3"));

		Iterable<Category> exists = categoryRepository.findAll();

		for (Category nItem : news) {
			boolean notExist = true;

			for (Category item : exists) {
				if(item.getName().equalsIgnoreCase(nItem.getName()))
					notExist = false;
			}

			if(notExist)
                categoryRepository.save(nItem);
		}
    }

    private void seedTeam() {
		List<User> userList = new ArrayList<User>();
		userRepository.findAll().forEach(userList::add);

		Team team = new Team("PP Group 2");
		team.setUser(userList);

        List<Team> news = new ArrayList<Team>();
        news.add(team);

        Iterable<Team> exists = teamRepository.findAll();

		for (Team nItem : news) {
			boolean notExist = true;

			for (Team item : exists) {
				if(item.getName().equalsIgnoreCase(nItem.getName()))
					notExist = false;
			}

			if(notExist)
                teamRepository.save(nItem);
		}
    }

    private void seedUser() {
		List<User> news = new ArrayList<User>();
        news.add(new User("Fabio", "Lab Head"));
        news.add(new User("Carlos", "Team Member"));
        news.add(new User("Mário", "Team Member"));
        news.add(new User("Eduardo", "Team Member"));

		Iterable<User> exists = userRepository.findAll();

		for (User nItem : news) {
			boolean notExist = true;

			for (User item : exists) {
				if(item.getName().equalsIgnoreCase(nItem.getName()))
					notExist = false;
			}

			if(notExist)
                userRepository.save(nItem);
		}
	}
}