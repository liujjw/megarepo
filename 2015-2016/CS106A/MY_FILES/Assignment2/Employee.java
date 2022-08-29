
public class Employee {
	
	public Employee(String newName, String newSupervisor, double newSalary){
		name = newName;
		supervisor = newSupervisor;
		salary = newSalary;
	}
	
	public String getName(){
		return name;
	}
	public String getSupervisor(){
		return supervisor;
	}
	public double getSalary(){
		return salary;
	}
	public void setName(String setName){
		name = setName;
	}
	public void setSupervisor(String setSupervisor){
		supervisor = setSupervisor;
	}
	public void setSalary(double setSalary){
		salary = setSalary;
	}
	
	public String toString(){
		return (name + "'s sueprvisor is "+ supervisor +". His salary is "+ salary);
	}
	
	private String name;
	private String supervisor;
	private double salary;
}
