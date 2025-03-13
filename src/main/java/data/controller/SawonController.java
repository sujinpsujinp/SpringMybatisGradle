package data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SawonController {
	
	@GetMapping({"/"})
	public String mainpage()
	{
		return "sawon/mainpage";		
	}
	
	@GetMapping({"/list"})
	public String sawonlist()
	{
		return "sawon/sawonlist";		
	}
	
	@GetMapping("/form")
	public String sawonform()
	{
		return "sawon/sawonform";		
	}
	
}
