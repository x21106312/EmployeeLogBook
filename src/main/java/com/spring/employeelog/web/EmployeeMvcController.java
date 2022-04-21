package com.spring.employeelog.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.lowagie.text.DocumentException;
import com.spring.employeelog.exception.RecordNotFoundException;
import com.spring.employeelog.model.EmployeeEntity;
import com.spring.employeelog.repository.EmployeeRepository;
import com.spring.employeelog.service.EmployeePdfExporter;
import com.spring.employeelog.service.EmployeeService;




@Controller
@RequestMapping("/")
public class EmployeeMvcController 
{
	@Autowired
	EmployeeService service;
	
   public EmployeeRepository employeeRepository;
	
	
	  public EmployeeMvcController(EmployeeRepository theEmployeeRepository) {
		  employeeRepository = theEmployeeRepository; }
	 
	
	@RequestMapping
	public String getAllEmployees(Model model) 
	{
		List<EmployeeEntity> list = service.getAllEmployees();

		model.addAttribute("employees", list);
		return "list-employees";
	}

	@RequestMapping(path = {"/edit", "/edit/{id}"})
	public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id) 
							throws RecordNotFoundException 
	{
		if (id.isPresent()) {
			EmployeeEntity entity = service.getEmployeeById(id.get());
			model.addAttribute("employee", entity);
		} else {
			model.addAttribute("employee", new EmployeeEntity());
		}
		return "add-edit-employee";
	}
	
	@RequestMapping(path = "/delete/{id}")
	public String deleteEmployeeById(Model model, @PathVariable("id") Long id) 
							throws RecordNotFoundException 
	{
		service.deleteEmployeeById(id);
		return "redirect:/";
	}

	@RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
	public String createOrUpdateEmployee(EmployeeEntity employee) 
	{
		service.createOrUpdateEmployee(employee);
		return "redirect:/";
	}
	@GetMapping("/pdf")
	public void exportToPdf(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat theDateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = theDateFormatter.format(new Date());
		
		String headerKey = "Content-Desposition";
		String headerValue = "attachment; filename=students_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		List<EmployeeEntity> list = service.getAllEmployees();
		
		
		//List<EmployeeEntity> theStudent = (List<EmployeeEntity>) repository.findAll();
		
		EmployeePdfExporter exporter = new EmployeePdfExporter(list);
		exporter.exportPdf(response);
	}
	
	
	
	@GetMapping("/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		
		DateFormat theDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateTime = theDateFormatter.format(new Date());
		
		String fileName = "students" + currentDateTime + ".csv";
		String headerKey = "Content-Desposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
		List<EmployeeEntity> theStudent = employeeRepository.findAll(Sort.by("lastName"));
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"First Name", "Last Name", "Email", "ID"};
		String[] csvData = {"firstName", "lastName", "email", "roll"};
		
		csvWriter.writeHeader(csvHeader);
		
		for(EmployeeEntity student : theStudent) {
			csvWriter.write(student, csvData);
		}
		csvWriter.close();
	}
	
	 
}
