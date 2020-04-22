package student;

public class Course implements Comparable<Course> {
	public String id;
	public String name;
	public String department;
	public String instructor;
	public boolean selected = false;
	
	public Course() {
		
	}
	
	public Course(String id, String name, String dep_id, String instructor, boolean selected) {
		this.id = id;
		this.name = name;
		this.department = dep_id;
		this.instructor = instructor;
		this.selected = selected;
	}

	@Override
	public int compareTo(Course o) {
		return this.id.compareTo(o.id);
	}

}
