package Models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.engine.internal.Cascade;
import org.hibernate.engine.spi.CascadeStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String taskText;
	private String creatorsSolution;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	//@OnDelete(action = OnDeleteAction.CASCADE)
	private RegisteredUser user;
	
	@OneToMany(mappedBy="task", orphanRemoval=true)
	private List<Test> tests;
	
	@OneToMany(mappedBy="task", orphanRemoval=true)
	private List<Solution> solutions;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTaskText() {
		return taskText;
	}
	public void setTaskText(String taskText) {
		this.taskText = taskText;
	}
	public String getCreatorsSolution() {
		return creatorsSolution;
	}
	public void setCreatorsSolution(String creatorsSolution) {
		this.creatorsSolution = creatorsSolution;
	}
	public RegisteredUser getUser() {
		return user;
	}
	public void setUser(RegisteredUser user) {
		this.user = user;
	}
	
	
	@JsonIgnore
	public List<Test> getTests() {
		return tests;
	}
	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
	
	@JsonIgnore
	public List<Solution> getSolutions() {
		return solutions;
	}
	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	public Task() {}
}
